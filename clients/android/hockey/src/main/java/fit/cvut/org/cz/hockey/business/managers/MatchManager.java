package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.tmlibrary.business.RoundRobinScoredMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.entities.NewMatchSpinnerParticipant;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class MatchManager implements IScoredMatchManager {

    private ScoredMatch fillMatch ( Context context, DMatch dm )
    {
        ScoredMatch match = new ScoredMatch( dm );

        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId( context, dm.getId());

        for( DParticipant dp : participants )
        {
            ArrayList<DStat> partStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId( context, dp.getId());
            int teamGoals = 0;
            for( DStat stat : partStats ){
                if( StatsEnum.valueOf(stat.getStatsEnumId()) == StatsEnum.team_goals ){
                    teamGoals = Integer.parseInt(stat.getValue());
                }
            }

            if( dp.getRole().equals( ParticipantType.home.toString()) )
            {
                match.setHomeParticipantId(dp.getTeamId());
                DTeam dt = DAOFactory.getInstance().teamDAO.getById( context, dp.getTeamId() );
                match.setHomeName(dt.getName());
                ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant(context, dp.getId());
                match.setHomeIds(playerIds);
                match.setHomeScore( teamGoals );
            }
            if( dp.getRole().equals(ParticipantType.away.toString()) )
            {
                match.setAwayParticipantId(dp.getTeamId());
                DTeam dt = DAOFactory.getInstance().teamDAO.getById( context, dp.getTeamId() );
                match.setAwayName(dt.getName());
                ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant(context, dp.getId());
                match.setAwayIds(playerIds);
                match.setAwayScore( teamGoals );
            }
        }

        return match;
    }

    @Override
    public ArrayList<ScoredMatch> getByTournamentId(Context context, long tournamentId) {

        ArrayList<DMatch> dMatches = DAOFactory.getInstance().matchDAO.getByTournamentId( context, tournamentId );
        ArrayList<ScoredMatch> res = new ArrayList<>();

        for( DMatch dm : dMatches )
        {
            res.add( fillMatch(context, dm));
        }

        Collections.sort(res, new Comparator<ScoredMatch>() {
            @Override
            public int compare(ScoredMatch lhs, ScoredMatch rhs) {
                if( lhs.getRound() != rhs.getRound() ) return lhs.getRound() - rhs.getRound();
                return lhs.getPeriod() - rhs.getPeriod();
            }
        });


        return res;
    }

    @Override
    public ScoredMatch getById(Context context, long Id) {

        DMatch dm = DAOFactory.getInstance().matchDAO.getById( context, Id );

        return fillMatch( context, dm);
    }

    @Override
    public void beginMatch(Context context, long matchId) {

        ScoredMatch match = getById( context, matchId );

        if( !(match.isPlayed())) {
            DTournament tour = DAOFactory.getInstance().tournamentDAO.getById( context, match.getTournamentId());
            ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
            for (DParticipant dp : participants) {
                for( StatsEnum statEn : StatsEnum.values() ) {
                    if( statEn.isForPlayer() ) continue;
                    DStat statToAdd = new DStat(-1, -1, dp.getId(), statEn.toString(), match.getTournamentId(), tour.getCompetitionId(), String.valueOf(0));
                    DAOFactory.getInstance().statDAO.insert( context, statToAdd );
                }
            }
            match.setPlayed(true);
            match.setLastModified(new Date());
            update(context, match);

            DMatchStat matchStat = new DMatchStat(matchId, false, false);
            DAOFactory.getInstance().matchStatisticsDAO.createStatsForMatch(context, matchStat);

        }
    }

    @Override
    public void generateRound(Context context, long tournamentId) {
        ArrayList<DTeam> teamsInTournament = DAOFactory.getInstance().teamDAO.getByTournamentId( context, tournamentId );
        ArrayList<NewMatchSpinnerParticipant> partsForGenerator = new ArrayList<>();

        for( DTeam dTeam : teamsInTournament )
        {
            partsForGenerator.add( new NewMatchSpinnerParticipant( dTeam.getId(), dTeam.getName() ) );
        }

        int lastRound = 0;

        ArrayList<DMatch> tournMatches = DAOFactory.getInstance().matchDAO.getByTournamentId( context, tournamentId );

        for( DMatch match : tournMatches )
        {
            if( match.getRound() > lastRound ) lastRound = match.getRound();
        }


        IScoredMatchGenerator generator = new RoundRobinScoredMatchGenerator();

        ArrayList<ScoredMatch> matchList = generator.generateRound( partsForGenerator, lastRound + 1 );

        for( ScoredMatch match : matchList ){ // prepare matches for insert and insert them
            match.setDate( new Date());
            match.setNote("");
            match.setTournamentId(tournamentId);
            insert( context, match );
        }
    }

    @Override
    public void resetMatch(Context context, long matchId) {
        ScoredMatch match = getById( context, matchId );

        if( !match.isPlayed() ) return;

        delete( context, matchId );
        insert( context, match );
    }

    @Override
    public void insert(Context context, ScoredMatch match) {
        DMatch dMatch = ScoredMatch.convertToDMatch( match);

        dMatch.setLastModified(new Date());
        dMatch.setPlayed( false );
        long matchId = DAOFactory.getInstance().matchDAO.insert(context, dMatch);

        DParticipant homeParticipant = new DParticipant( -1, match.getHomeParticipantId(), matchId, ParticipantType.home.toString() );

        DParticipant awayParticipant = new DParticipant( -1, match.getAwayParticipantId(), matchId, ParticipantType.away.toString() );


        long homePartId = DAOFactory.getInstance().participantDAO.insert(context, homeParticipant);
        long awayPartId = DAOFactory.getInstance().participantDAO.insert( context, awayParticipant);



    }

    @Override
    public void update(Context context, ScoredMatch match) {
        DMatch dMatch = ScoredMatch.convertToDMatch( match );


        DAOFactory.getInstance().matchDAO.update(context, dMatch);

    }

    @Override
    public void delete(Context context, long id) {

        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId( context, id );
        for( DParticipant dp : participants )
        {
            ArrayList<DStat> participantStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId( context, dp.getId() );
            for( DStat ds : participantStats ) DAOFactory.getInstance().statDAO.delete( context, ds.getId() );
            DAOFactory.getInstance().participantDAO.delete( context, dp.getId() );
        }
        DAOFactory.getInstance().matchDAO.delete( context, id );
    }
}
