package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
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
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
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
        super("Hockey Stats Service");
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
                List<PlayerStat> homeStats = new ArrayList<>();
                List<PlayerStat> awayStats = new ArrayList<>();

                long homeId = 0, awayId = 0;
                for (Participant participant : participants) {
                    final IPlayerStatManager playerStatManager = ManagerFactory.getInstance(this).getEntityManager(PlayerStat.class);
                    if (ParticipantType.home.toString().equals(participant.getRole())) {
                        homeStats = playerStatManager.getByParticipantId(participant.getId());
                        res.putExtra(ExtraConstants.EXTRA_HOME_PARTICIPANT, participant);
                        homeId = participant.getParticipantId();
                    }
                    else if (ParticipantType.away.toString().equals(participant.getRole())) {
                        awayStats = playerStatManager.getByParticipantId(participant.getId());
                        res.putExtra(ExtraConstants.EXTRA_AWAY_PARTICIPANT, participant);
                        awayId = participant.getParticipantId();
                    }
                }
                String homeName, awayName;
                if (CompetitionTypes.teams().equals(competition.getType())) {
                    final ITeamManager teamManager = ManagerFactory.getInstance(this).getEntityManager(Team.class);
                    Team team = teamManager.getById(homeId);
                    homeName = team.getName();
                    team = teamManager.getById(awayId);
                    awayName = team.getName();
                } else {
                    final IManager<Player> playerManager = ManagerFactory.getInstance(this).getEntityManager(Player.class);
                    Player player = playerManager.getById(homeId);
                    homeName = player.getName();
                    player = playerManager.getById(awayId);
                    awayName = player.getName();
                }

                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_HOME_STATS, new ArrayList<>(homeStats));
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_AWAY_STATS, new ArrayList<>(awayStats));

                res.putExtra(ExtraConstants.EXTRA_HOME_NAME, homeName);
                res.putExtra(ExtraConstants.EXTRA_AWAY_NAME, awayName);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
        }
    }
}

