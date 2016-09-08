package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class TeamService extends AbstractIntentServiceWProgress {

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TEAM = "extra_team";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_TEAM_LIST = "extra_team_list";
    public static final String EXTRA_OUTCOME = "extra_team_outcome";

    public static final int OUTCOME_OK = 1;
    public static final int OUTCOME_NOT_OK = 0;

    public static final String ACTION_GET_BY_ID = "action_get_team_by_id";
    public static final String ACTION_INSERT = "action_insert_team";
    public static final String ACTION_EDIT = "action_edit_team";
    public static final String ACTION_DELETE = "action_delete_team";
    public static final String ACTION_GET_TEAMS_BY_TOURNAMENT = "action_get_teams_by_tournament";

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, TeamService.class);
        res.putExtra(EXTRA_ACTION, action);
        return res;
    }

    public TeamService() {
        super("Hockey Team Service");
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    /**
     * sends teams to broadcast
     * @param id id of tournament
     */
    private void sendTeams(long id) {
        Intent res = new Intent(ACTION_GET_TEAMS_BY_TOURNAMENT);
        ArrayList<Team> teams = ManagerFactory.getInstance().teamManager.getByTournamentId(this, id);
        res.putParcelableArrayListExtra(EXTRA_TEAM_LIST, teams);

        LocalBroadcastManager.getInstance(this ).sendBroadcast( res);
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action) {
            case ACTION_GET_BY_ID: {
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Team t = ManagerFactory.getInstance().teamManager.getById(this, id);
                t.setPlayers(ManagerFactory.getInstance().packagePlayerManager.getPlayersByTeam( this, t.getId() ));
                Intent res = new Intent(ACTION_GET_BY_ID);
                res.putExtra(EXTRA_TEAM, t);

                LocalBroadcastManager.getInstance(this ).sendBroadcast( res);
                break;
            }
            case ACTION_INSERT: {
                Team t = intent.getParcelableExtra(EXTRA_TEAM);
                ManagerFactory.getInstance().teamManager.insert(this, t);
                sendTeams(t.getTournamentId());
                break;
            }
            case ACTION_EDIT: {
                Team t = intent.getParcelableExtra(EXTRA_TEAM);
                ManagerFactory.getInstance().teamManager.update(this, t);
                sendTeams(t.getTournamentId());
                break;
            }
            case ACTION_GET_TEAMS_BY_TOURNAMENT: {
                long id = intent.getLongExtra(EXTRA_ID, -1);
                sendTeams(id);
                break;
            }
            case ACTION_DELETE: {
                Intent res = new Intent(ACTION_DELETE);
                long teamId = intent.getLongExtra(EXTRA_ID, -1);
                if (teamId == -1)
                    break;
                if (ManagerFactory.getInstance().teamManager.delete( this, teamId)) {
                    res.putExtra(EXTRA_OUTCOME, OUTCOME_OK);
                    int position = intent.getIntExtra(EXTRA_POSITION, -1);
                    res.putExtra(EXTRA_POSITION, position);
                } else {
                    res.putExtra(EXTRA_OUTCOME, OUTCOME_NOT_OK);
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}
