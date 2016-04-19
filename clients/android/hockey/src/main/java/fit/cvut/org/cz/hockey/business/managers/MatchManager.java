package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class MatchManager implements IScoredMatchManager {

    @Override
    public ArrayList<ScoredMatch> getByTournamentId(Context context, long tournamentId) {

        ArrayList<DMatch> dMatches = DAOFactory.getInstance().matchDAO.getByTournamentId( context, tournamentId );
        ArrayList<ScoredMatch> res = new ArrayList<>();

        for( DMatch dm : dMatches )
        {
            //TODO predelat na convertToDMatch
            ScoredMatch match = new ScoredMatch();
            match.setRound(dm.getRound());
            match.setPeriod(dm.getPeriod());
            match.setId(dm.getId());
            match.setTournamentId(dm.getTournamentId());
            match.setDate(dm.getDate());
            match.setPlayed(dm.isPlayed());
            match.setUid(dm.getUid());
            match.setEtag(dm.getEtag());
            match.setLastModified(dm.getLastModified());
            match.setLastSynchronized(dm.getLastSynchronized());
            ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId( context, dm.getId());

            for( DParticipant dp : participants )
            {
                if( dp.getRole().equals( ParticipantType.home.toString()) )
                {
                    match.setHomeParticipantId( dp.getTeamId() );
                    DTeam dt = DAOFactory.getInstance().teamDAO.getById( context, dp.getTeamId() );
                    match.setHomeName( dt.getName() );
                    match.setHomeIds( dp.getPlayerIds() );
                }
                if( dp.getRole().equals(ParticipantType.away.toString()) )
                {
                    match.setAwayParticipantId(dp.getTeamId());
                    DTeam dt = DAOFactory.getInstance().teamDAO.getById( context, dp.getTeamId() );
                    match.setAwayName( dt.getName() );
                    match.setAwayIds(dp.getPlayerIds());
                }
            }
            //TODO skore podle participantu do scoredMatch


            res.add(match);
        }


        return res;
    }

    @Override
    public ScoredMatch getById(Context context, long Id) {

        DMatch dm = DAOFactory.getInstance().matchDAO.getById( context, Id );
        //TODO predelat na convert to DMatch
        ScoredMatch match = new ScoredMatch();
        match.setRound(dm.getRound());
        match.setPeriod(dm.getPeriod());
        match.setId(dm.getId());
        match.setTournamentId(dm.getTournamentId());
        match.setDate(dm.getDate());
        match.setPlayed( dm.isPlayed() );
        match.setUid( dm.getUid());
        match.setEtag( dm.getEtag());
        match.setLastModified( dm.getLastModified());
        match.setLastSynchronized( dm.getLastSynchronized());
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId( context, dm.getId());

        for( DParticipant dp : participants )
        {
            if( dp.getRole().equals( ParticipantType.home.toString()) )
            {
                match.setHomeParticipantId( dp.getTeamId() );
                DTeam dt = DAOFactory.getInstance().teamDAO.getById( context, dp.getTeamId() );
                match.setHomeName( dt.getName() );
                match.setHomeIds( dp.getPlayerIds() );
            }
            if( dp.getRole().equals(ParticipantType.away.toString()) )
            {
                match.setAwayParticipantId(dp.getTeamId());
                DTeam dt = DAOFactory.getInstance().teamDAO.getById( context, dp.getTeamId() );
                match.setAwayName( dt.getName() );
                match.setAwayIds(dp.getPlayerIds());
            }
        }
        //TODO doplnit skore od Participant statistik
        return match;
    }

    @Override
    public void beginMatch(Context context, long matchId) {

        ScoredMatch match = getById( context, matchId );

        if( !(match.isPlayed())) {
            ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
            for (DParticipant dp : participants) {
                long teamId = dp.getTeamId();
                ArrayList<Long> plIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTeam(context, teamId);
                dp.setPlayerIds(plIds);
                DAOFactory.getInstance().participantDAO.update(context, dp, true);
            }
            match.setPlayed( true );
            match.setLastModified( new Date());
            update( context, match);

        }
    }

    @Override
    public void insert(Context context, ScoredMatch match) {
        DMatch dMatch = new DMatch();
        dMatch.setDate(match.getDate());
        dMatch.setPeriod(match.getPeriod());
        dMatch.setRound(match.getRound());
        dMatch.setTournamentId(match.getTournamentId());
        dMatch.setNote(match.getNote());
        dMatch.setPlayed( false );
        long matchId = DAOFactory.getInstance().matchDAO.insert(context, dMatch);

        //TODO pozor na participant ID, chces si tam predavat team id, tak v tom scored match musi byt jako participant id team id
        DParticipant homeParticipant = new DParticipant( -1, match.getHomeParticipantId(), matchId, ParticipantType.home.toString() );
        homeParticipant.setPlayerIds( match.getHomeIds() );
        DParticipant awayParticipant = new DParticipant( -1, match.getAwayParticipantId(), matchId, ParticipantType.away.toString() );
        awayParticipant.setPlayerIds( match.getAwayIds() );

        DAOFactory.getInstance().participantDAO.insert(context, homeParticipant, false);
        DAOFactory.getInstance().participantDAO.insert( context, awayParticipant, false);


    }

    @Override
    public void update(Context context, ScoredMatch match) {
        DMatch dMatch = new DMatch();
        dMatch.setId(match.getId());
        dMatch.setDate(match.getDate());
        dMatch.setPeriod(match.getPeriod());
        dMatch.setRound(match.getRound());
        dMatch.setTournamentId(match.getTournamentId());
        dMatch.setNote(match.getNote());
        dMatch.setPlayed(match.isPlayed());
        dMatch.setUid(match.getUid());
        dMatch.setEtag(match.getEtag());
        dMatch.setLastSynchronized(match.getLastSynchronized());
        dMatch.setLastModified( match.getLastModified());
        DAOFactory.getInstance().matchDAO.update(context, dMatch);

    }

    @Override
    public void delete(Context context, long id) {

    }
}
