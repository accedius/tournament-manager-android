package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 4. 4. 2016.
 */
public class TournamentService extends AbstractIntentServiceWProgress {
    public static final String ACTION_CREATE = "fit.cvut.org.cz.hockey.presentation.services.tournament_create";
    public static final String ACTION_FIND_BY_ID = "fit.cvut.org.cz.hockey.presentation.services.tournament_find_by_id";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.hockey.presentation.services.tournament_update";
    public static final String ACTION_GET_ALL = "fit.cvut.org.cz.hockey.presentation.services.tournament_all";
    public static final String ACTION_GET_CONFIG_BY_ID = "fit.cvut.org.cz.hockey.presentation.services.tournament_get_configuration_by_tournament_id";
    public static final String ACTION_SET_CONFIG = "fit.cvut.org.cz.hockey.presentation.services.tournament_set_configuration";
    public static final String ACTION_DELETE = "fit.cvut.org.cz.hockey.presentation.services.tournament_delete";
    public static final String ACTION_GENERATE_ROSTERS = "fit.cvut.org.cz.hockey.presentation.services.generate_rosters";

    public static final int GENERATING_TYPES_CNT = 4;
    public static final int GENERATE_BY_TEAM_POINTS = 0;
    public static final int GENERATE_BY_WINS = 1;
    public static final int GENERATE_BY_GOALS = 2;
    public static final int GENERATE_RANDOMLY = 3;

    public TournamentService() {
        super("Hockey Tournament Service");
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, TournamentService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);
        switch (action) {
            case ACTION_CREATE: {
                Tournament t = intent.getParcelableExtra(ExtraConstants.EXTRA_TOURNAMENT);
                ManagerFactory.getInstance(this).getEntityManager(Tournament.class).insert(t);
                break;
            }
            case ACTION_UPDATE: {
                Tournament c = intent.getParcelableExtra(ExtraConstants.EXTRA_TOURNAMENT);
                ManagerFactory.getInstance(this).getEntityManager(Tournament.class).update(c);
                break;
            }
            case ACTION_DELETE: {
                Intent res = new Intent(ACTION_DELETE);
                long tourId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                boolean result = ManagerFactory.getInstance(this).getEntityManager(Tournament.class).delete(tourId);
                res.putExtra(ExtraConstants.EXTRA_RESULT, result);
                res.putExtra(ExtraConstants.EXTRA_POSITION, intent.getIntExtra(ExtraConstants.EXTRA_POSITION, -1));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID: {
                Intent res = new Intent();
                res.setAction(ACTION_FIND_BY_ID);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);

                Tournament tournament = ManagerFactory.getInstance(this).getEntityManager(Tournament.class).getById(id);
                res.putExtra(ExtraConstants.EXTRA_TOURNAMENT, tournament);

                List<Team> teams = ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).getByTournamentId(id);
                List<Player> players = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getTournamentPlayers(id);
                List<Match> matches = ((IMatchManager)ManagerFactory.getInstance(this).getEntityManager(Match.class)).getByTournamentId(id);

                res.putExtra(ExtraConstants.EXTRA_MATCHES_COUNT, matches.size());
                res.putExtra(ExtraConstants.EXTRA_TEAMS_COUNT, teams.size());
                res.putExtra(ExtraConstants.EXTRA_PLAYERS_COUNT, players.size());
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_GET_ALL: {
                Intent res = new Intent();
                res.setAction(ACTION_GET_ALL);
                long competitionId = intent.getLongExtra(ExtraConstants.EXTRA_COMP_ID, -1);
                List<Tournament> tournaments = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getByCompetitionId(competitionId);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_TOURNAMENTS, new ArrayList<>(tournaments));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_GET_CONFIG_BY_ID: {
                Intent res = new Intent();
                res.setAction(ACTION_GET_CONFIG_BY_ID);
                res.putExtra(ExtraConstants.EXTRA_CONFIGURATION, ManagerFactory.getInstance(this).getEntityManager(PointConfiguration.class).getById(intent.getLongExtra(ExtraConstants.EXTRA_ID, -1)));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_SET_CONFIG: {
                PointConfiguration pc = intent.getParcelableExtra(ExtraConstants.EXTRA_CONFIGURATION);
                ManagerFactory.getInstance(this).getEntityManager(PointConfiguration.class).update(pc);
                break;
            }
            case ACTION_GENERATE_ROSTERS: {
                Intent result = new Intent(action);
                boolean res = ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).generateRosters(
                        intent.getLongExtra(ExtraConstants.EXTRA_ID, -1),
                        intent.getLongExtra(ExtraConstants.EXTRA_TOUR_ID, -1),
                        intent.getIntExtra(ExtraConstants.EXTRA_GENERATING_TYPE, -1));

                result.putExtra(ExtraConstants.EXTRA_RESULT, res);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }
}
