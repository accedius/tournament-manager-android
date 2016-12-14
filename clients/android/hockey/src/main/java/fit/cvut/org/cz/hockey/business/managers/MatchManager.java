package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.PlayerStat;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBHelper;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.generators.AllPlayAllMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class MatchManager extends BaseManager<Match> implements IMatchManager {
    protected HockeyDBHelper sportDBHelper;

    public MatchManager(Context context, ICorePlayerManager corePlayerManager, HockeyDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<Match, Long> getDao() {
        return DatabaseFactory.getDBeHelper(context).getHockeyMatchDAO();
    }

    @Override
    public List<Match> getByTournamentId(long tournamentId) {
        try {
            List<Match> matches = getDao().queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            //return fillMatch(dm);
            for (Match match : matches) {
                List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(match.getId());
                match.addParticipants(participants);

                for (Participant participant : participants) {
                    if (ParticipantType.home.toString().equals(participant.getRole()))
                        match.setHomeScore(ManagerFactory.getInstance(context).participantStatManager.getScoreByParticipantId(participant.getId()));
                    else if (ParticipantType.away.toString().equals(participant.getRole()))
                        match.setAwayScore(ManagerFactory.getInstance(context).participantStatManager.getScoreByParticipantId(participant.getId()));
                }
            }
            // TODO match participant stats and match players stats

            Collections.sort(matches, new Comparator<Match>() {
                @Override
                public int compare(Match lhs, Match rhs) {
                    if (lhs.getRound() != rhs.getRound()) return lhs.getRound() - rhs.getRound();
                    return lhs.getPeriod() - rhs.getPeriod();
                }
            });
            return matches;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Match getById(long id) {
        Match match = super.getById(id);
        //return fillMatch(dm);
        // TODO load match stats, match participant stats and match players stats
        List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(id);
        for (Participant participant : participants) {
            match.addParticipant(participant);
            if (ParticipantType.home.toString().equals(participant.getRole()))
                match.setHomeName(participant.getName());
            else if (ParticipantType.away.toString().equals(participant.getRole()))
                match.setAwayName(participant.getName());
        }
        return match;
    }

    @Override
    public void beginMatch(long matchId) {
        Match match = getById(matchId);
        if (!(match.isPlayed())) {
            match.setPlayed(true);
            match.setLastModified(new Date());
            update(match);
        }
    }

    @Override
    public void generateRound(long tournamentId) {
        List<Team> teams = ManagerFactory.getInstance(context).teamManager.getByTournamentId(tournamentId);
        Map<Long, Team> teamMap = new HashMap<>();
        ArrayList<Participant> partsForGenerator = new ArrayList<>();

        for (Team team : teams) {
            teamMap.put(team.getId(), team);
            // match_id and role will be added by generator
            partsForGenerator.add(new Participant(-1, team.getId(), null));
        }

        int lastRound = 0;
        List<Match> matches = getByTournamentId(tournamentId);
        for (Match match : matches) {
            if (match.getRound() > lastRound)
                lastRound = match.getRound();
        }

        IMatchGenerator generator = new AllPlayAllMatchGenerator();
        List<fit.cvut.org.cz.tmlibrary.data.entities.Match> matchList = generator.generateRound(partsForGenerator, lastRound + 1);

        for (fit.cvut.org.cz.tmlibrary.data.entities.Match match : matchList) {
            match.setDate(new Date());
            match.setNote("");
            match.setTournamentId(tournamentId);
            Match hockeyMatch = new Match(match);
            insert(hockeyMatch);
            List<PlayerStat> playerStats = new ArrayList<>();
            for (Participant participant : match.getParticipants())
                for (Player player : teamMap.get(participant.getParticipantId()).getPlayers())
                    playerStats.add(new PlayerStat(participant.getId(), player.getId()));

            for (PlayerStat playerStat : playerStats)
                ManagerFactory.getInstance(context).playerStatManager.insert(playerStat);
        }
    }

    @Override
    public void resetMatch(long matchId) {
        Match match = getById(matchId);
        if (!match.isPlayed())
            return;

        List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(matchId);

        try {
            // Remove Participant Stats and Player Stats
            for (Participant participant : participants) {
                DeleteBuilder<ParticipantStat, Long> participantStatBuilder = sportDBHelper.getParticipantStatDAO().deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();

                DeleteBuilder<fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat, Long> playerStatBuilder = sportDBHelper.getPlayerStatDAO().deleteBuilder();
                playerStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                playerStatBuilder.delete();
            }
        } catch (SQLException e) {}

        match.setPlayed(false);
        match.setOvertime(false);
        match.setShootouts(false);
        update(match);
    }

    @Override
    public void insert(Match match) {
        // TODO check if id is filled ?
        match.setLastModified(new Date());
        super.insert(match);

        for (Participant participant : match.getParticipants()) {
            participant.setMatchId(match.getId());
        }

        for (Participant participant : match.getParticipants())
            ManagerFactory.getInstance(context).participantManager.insert(participant);
    }

    @Override
    public boolean delete(long id) {
        List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(id);
        try {
            for (Participant participant : participants) {
                DeleteBuilder<ParticipantStat, Long> participantStatBuilder = sportDBHelper.getParticipantStatDAO().deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();

                DeleteBuilder<fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat, Long> statBuilder = sportDBHelper.getPlayerStatDAO().deleteBuilder();
                statBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                statBuilder.delete();
            }
            DeleteBuilder<Participant, Long> participantBuilder = sportDBHelper.getParticipantDAO().deleteBuilder();
            participantBuilder.where().eq(DBConstants.cMATCH_ID, id);
            participantBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        super.delete(id);
        return true;
    }
}
