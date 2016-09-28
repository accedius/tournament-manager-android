package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.HockeyScoredMatch;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.tmlibrary.business.entities.MatchParticipant;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
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
    public static final String ACTION_RESTART = "action_restart_match";
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
                ScoredMatch m = intent.getParcelableExtra(EXTRA_MATCH);
                ManagerFactory.getInstance().matchManager.insert(this, m);
                break;
            }
            case ACTION_UPDATE_FOR_OVERVIEW: {
                Intent res = new Intent(ACTION_UPDATE_FOR_OVERVIEW);
                MatchScore m = intent.getParcelableExtra(EXTRA_MATCH_SCORE);
                if (!ManagerFactory.getInstance().matchManager.getById(this, m.getMatchId()).isPlayed()) {
                    ManagerFactory.getInstance().matchManager.beginMatch(this, m.getMatchId());
                }
                ArrayList<MatchPlayerStatistic> homeStats = intent.getParcelableArrayListExtra(EXTRA_HOME_STATS);
                ArrayList<MatchPlayerStatistic> awayStats = intent.getParcelableArrayListExtra(EXTRA_AWAY_STATS);

                // Update participants lists
                ArrayList<Long> homePlayerIds = new ArrayList<>();
                for (MatchPlayerStatistic stat : homeStats) homePlayerIds.add(stat.getPlayerId());

                ArrayList<Long> awayPlayerIds = new ArrayList<>();
                for (MatchPlayerStatistic stat : awayStats) awayPlayerIds.add(stat.getPlayerId());

                ManagerFactory.getInstance().statisticsManager.updatePlayersInMatch(this, m.getMatchId(), ParticipantType.home, homePlayerIds);
                ManagerFactory.getInstance().statisticsManager.updatePlayersInMatch(this, m.getMatchId(), ParticipantType.away, awayPlayerIds);
                //-----------------
                for (MatchPlayerStatistic statistic : homeStats) ManagerFactory.getInstance().statisticsManager.updatePlayerStatsInMatch(this, statistic, m.getMatchId());
                for (MatchPlayerStatistic statistic : awayStats) ManagerFactory.getInstance().statisticsManager.updatePlayerStatsInMatch(this, statistic, m.getMatchId());

                ManagerFactory.getInstance().statisticsManager.setMatchScoreByMatchId(this, m.getMatchId(), m);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID: {
                Intent res = new Intent();
                res.setAction(ACTION_FIND_BY_ID);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);
                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);
                ArrayList<Team> tourTeams;

                tourTeams = ManagerFactory.getInstance().teamManager.getByTournamentId(this, tourId);
                if (matchId != -1) {
                    ScoredMatch m = ManagerFactory.getInstance().matchManager.getById(this, matchId);
                    res.putExtra(EXTRA_MATCH, m);
                }

                ArrayList<MatchParticipant> participants = new ArrayList<>();
                for (Team t : tourTeams) {
                    participants.add(new MatchParticipant(t.getId(), t.getName()));
                }

                res.putParcelableArrayListExtra(EXTRA_PART_LIST, participants);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_TOURNAMENT_ID: {
                Intent res = new Intent(ACTION_FIND_BY_TOURNAMENT_ID);

                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);
                ArrayList<HockeyScoredMatch> hockeyScoredMatches = new ArrayList<>();
                ArrayList<ScoredMatch> matches = ManagerFactory.getInstance().matchManager.getByTournamentId(this, tourId);
                for (ScoredMatch scoredMatch : matches) {
                    hockeyScoredMatches.add(
                        new HockeyScoredMatch(
                            scoredMatch,
                            ManagerFactory.getInstance().statisticsManager.getMatchScoreByMatchId(this, scoredMatch.getId())
                        )
                    );
                }
                res.putParcelableArrayListExtra(EXTRA_MATCH_LIST, hockeyScoredMatches);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_GENERATE_ROUND: {
                Intent res = new Intent(ACTION_GENERATE_ROUND);
                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);

                ManagerFactory.getInstance().matchManager.generateRound(this, tourId);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID_FOR_OVERVIEW: {
                Intent res = new Intent();
                res.setAction(ACTION_FIND_BY_ID_FOR_OVERVIEW);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);

                ScoredMatch m = ManagerFactory.getInstance().matchManager.getById(this, matchId);
                res.putExtra(EXTRA_MATCH, m);

                MatchScore score = ManagerFactory.getInstance().statisticsManager.getMatchScoreByMatchId(this, matchId);
                if (score != null)
                    res.putExtra(EXTRA_MATCH_SCORE, score);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_DELETE: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);

                ManagerFactory.getInstance().matchManager.delete(this, matchId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_RESTART: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);

                ManagerFactory.getInstance().matchManager.resetMatch(this, matchId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_UPDATE: {
                Intent res = new Intent(action);
                ScoredMatch match = intent.getParcelableExtra(EXTRA_MATCH);
                ScoredMatch originalMatch = ManagerFactory.getInstance().matchManager.getById(this, match.getId());
                match.setPlayed(originalMatch.isPlayed());

                ManagerFactory.getInstance().matchManager.update(this, match);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}
