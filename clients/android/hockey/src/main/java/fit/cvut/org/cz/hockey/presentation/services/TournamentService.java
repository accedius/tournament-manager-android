package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 4. 4. 2016.
 */
public class TournamentService extends AbstractIntentServiceWProgress {
    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_TOURNAMENT = "extra_tournament";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_COMP_ID = "extra_comp_id";
    public static final String EXTRA_LIST = "extra_list";
    public static final String EXTRA_PLAYER_SUM = "extra_number_of_players";
    public static final String EXTRA_MATCHES_SUM = "extra_number_of_matches";
    public static final String EXTRA_TEAMS_SUM = "extra_number_of_teams";
    public static final String EXTRA_CONFIGURATION = "extra_configuration";
    public static final String EXTRA_RESULT = "extra_result";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_GENERATING_TYPE = "extra_generating_type";

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
        switch (action) {
            case ACTION_CREATE: {
                Tournament t = intent.getParcelableExtra(EXTRA_TOURNAMENT);
                ManagerFactory.getInstance(this).tournamentManager.insert(this, t);
                break;
            }
            case ACTION_UPDATE: {
                Tournament c = intent.getParcelableExtra(EXTRA_TOURNAMENT);
                ManagerFactory.getInstance(this).tournamentManager.update(this, c);
                break;
            }
            case ACTION_DELETE: {
                Intent res = new Intent(ACTION_DELETE);
                long tourId = intent.getLongExtra(EXTRA_ID, -1);
                if (ManagerFactory.getInstance(this).tournamentManager.delete(this, tourId)) {
                    res.putExtra(EXTRA_RESULT, 0);
                    res.putExtra(EXTRA_POSITION, intent.getIntExtra(EXTRA_POSITION, -1));
                }
                else
                    res.putExtra(EXTRA_RESULT, 1);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID: {
                Intent res = new Intent();
                res.setAction(ACTION_FIND_BY_ID);
                long id = intent.getLongExtra(EXTRA_ID, -1);

                Tournament c = ManagerFactory.getInstance(this).tournamentManager.getById(this, id);
                res.putExtra(EXTRA_TOURNAMENT, c);

                List<Team> teams = ManagerFactory.getInstance(this).teamManager.getByTournamentId(this, id);
                List<Player> players = ManagerFactory.getInstance(this).tournamentManager.getTournamentPlayers(this, id);
                List<Match> matches = ManagerFactory.getInstance(this).matchManager.getByTournamentId(this, id);

                res.putExtra(EXTRA_MATCHES_SUM, matches.size());
                res.putExtra(EXTRA_TEAMS_SUM, teams.size());
                res.putExtra(EXTRA_PLAYER_SUM, players.size());
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_GET_ALL: {
                Intent res = new Intent();
                res.setAction(ACTION_GET_ALL);
                long competitionId = intent.getLongExtra(EXTRA_COMP_ID, -1);
                List<Tournament> tournaments = ManagerFactory.getInstance(this).tournamentManager.getByCompetitionId(this, competitionId);
                res.putParcelableArrayListExtra(EXTRA_LIST, new ArrayList<>(tournaments));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_GET_CONFIG_BY_ID: {
                Intent res = new Intent();
                res.setAction(ACTION_GET_CONFIG_BY_ID);
                res.putExtra(EXTRA_CONFIGURATION, ManagerFactory.getInstance(this).pointConfigManager.getById(this, intent.getLongExtra(EXTRA_ID, -1)));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_SET_CONFIG: {
                PointConfiguration pc = intent.getParcelableExtra(EXTRA_CONFIGURATION);
                ManagerFactory.getInstance(this).pointConfigManager.update(this, pc);
                break;
            }
            case ACTION_GENERATE_ROSTERS: {
                Intent result = new Intent(action);
                boolean res = ManagerFactory.getInstance(this).teamManager.generateRosters(
                        this,
                        intent.getLongExtra(EXTRA_ID, -1),
                        intent.getLongExtra(EXTRA_TOURNAMENT, -1),
                        intent.getIntExtra(EXTRA_GENERATING_TYPE, -1));

                result.putExtra(EXTRA_RESULT, res);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }
}
