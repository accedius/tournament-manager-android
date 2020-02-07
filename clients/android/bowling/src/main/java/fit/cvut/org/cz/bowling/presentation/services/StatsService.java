package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.bowling.business.entities.Standing;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Bowling stats service to handle intent/service/activity work in statistics scope
 */
public class StatsService extends AbstractIntentServiceWProgress {
    public static final String ACTION_GET_BY_COMP_ID = "get_by_comp_id";
    public static final String ACTION_GET_BY_TOUR_ID = "get_by_tour_id";
    public static final String ACTION_GET_STANDINGS_BY_TOURNAMENT = "get__standings_by_tour_id";
    public static final String ACTION_GET_MATCH_PLAYER_STATISTICS = "get_match_player_statistics";

    public StatsService() {
        super("Bowling Stats Service");
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, StatsService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action)
        {
            case ACTION_GET_BY_COMP_ID:
            {
                Intent res = new Intent();
                long compID = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                res.setAction(ACTION_GET_BY_COMP_ID);
                final IStatisticManager statisticManager = ManagerFactory.getInstance(this).getEntityManager(AggregatedStatistics.class);
                List<AggregatedStatistics> stats = statisticManager.getByCompetitionId(compID);

                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_STATS, new ArrayList<>(stats));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_BY_TOUR_ID:
            {
                Intent res = new Intent();
                long tourID = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                res.setAction(ACTION_GET_BY_TOUR_ID);
                final IStatisticManager statisticManager = ManagerFactory.getInstance(this).getEntityManager(AggregatedStatistics.class);
                List<AggregatedStatistics> stats = statisticManager.getByTournamentId(tourID);

                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_STATS, new ArrayList<>(stats));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_STANDINGS_BY_TOURNAMENT:
            {
                Intent res = new Intent(action);
                long tourID = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                final IStatisticManager statisticManager = ManagerFactory.getInstance(this).getEntityManager(AggregatedStatistics.class);
                List<Standing> standings = statisticManager.getStandingsByTournamentId(tourID);

                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_STANDINGS, new ArrayList<>(standings));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_MATCH_PLAYER_STATISTICS: {
                Intent res = new Intent(ACTION_GET_MATCH_PLAYER_STATISTICS);
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                final IMatchManager matchManager = ManagerFactory.getInstance(this).getEntityManager(Match.class);

                Match match = matchManager.getById(matchId);
                Tournament tournament = ManagerFactory.getInstance(this).getEntityManager(Tournament.class).getById(match.getTournamentId());
                Competition competition = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(tournament.getCompetitionId());

                final IParticipantManager participantManager = ManagerFactory.getInstance(this).getEntityManager(Participant.class);
                List<Participant> participants = participantManager.getByMatchId(matchId);
                List<PlayerStat> playerStats;

                for (int i = 0; i < participants.size(); ++i){
                    final IPlayerStatManager playerStatManager = ManagerFactory.getInstance(this).getEntityManager(PlayerStat.class);
                    playerStats = playerStatManager.getByParticipantId(participants.get(i).getId());

                    res.putParcelableArrayListExtra(ExtraConstants.EXTRA_STATS+i, new ArrayList<>(playerStats));
                }
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS, new ArrayList<Parcelable>(participants));

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
        }
    }
}

