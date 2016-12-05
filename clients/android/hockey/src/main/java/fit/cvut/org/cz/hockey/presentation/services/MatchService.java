package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
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
                fit.cvut.org.cz.tmlibrary.business.entities.Match m = intent.getParcelableExtra(EXTRA_MATCH);
                Match hockeyMatch = new Match(m);
                ManagerFactory.getInstance(this).matchManager.insert(this, hockeyMatch);
                for (Participant participant : hockeyMatch.getParticipants()) {
                    Team team = ManagerFactory.getInstance(this).teamManager.getById(this, participant.getParticipantId());
                    List<Player> teamPlayers = ManagerFactory.getInstance(this).teamManager.getTeamPlayers(this, team);
                    for (Player player : teamPlayers) {
                        ManagerFactory.getInstance(this).playerStatManager.insert(this, new PlayerStat(participant.getId(), player.getId()));
                    }
                }
                break;
            }
            case ACTION_UPDATE_FOR_OVERVIEW: {
                Intent res = new Intent(ACTION_UPDATE_FOR_OVERVIEW);
                Match match = intent.getParcelableExtra(EXTRA_MATCH_SCORE);
                if (!ManagerFactory.getInstance(this).matchManager.getById(this, match.getId()).isPlayed()) {
                    ManagerFactory.getInstance(this).matchManager.beginMatch(this, match.getId());
                }
                ManagerFactory.getInstance(this).matchManager.update(this, match);
                List<Participant> participants = ManagerFactory.getInstance(this).participantManager.getByMatchId(this, match.getId());
                for (Participant participant : participants) {
                    int score = ParticipantType.home.toString().equals(participant.getRole()) ? match.getHomeScore() : match.getAwayScore();
                    List<ParticipantStat> stats = ManagerFactory.getInstance(this).participantStatManager.getByParticipantId(this, participant.getId());
                    ParticipantStat stat;
                    if (stats.isEmpty()) {
                        stat = new ParticipantStat(participant.getId(), score);
                        ManagerFactory.getInstance(this).participantStatManager.insert(this, stat);
                    } else {
                        stat = stats.get(0);
                        stat.setScore(score);
                        ManagerFactory.getInstance(this).participantStatManager.update(this, stat);
                    }

                    // Remove original stats and add new ones (way to handle removed items)
                    ManagerFactory.getInstance(this).playerStatManager.deleteByParticipantId(this, participant.getId());
                }


                ArrayList<PlayerStat> homeStats = intent.getParcelableArrayListExtra(EXTRA_HOME_STATS);
                ArrayList<PlayerStat> awayStats = intent.getParcelableArrayListExtra(EXTRA_AWAY_STATS);

                for (PlayerStat playerStat : homeStats)
                    ManagerFactory.getInstance(this).playerStatManager.insert(this, playerStat);

                for (PlayerStat playerStat : awayStats)
                    ManagerFactory.getInstance(this).playerStatManager.insert(this, playerStat);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID: {
                Intent res = new Intent();
                res.setAction(ACTION_FIND_BY_ID);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);
                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);
                List<Team> tourTeams = ManagerFactory.getInstance(this).teamManager.getByTournamentId(this, tourId);
                if (matchId != -1) {
                    Match m = ManagerFactory.getInstance(this).matchManager.getById(this, matchId);
                    res.putExtra(EXTRA_MATCH, m);
                }

                ArrayList<Team> participants = new ArrayList<>();
                for (Team t : tourTeams) {
                    participants.add(t);
                    //participants.add(new Participant(-1, t.getId(), null));
                }

                res.putParcelableArrayListExtra(EXTRA_PART_LIST, participants);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_TOURNAMENT_ID: {
                Intent res = new Intent(ACTION_FIND_BY_TOURNAMENT_ID);

                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);
                ArrayList<Match> matches = new ArrayList<>(ManagerFactory.getInstance(this).matchManager.getByTournamentId(this, tourId));
                for (Match m : matches) {
                    /* TODO create method for this */
                    List<Participant> participants = ManagerFactory.getInstance(this).participantManager.getByMatchId(this, m.getId());
                    for (Participant p : participants) {
                        m.addParticipant(p);
                        int score = ManagerFactory.getInstance(this).participantStatManager.getScoreByParticipantId(this, p.getId());
                        Team t = ManagerFactory.getInstance(this).teamManager.getById(this, p.getParticipantId());
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
                Intent res = new Intent(ACTION_GENERATE_ROUND);
                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);

                ManagerFactory.getInstance(this).matchManager.generateRound(this, tourId);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID_FOR_OVERVIEW: {
                Intent res = new Intent();
                res.setAction(ACTION_FIND_BY_ID_FOR_OVERVIEW);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);

                Match match = ManagerFactory.getInstance(this).matchManager.getById(this, matchId);
                /* TODO create method for this */
                List<Participant> participants = ManagerFactory.getInstance(this).participantManager.getByMatchId(this, match.getId());
                for (Participant participant : participants) {
                    match.addParticipant(participant);
                    int score = ManagerFactory.getInstance(this).participantStatManager.getScoreByParticipantId(this, participant.getId());
                    Team team = ManagerFactory.getInstance(this).teamManager.getById(this, participant.getParticipantId());
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

                ManagerFactory.getInstance(this).matchManager.delete(this, matchId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_RESET: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);

                ManagerFactory.getInstance(this).matchManager.resetMatch(this, matchId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_UPDATE: {
                Intent res = new Intent(action);
                Match match = intent.getParcelableExtra(EXTRA_MATCH);
                Match originalMatch = ManagerFactory.getInstance(this).matchManager.getById(this, match.getId());
                match.setPlayed(originalMatch.isPlayed());

                ManagerFactory.getInstance(this).matchManager.update(this, match);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}
