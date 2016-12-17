package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class StatsService extends AbstractIntentServiceWProgress {
    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_STATS = "extra_stats";
    public static final String EXTRA_STANDINGS = "extra_standings";
    public static final String EXTRA_HOME_STATS = "extra_home_statistics";
    public static final String EXTRA_AWAY_STATS = "extra_away_statistics";
    public static final String EXTRA_HOME_NAME = "extra_home_name";
    public static final String EXTRA_AWAY_NAME = "extra_away_name";
    public static final String EXTRA_HOME_PARTICIPANT = "extra_home_participant";
    public static final String EXTRA_AWAY_PARTICIPANT = "extra_away_participant";

    public static final String ACTION_GET_BY_COMP_ID = "get_by_comp_id";
    public static final String ACTION_GET_BY_TOUR_ID = "get_by_tour_id";
    public static final String ACTION_GET_STANDINGS_BY_TOURNAMENT = "get__standings_by_tour_id";
    public static final String ACTION_GET_MATCH_PLAYER_STATISTICS = "get_match_player_statistics";

    public StatsService() {
        super("Hockey Stats Service");
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, StatsService.class);
        res.putExtra(EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action)
        {
            case ACTION_GET_BY_COMP_ID:
            {
                Intent res = new Intent();
                long compID = intent.getLongExtra(EXTRA_ID, -1);
                res.setAction(ACTION_GET_BY_COMP_ID);
                List<AggregatedStatistics> stats = ((IStatisticManager)ManagerFactory.getInstance((this)).getEntityManager(AggregatedStatistics.class)).getByCompetitionId(compID);

                res.putParcelableArrayListExtra(EXTRA_STATS, new ArrayList<>(stats));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_BY_TOUR_ID:
            {
                Intent res = new Intent();
                long tourID = intent.getLongExtra(EXTRA_ID, -1);
                res.setAction(ACTION_GET_BY_TOUR_ID);
                List<AggregatedStatistics> stats = ((IStatisticManager)ManagerFactory.getInstance((this)).getEntityManager(AggregatedStatistics.class)).getByTournamentId(tourID);

                res.putParcelableArrayListExtra(EXTRA_STATS, new ArrayList<>(stats));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_STANDINGS_BY_TOURNAMENT:
            {
                Intent res = new Intent(action);
                long tourID = intent.getLongExtra(EXTRA_ID, -1);
                List<Standing> standings = ((IStatisticManager)ManagerFactory.getInstance((this)).getEntityManager(AggregatedStatistics.class)).getStandingsByTournamentId(tourID);

                res.putParcelableArrayListExtra(EXTRA_STANDINGS, new ArrayList<>(standings));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_MATCH_PLAYER_STATISTICS: {
                Intent res = new Intent(ACTION_GET_MATCH_PLAYER_STATISTICS);
                long matchId = intent.getLongExtra(EXTRA_ID, -1);
                Match match = ((IMatchManager)ManagerFactory.getInstance((this)).getEntityManager(Match.class)).getById(matchId);
                List<Participant> participants = ((IParticipantManager)ManagerFactory.getInstance((this)).getEntityManager(Participant.class)).getByMatchId(matchId);
                List<PlayerStat> homeStats = new ArrayList<>();
                List<PlayerStat> awayStats = new ArrayList<>();

                for (Participant participant : participants) {
                    if (ParticipantType.home.toString().equals(participant.getRole())) {
                        homeStats = ((IPlayerStatManager)ManagerFactory.getInstance((this)).getEntityManager(PlayerStat.class)).getByParticipantId(participant.getId());
                        res.putExtra(EXTRA_HOME_PARTICIPANT, participant);
                    }
                    else if (ParticipantType.away.toString().equals(participant.getRole())) {
                        awayStats = ((IPlayerStatManager)ManagerFactory.getInstance((this)).getEntityManager(PlayerStat.class)).getByParticipantId(participant.getId());
                        res.putExtra(EXTRA_AWAY_PARTICIPANT, participant);
                    }
                }

                res.putParcelableArrayListExtra(EXTRA_HOME_STATS, new ArrayList<>(homeStats));
                res.putParcelableArrayListExtra(EXTRA_AWAY_STATS, new ArrayList<>(awayStats));

                res.putExtra(EXTRA_HOME_NAME, match.getHomeName());
                res.putExtra(EXTRA_AWAY_NAME, match.getAwayName());

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
        }
    }
}
