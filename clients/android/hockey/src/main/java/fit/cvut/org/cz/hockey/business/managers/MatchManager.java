package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
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
                long teamId = dp.getTeamId();
                ArrayList<Long> plIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTeam(context, teamId);

                DAOFactory.getInstance().participantDAO.update(context, dp);

                //Create stats for each participating player
                for( Long id : plIds ){
                    for( StatsEnum statEn : StatsEnum.values() ) {
                        if( !statEn.isForPlayer() ) continue;
                        DStat statToAdd = new DStat(-1, id, dp.getId(), statEn.toString(), match.getTournamentId(), tour.getCompetitionId(), String.valueOf(0));
                        DAOFactory.getInstance().statDAO.insert( context, statToAdd );
                    }

                }
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
    public void insert(Context context, ScoredMatch match) {
        DMatch dMatch = ScoredMatch.convertToDMatch( match);

        dMatch.setLastModified(new Date());
        dMatch.setPlayed( false );
        long matchId = DAOFactory.getInstance().matchDAO.insert(context, dMatch);

        //TODO pozor na participant ID, chces si tam predavat team id, tak v tom scored match musi byt jako participant id team id
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

    }
}
