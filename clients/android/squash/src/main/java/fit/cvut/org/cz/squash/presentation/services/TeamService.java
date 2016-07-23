package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to Teams
 * Created by Vaclav on 3. 4. 2016.
 */
public class TeamService extends AbstractIntentServiceWProgress {

    public TeamService() {
        super("Squash Team Service");
    }

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TEAMS = "extra_teams";
    public static final String EXTRA_TEAM = "extra_team";
    public static final String EXTRA_RESULT = "extra_result";
    public static final String EXTRA_POSITION = "extra_position";

    public static final String ACTION_GET_TEAMS_BY_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.get_teams_by_tournament";
    public static final String ACTION_ADD_TEAM = "fit.cvut.org.cz.squash.presentation.services.add_team";
    public static final String ACTION_EDIT_TEAM = "fit.cvut.org.cz.squash.presentation.services.edit_team";
    public static final String ACTION_GET_BY_ID = "fit.cvut.org.cz.squash.presentation.services.get_team_by_id";
    public static final String ACTION_DELETE = "fit.cvut.org.cz.squash.presentation.services.delete_team";

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action){
            case ACTION_GET_TEAMS_BY_TOURNAMENT:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                sendTeamsByTournament(id);
                break;

            }
            case ACTION_ADD_TEAM:{
                Team t = intent.getParcelableExtra(EXTRA_TEAM);
                ManagersFactory.getInstance().teamsManager.insert(this, t);
                sendTeamsByTournament(t.getTournamentId());
                break;
            }
            case ACTION_GET_BY_ID:{
                Intent result = new Intent(action);
                result.putExtra(EXTRA_TEAM, ManagersFactory.getInstance().teamsManager.getById(this, intent.getLongExtra(EXTRA_ID, -1)));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_EDIT_TEAM:{

                Team t = intent.getParcelableExtra(EXTRA_TEAM);
                ManagersFactory.getInstance().teamsManager.update(this, t);
                sendTeamsByTournament(t.getTournamentId());
                break;
            }
            case ACTION_DELETE:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);
                result.putExtra(EXTRA_RESULT, ManagersFactory.getInstance().teamsManager.delete(this, id));
                result.putExtra(EXTRA_POSITION, intent.getIntExtra(EXTRA_POSITION, -1));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }

        }


    }

    private void sendTeamsByTournament(long tournamentId){

        Intent result = new Intent(ACTION_GET_TEAMS_BY_TOURNAMENT);
        result.putParcelableArrayListExtra(EXTRA_TEAMS, ManagersFactory.getInstance().teamsManager.getByTournamentId(this, tournamentId));
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, TeamService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }
}
