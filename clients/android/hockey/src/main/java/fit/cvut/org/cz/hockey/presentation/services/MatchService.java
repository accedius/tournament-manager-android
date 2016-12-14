package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 10. 4. 2016.
 */
public class MatchService extends AbstractIntentServiceWProgress {
    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_MATCH = "extra_match";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_MATCH_SCORE = "extra_match_score";
    public static final String EXTRA_TOUR_ID = "extra_tour_id";
    public static final String EXTRA_PART_LIST = "extra_participants_list";
    public static final String EXTRA_MATCH_LIST = "extra_match_list";
    public static final String EXTRA_HOME_STATS = "extra_home_statistics";
    public static final String EXTRA_AWAY_STATS = "extra_away_statistics";

    public static final String ACTION_FIND_BY_ID = "action_find_match_by_id";
    public static final String ACTION_FIND_BY_TOURNAMENT_ID = "action_find_match_by_tournament_id";
    public static final String ACTION_CREATE = "action_create_match";
    public static final String ACTION_DELETE = "action_delete_match";
    public static final String ACTION_RESET = "action_reset_match";
    public static final String ACTION_UPDATE = "action_update_match";
    public static final String ACTION_GENERATE_ROUND = "action_generate_round";
    public static final String ACTION_FIND_BY_ID_FOR_OVERVIEW = "action_find_match_for_match_overview";
    public static final String ACTION_UPDATE_FOR_OVERVIEW = "action_update_match_for_overview";

