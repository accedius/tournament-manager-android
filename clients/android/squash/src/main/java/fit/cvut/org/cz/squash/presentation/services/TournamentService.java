package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to Tournaments
 * Created by Vaclav on 28. 3. 2016.
 */
public class TournamentService extends AbstractIntentServiceWProgress{
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
        return ExtraConstants.EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, TournamentService.class);
        intent.putExtra(ExtraConstants.EXTRA_ACTION, action);

        return intent;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action){
            case ACTION_CREATE:{
                Tournament t = intent.getParcelableExtra(ExtraConstants.EXTRA_TOURNAMENT);
                ManagerFactory.getInstance(this).getEntityManager(Tournament.class).insert(t);
                break;
            }
            case ACTION_UPDATE:{
                Tournament t = intent.getParcelableExtra(ExtraConstants.EXTRA_TOURNAMENT);
                ManagerFactory.getInstance(this).getEntityManager(Tournament.class).update(t);
                break;
            }
            case ACTION_DELETE:{
                Intent result = new Intent(action);
                int position = intent.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                result.putExtra(ExtraConstants.EXTRA_POSITION, position);
                result.putExtra(ExtraConstants.EXTRA_RESULT, ManagerFactory.getInstance(this).getEntityManager(Tournament.class).delete(intent.getLongExtra(ExtraConstants.EXTRA_ID, -1)));

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_BY_ID:{
                Intent result = new Intent();
                result.setAction(ACTION_GET_BY_ID);
                Tournament t = ManagerFactory.getInstance(this).getEntityManager(Tournament.class).getById(intent.getLongExtra(ExtraConstants.EXTRA_ID, -1));
                result.putExtra(ExtraConstants.EXTRA_TOURNAMENT, t);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_BY_COMPETITION_ID:{
                Intent result = new Intent();
                result.setAction(ACTION_GET_BY_COMPETITION_ID);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                ITournamentManager tournamentManager = ManagerFactory.getInstance(this).getEntityManager(Tournament.class);
                ArrayList<Tournament> tournaments = new ArrayList<>(tournamentManager.getByCompetitionId(id));
                result.putParcelableArrayListExtra(ExtraConstants.EXTRA_TOURNAMENT, tournaments);
                Competition c = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(id);
                result.putExtra(ExtraConstants.EXTRA_TYPE, c.getType().id);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_OVERVIEW:{
                Intent result = new Intent(action);
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                CompetitionType type = CompetitionTypes.competitionTypes()[intent.getIntExtra(ExtraConstants.EXTRA_TYPE, 0)];
                Tournament t = ManagerFactory.getInstance(this).getEntityManager(Tournament.class).getById(id);
                List<Player> players = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getTournamentPlayers(id);
                List<Match> matches = ((IMatchManager)ManagerFactory.getInstance(this).getEntityManager(Match.class)).getByTournamentId(id);

                if (type.equals(CompetitionTypes.teams()))
                    result.putExtra(ExtraConstants.EXTRA_TEAMS_COUNT, ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).getByTournamentId(id).size());
                else
                    result.putExtra(ExtraConstants.EXTRA_TEAMS_COUNT, 0);

                result.putExtra(ExtraConstants.EXTRA_TOURNAMENT, t);
                result.putExtra(ExtraConstants.EXTRA_PLAYERS_COUNT, players.size());
                result.putExtra(ExtraConstants.EXTRA_MATCHES_COUNT, matches.size());
                result.putExtra(ExtraConstants.EXTRA_TYPE, type.id);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GENERATE_ROSTERS: {
                Intent result = new Intent(action);
                boolean res = ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).generateRosters(
                        intent.getLongExtra(ExtraConstants.EXTRA_ID, -1),
                        intent.getLongExtra(ExtraConstants.EXTRA_TOURNAMENT, -1),
                        intent.getIntExtra(ExtraConstants.EXTRA_GENERATING_TYPE, -1));

                result.putExtra(ExtraConstants.EXTRA_RESULT, res);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }
}
