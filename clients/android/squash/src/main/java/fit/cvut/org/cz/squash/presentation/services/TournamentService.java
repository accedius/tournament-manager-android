package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to Tournaments
 * Created by Vaclav on 28. 3. 2016.
 */
public class TournamentService extends AbstractIntentServiceWProgress{

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TOURNAMENT = "extra_tournament";
    public static final String EXTRA_PLAYER_COUNT = "extra_player_count";
    public static final String EXTRA_MATCH_COUNT = "extra_match_count";
    public static final String EXTRA_TEAM_COUNT = "extra_team_count";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_RESULT = "extra_result";
    public static final String EXTRA_GENERATING_TYPE = "extra_generating_type";

    public static final String ACTION_CREATE = "fit.cvut.org.cz.squash.presentation.services.new_tournament";
    public static final String ACTION_DELETE = "fit.cvut.org.cz.squash.presentation.services.delete_tournament";
    public static final String ACTION_GET_BY_ID = "fit.cvut.org.cz.squash.presentation.services.get_tournament_by_id";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.squash.presentation.services.update_tournament";
    public static final String ACTION_GET_BY_COMPETITION_ID = "fit.cvut.org.cz.squash.presentation.services.get_tournaments_by_competition_id";
    public static final String ACTION_GET_OVERVIEW = "fit.cvut.org.cz.squash.presentation.services.get_tournament_overview";
    public static final String ACTION_GENERATE_ROSTERS = "fit.cvut.org.cz.squash.presentation.services.generate_rosters";

    public static final int GENERATING_TYPES_CNT = 4;
    public static final int GENERATE_BY_TEAM_POINTS = 0;
    public static final int GENERATE_BY_SETS = 1;
    public static final int GENERATE_BY_BALLS = 2;
    public static final int GENERATE_RANDOMLY = 3;

    public TournamentService() {
        super("Squash Tournament Service");
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, TournamentService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action){
            case ACTION_CREATE:{
                Tournament t = intent.getParcelableExtra(EXTRA_TOURNAMENT);
                ManagersFactory.getInstance().tournamentManager.insert(this, t);
                break;
            }
            case ACTION_UPDATE:{
                Tournament t = intent.getParcelableExtra(EXTRA_TOURNAMENT);
                ManagersFactory.getInstance().tournamentManager.update(this, t);
                break;
            }
            case ACTION_DELETE:{
                Intent result = new Intent(action);
                int position = intent.getIntExtra(EXTRA_POSITION, -1);
                result.putExtra(EXTRA_POSITION, position);
                result.putExtra(EXTRA_RESULT, ManagersFactory.getInstance().tournamentManager.delete(this, intent.getLongExtra(EXTRA_ID, -1)));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_BY_ID:{
                Intent result = new Intent();
                result.setAction(ACTION_GET_BY_ID);
                Tournament t = ManagersFactory.getInstance().tournamentManager.getById(this, intent.getLongExtra(EXTRA_ID, -1));
                result.putExtra(EXTRA_TOURNAMENT, t);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_BY_COMPETITION_ID:{
                Intent result = new Intent();
                result.setAction(ACTION_GET_BY_COMPETITION_ID);
                long id = intent.getLongExtra(EXTRA_ID, -1);
                result.putParcelableArrayListExtra(EXTRA_TOURNAMENT, ManagersFactory.getInstance().tournamentManager.getByCompetitionId(this, id));
                Competition c = ManagersFactory.getInstance().competitionManager.getById(this, id);
                result.putExtra(EXTRA_TYPE, c.getType().id);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_OVERVIEW:{
                Intent result = new Intent(action);
                long id = intent.getLongExtra(EXTRA_ID, -1);
                CompetitionType type = CompetitionTypes.competitionTypes()[intent.getIntExtra(EXTRA_TYPE, 0)];
                Tournament t = ManagersFactory.getInstance().tournamentManager.getById(this, id);
                ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(this, id);
                ArrayList<ScoredMatch> matches = ManagersFactory.getInstance().matchManager.getByTournamentId(this, id);

                if (type.equals(CompetitionTypes.teams()))
                    result.putExtra(EXTRA_TEAM_COUNT, ManagersFactory.getInstance().teamsManager.getByTournamentId(this, id).size());
                else
                    result.putExtra(EXTRA_TEAM_COUNT, 0);

                result.putExtra(EXTRA_TOURNAMENT, t);
                result.putExtra(EXTRA_PLAYER_COUNT, players.size());
                result.putExtra(EXTRA_MATCH_COUNT, matches.size());
                result.putExtra(EXTRA_TYPE, type.id);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GENERATE_ROSTERS: {
                Intent result = new Intent(action);
                boolean res = ManagersFactory.getInstance().teamsManager.generateRosters(
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
