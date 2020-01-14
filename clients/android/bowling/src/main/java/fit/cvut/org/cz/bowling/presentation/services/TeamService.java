package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Bowling team service to handle intent/service/activity work in team's scope
 */
public class TeamService extends AbstractIntentServiceWProgress {
    public static final String ACTION_GET_BY_ID = "action_get_team_by_id";
    public static final String ACTION_INSERT = "action_insert_team";
    public static final String ACTION_EDIT = "action_edit_team";
    public static final String ACTION_DELETE = "action_delete_team";
    public static final String ACTION_GET_TEAMS_BY_TOURNAMENT = "action_get_teams_by_tournament";
    public static final String ACTION_GET_TEAMS_IN_TOURNAMENT_BY_MATCH_ID = "action_get_teams_in_tournament_by_match_id";

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, TeamService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);
        return res;
    }

    public TeamService() {
        super("Bowling Team Service");
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    /**
     * sends teams to broadcast
     * @param id id of tournament
     */
    private void sendTeams(long id) {
        Intent res = new Intent(ACTION_GET_TEAMS_BY_TOURNAMENT);
        List<Team> teams = ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).getByTournamentId(id);
        res.putParcelableArrayListExtra(ExtraConstants.EXTRA_TEAMS, new ArrayList<>(teams));

        LocalBroadcastManager.getInstance(this).sendBroadcast(res);
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action) {
            case ACTION_GET_BY_ID: {
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                Team t = ManagerFactory.getInstance(this).getEntityManager(Team.class).getById(id);
                t.setPlayers(((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).getTeamPlayers(t));
                Intent res = new Intent(ACTION_GET_BY_ID);
                res.putExtra(ExtraConstants.EXTRA_TEAM, t);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_INSERT: {
                Team t = intent.getParcelableExtra(ExtraConstants.EXTRA_TEAM);
                ManagerFactory.getInstance(this).getEntityManager(Team.class).insert(t);
                sendTeams(t.getTournamentId());
                break;
            }
            case ACTION_EDIT: {
                Team t = intent.getParcelableExtra(ExtraConstants.EXTRA_TEAM);
                ManagerFactory.getInstance(this).getEntityManager(Team.class).update(t);
                sendTeams(t.getTournamentId());
                break;
            }
            case ACTION_GET_TEAMS_BY_TOURNAMENT: {
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                sendTeams(id);
                break;
            }
            case ACTION_GET_TEAMS_IN_TOURNAMENT_BY_MATCH_ID: {
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                IManagerFactory iManagerFactory = ManagerFactory.getInstance(this);
                Match match = iManagerFactory.getEntityManager(Match.class).getById(matchId);
                long tournamentId = match.getTournamentId();

                Intent res = new Intent(ACTION_GET_TEAMS_IN_TOURNAMENT_BY_MATCH_ID);
                List<Team> teams = ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).getByTournamentId(tournamentId);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_TEAMS, new ArrayList<>(teams));

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
            }
            case ACTION_DELETE: {
                Intent res = new Intent(ACTION_DELETE);
                long teamId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                if (teamId == -1)
                    break;
                boolean result = ManagerFactory.getInstance(this).getEntityManager(Team.class).delete(teamId);
                res.putExtra(ExtraConstants.EXTRA_RESULT, result);
                res.putExtra(ExtraConstants.EXTRA_POSITION, intent.getIntExtra(ExtraConstants.EXTRA_POSITION, -1));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}