    public MatchService() {
        super("Hockey Match Service");
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, MatchService.class);
        res.putExtra(EXTRA_ACTION, action);
        return res;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);
        if (action == null)
            action = intent.getAction();

        switch (action) {
            case ACTION_CREATE: {
                fit.cvut.org.cz.tmlibrary.data.entities.Match m = intent.getParcelableExtra(EXTRA_MATCH);
                Match hockeyMatch = new Match(m);
                ManagerFactory.getInstance(this).matchManager.insert(hockeyMatch);
                for (Participant participant : hockeyMatch.getParticipants()) {
                    Team team = ManagerFactory.getInstance(this).teamManager.getById(participant.getParticipantId());
                    List<Player> teamPlayers = ManagerFactory.getInstance(this).teamManager.getTeamPlayers(team);
                    for (Player player : teamPlayers) {
                        ManagerFactory.getInstance(this).playerStatManager.insert(new PlayerStat(participant.getId(), player.getId()));
                    }
                }
                break;
            }
            case ACTION_UPDATE_FOR_OVERVIEW: {
                Intent res = new Intent(action);
                Match match = intent.getParcelableExtra(EXTRA_MATCH_SCORE);
                Match original = ManagerFactory.getInstance(this).matchManager.getById(match.getId());
                if (!original.isPlayed()) {
                    ManagerFactory.getInstance(this).matchManager.beginMatch(match.getId());
                }
                match.setTournamentId(original.getTournamentId());
                match.setDate(original.getDate());
                match.setNote(original.getNote());
                match.setPeriod(original.getPeriod());
                match.setRound(original.getRound());
                ManagerFactory.getInstance(this).matchManager.update(match);
                List<Participant> participants = ManagerFactory.getInstance(this).participantManager.getByMatchId(match.getId());
                for (Participant participant : participants) {
                    int score = ParticipantType.home.toString().equals(participant.getRole()) ? match.getHomeScore() : match.getAwayScore();
                    List<ParticipantStat> stats = ManagerFactory.getInstance(this).participantStatManager.getByParticipantId(participant.getId());
                    ParticipantStat stat;
                    if (stats.isEmpty()) {
                        stat = new ParticipantStat(participant.getId(), score);
                        ManagerFactory.getInstance(this).participantStatManager.insert(stat);
                    } else {
                        stat = stats.get(0);
                        stat.setScore(score);
                        ManagerFactory.getInstance(this).participantStatManager.update(stat);
                    }

                    // Remove original stats and add new ones (way to handle removed items)
                    ManagerFactory.getInstance(this).playerStatManager.deleteByParticipantId(participant.getId());
                }

                ArrayList<PlayerStat> homeStats = intent.getParcelableArrayListExtra(EXTRA_HOME_STATS);
                ArrayList<PlayerStat> awayStats = intent.getParcelableArrayListExtra(EXTRA_AWAY_STATS);

                for (PlayerStat playerStat : homeStats)
                    ManagerFactory.getInstance(this).playerStatManager.insert(playerStat);

                for (PlayerStat playerStat : awayStats)
                    ManagerFactory.getInstance(this).playerStatManager.insert(playerStat);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);
                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);
                List<Team> tourTeams = ManagerFactory.getInstance(this).teamManager.getByTournamentId(tourId);
                if (matchId != -1) {
                    Match m = ManagerFactory.getInstance(this).matchManager.getById(matchId);
                    res.putExtra(EXTRA_MATCH, m);
                }

                ArrayList<Participant> participants = new ArrayList<>();
                for (Team team : tourTeams) {
                    Participant participant = new Participant(matchId, team.getId(), null);
                    participant.setName(team.getName());
                    participants.add(participant);
                    //participants.add(new Participant(-1, t.getId(), null));
                }

                res.putParcelableArrayListExtra(EXTRA_PART_LIST, participants);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_TOURNAMENT_ID: {
                Intent res = new Intent(action);

                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);
                ArrayList<Match> matches = new ArrayList<>(ManagerFactory.getInstance(this).matchManager.getByTournamentId(tourId));
                for (Match m : matches) {
                    /* TODO create method for this */
                    List<Participant> participants = ManagerFactory.getInstance(this).participantManager.getByMatchId(m.getId());
                    for (Participant p : participants) {
                        m.addParticipant(p);
                        int score = ManagerFactory.getInstance(this).participantStatManager.getScoreByParticipantId(p.getId());
                        Team t = ManagerFactory.getInstance(this).teamManager.getById(p.getParticipantId());
                        if (ParticipantType.home.toString().equals(p.getRole())) {
                            m.setHomeName(t.getName());
                            m.setHomeScore(score);
                        }
                        else if (ParticipantType.away.toString().equals(p.getRole())) {
                            m.setAwayName(t.getName());
                            m.setAwayScore(score);
                        }
                    }
                }
                res.putParcelableArrayListExtra(EXTRA_MATCH_LIST, matches);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_GENERATE_ROUND: {
                Intent res = new Intent(action);
                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);

                ManagerFactory.getInstance(this).matchManager.generateRound(tourId);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID_FOR_OVERVIEW: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);

                Match match = ManagerFactory.getInstance(this).matchManager.getById(matchId);
                /* TODO create method for this */
                List<Participant> participants = ManagerFactory.getInstance(this).participantManager.getByMatchId(match.getId());
                for (Participant participant : participants) {
                    match.addParticipant(participant);
                    int score = ManagerFactory.getInstance(this).participantStatManager.getScoreByParticipantId(participant.getId());
                    Team team = ManagerFactory.getInstance(this).teamManager.getById(participant.getParticipantId());
                    if (ParticipantType.home.toString().equals(participant.getRole())) {
                        match.setHomeName(team.getName());
                        match.setHomeScore(score);
                    }
                    else if (ParticipantType.away.toString().equals(participant.getRole())) {
                        match.setAwayName(team.getName());
                        match.setAwayScore(score);
                    }
                }
                res.putExtra(EXTRA_MATCH, match);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_DELETE: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);

                ManagerFactory.getInstance(this).matchManager.delete(matchId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_RESET: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);

                ManagerFactory.getInstance(this).matchManager.resetMatch(matchId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_UPDATE: {
                Intent res = new Intent(action);
                Match match = intent.getParcelableExtra(EXTRA_MATCH);
                Match originalMatch = ManagerFactory.getInstance(this).matchManager.getById(match.getId());
                match.setPlayed(originalMatch.isPlayed());

                ManagerFactory.getInstance(this).matchManager.update(match);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}
