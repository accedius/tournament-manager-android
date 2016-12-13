package fit.cvut.org.cz.squash.business.managers;

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

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.Match;
import fit.cvut.org.cz.squash.business.entities.ParticipantStat;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.generators.AllPlayAllMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class MatchManager extends BaseManager<Match> implements IMatchManager {
    protected SquashDBHelper sportDBHelper;

    public MatchManager(Context context, ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<Match, Long> getDao() {
        return DatabaseFactory.getDBeHelper(context).getSquashMatchDAO();
    }

    @Override
    public List<Match> getByTournamentId(long tournamentId) {
        try {
            List<Match> matches = getDao().queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (Match match : matches) {
                List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(match.getId());
                match.addParticipants(participants);
                for (Participant participant : participants) {
                    if (ParticipantType.home.toString().equals(participant.getRole()))
                        match.setHomeName(getParticipantName(match, participant));
                    else if (ParticipantType.away.toString().equals(participant.getRole()))
                        match.setAwayName(getParticipantName(match, participant));
                }
                loadMatchScore(match);
            }

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

    private String getParticipantName(Match match, Participant participant) {
        Tournament tournament = ManagerFactory.getInstance(context).tournamentManager.getById(match.getTournamentId());
        Competition competition = ManagerFactory.getInstance(context).competitionManager.getById(tournament.getCompetitionId());
        if (CompetitionTypes.teams().equals(competition.getType())) {
            Team team = ManagerFactory.getInstance(context).teamManager.getById(participant.getParticipantId());
            return team.getName();
        } else {
            Player player = ManagerFactory.getInstance(context).corePlayerManager.getPlayerById(participant.getParticipantId());
            return player.getName();
        }
    }

    @Override
    public Match getById(long id) {
        Match match = super.getById(id);
        List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(id);
        for (Participant participant : participants) {
            match.addParticipant(participant);

            if (ParticipantType.home.toString().equals(participant.getRole()))
                match.setHomeName(getParticipantName(match, participant));
            else if (ParticipantType.away.toString().equals(participant.getRole()))
                match.setAwayName(getParticipantName(match, participant));
        }
        loadMatchScore(match);
        return match;
    }

    @Override
    public void generateRound(long tournamentId) {
        Tournament tournament = ManagerFactory.getInstance(context).tournamentManager.getById(tournamentId);
        Competition competition = ManagerFactory.getInstance(context).competitionManager.getById(tournament.getCompetitionId());
        CompetitionType type = competition.getType();

        ArrayList<fit.cvut.org.cz.tmlibrary.business.entities.Participant> partsForGenerator = new ArrayList<>();

        if (CompetitionTypes.individuals().equals(type)) {
            List<Player> players = ManagerFactory.getInstance(context).tournamentManager.getTournamentPlayers(tournamentId);
            for (Player player : players) {
                // match_id and role will be added by generator
                partsForGenerator.add(new fit.cvut.org.cz.tmlibrary.business.entities.Participant(-1, player.getId(), null));
            }
        } else {
            List<Team> teams = ManagerFactory.getInstance(context).teamManager.getByTournamentId(tournamentId);
            for (Team team : teams) {
                // match_id and role will be added by generator
                partsForGenerator.add(new fit.cvut.org.cz.tmlibrary.business.entities.Participant(-1, team.getId(), null));
            }
        }

        int lastRound = 0;
        List<Match> matches = getByTournamentId(tournamentId);
        for (Match match : matches) {
            if (match.getRound() > lastRound)
                lastRound = match.getRound();
        }

        IMatchGenerator generator = new AllPlayAllMatchGenerator();
        List<fit.cvut.org.cz.tmlibrary.business.entities.Match> matchList = generator.generateRound(partsForGenerator, lastRound + 1);

        for (fit.cvut.org.cz.tmlibrary.business.entities.Match match : matchList) {
            match.setDate(new Date());
            match.setNote("");
            match.setTournamentId(tournamentId);
            Match squashMatch = new Match(match);
            insert(squashMatch);
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
                DeleteBuilder<ParticipantStat, Long> participantStatBuilder = sportDBHelper.getSquashParticipantStatDAO().deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();

                DeleteBuilder<fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat, Long> playerStatBuilder = sportDBHelper.getPlayerStatDAO().deleteBuilder();
                playerStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                playerStatBuilder.delete();
            }
        } catch (SQLException e) {}

        match.setPlayed(false);
        match.setSetsNumber(0);
        update(match);
    }

    @Override
    public void insert(Match match) {
        // TODO check if id is filled ?
        match.setLastModified(new Date());

        Tournament tournament = ManagerFactory.getInstance(context).tournamentManager.getById(match.getTournamentId());
        Competition competition = ManagerFactory.getInstance(context).competitionManager.getById(tournament.getCompetitionId());
        match.setType(competition.getType());
        super.insert(match);

        for (Participant participant : match.getParticipants()) {
            participant.setMatchId(match.getId());
        }

        for (Participant participant : match.getParticipants())
            ManagerFactory.getInstance(context).participantManager.insert(participant);

        Map<Long, Team> teamMap = new HashMap<>();
        List<Team> teams = ManagerFactory.getInstance(context).teamManager.getByTournamentId(match.getTournamentId());
        for (Team team : teams) {
            teamMap.put(team.getId(), team);
        }

        List<PlayerStat> playerStats = new ArrayList<>();
        for (Participant participant : match.getParticipants()) {
            if (CompetitionTypes.individuals().equals(match.getType())) {
                playerStats.add(new PlayerStat(participant.getId(), participant.getParticipantId()));
            } else {
                for (Player player : teamMap.get(participant.getParticipantId()).getPlayers())
                    playerStats.add(new PlayerStat(participant.getId(), player.getId()));
            }
        }

        for (PlayerStat playerStat : playerStats)
            ManagerFactory.getInstance(context).playerStatManager.insert(playerStat);
    }

    @Override
    public boolean delete(long id) {
        List<Participant> participants = ManagerFactory.getInstance(context).participantManager.getByMatchId(id);
        try {
            for (Participant participant : participants) {
                DeleteBuilder<fit.cvut.org.cz.tmlibrary.business.entities.ParticipantStat, Long> participantStatBuilder = sportDBHelper.getParticipantStatDAO().deleteBuilder();
                participantStatBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participant.getId());
                participantStatBuilder.delete();

                DeleteBuilder<fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat, Long> statBuilder = sportDBHelper.getPlayerStatDAO().deleteBuilder();
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

    private void loadMatchScore(Match match) {
        if (!match.isPlayed())
            return;

        Participant home = null, away = null;
        for (Participant participant : match.getParticipants()) {
            if (ParticipantType.home.toString().equals(participant.getRole())) {
                home = participant;
            } else if (ParticipantType.away.toString().equals(participant.getRole())) {
                away = participant;
            }
        }

        List<ParticipantStat> homeStats = ManagerFactory.getInstance(context).participantStatManager.getByParticipantId(home.getId());
        List<ParticipantStat> awayStats = ManagerFactory.getInstance(context).participantStatManager.getByParticipantId(away.getId());
        Map<Integer, Integer> homePointsMap = new HashMap<>();
        Map<Integer, Integer> awayPointsMap = new HashMap<>();

        home.setParticipantStats(homeStats);
        away.setParticipantStats(awayStats);

        for (ParticipantStat stat : homeStats) {
            homePointsMap.put(stat.getSetNumber(), stat.getPoints());
        }
        for (ParticipantStat stat : awayStats) {
            awayPointsMap.put(stat.getSetNumber(), stat.getPoints());
        }

        int homeWonSets = 0;
        int awayWonSets = 0;
        for (int i=1; i <= match.getSetsNumber(); i++) {
            if (homePointsMap.get(i) > awayPointsMap.get(i))
                homeWonSets++;
            else if (homePointsMap.get(i) < awayPointsMap.get(i))
                awayWonSets++;
        }
        match.setHomeWonSets(homeWonSets);
        match.setAwayWonSets(awayWonSets);
    }

    @Override
    public void updateMatch(long matchId, ArrayList<SetRowItem> sets) {
        Match match = getById(matchId);
        // Delete all sets in match
        ManagerFactory.getInstance(context).participantStatManager.deleteByMatchId(matchId);

        if (sets.isEmpty()) {
            match.setSetsNumber(sets.size());
            update(match);
            return;
        }

        // Add new sets
        List<ParticipantStat> stats = new ArrayList<>();
        match.setSetsNumber(sets.size());
        match.setPlayed(true);
        update(match);

        int i=1;
        for (SetRowItem set : sets) {
            for (Participant participant : match.getParticipants()) {
                if (ParticipantType.home.toString().equals(participant.getRole()))
                    stats.add(new ParticipantStat(participant.getId(), i, set.getHomeScore()));
                else if (ParticipantType.away.toString().equals(participant.getRole()))
                    stats.add(new ParticipantStat(participant.getId(), i, set.getAwayScore()));
            }
            i++;
        }

        for (ParticipantStat stat : stats)
            ManagerFactory.getInstance(context).participantStatManager.insert(stat);
    }
}
