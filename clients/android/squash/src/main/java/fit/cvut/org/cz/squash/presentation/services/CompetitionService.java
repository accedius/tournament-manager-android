package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to Competitions
 * Created by Vaclav on 28. 3. 2016.
 */
public class CompetitionService extends AbstractIntentServiceWProgress{
    public static final String ACTION_CREATE = "fit.cvut.org.cz.squash.presentation.services.new_competition";
    public static final String ACTION_GET_BY_ID = "fit.cvut.org.cz.squash.presentation.services.get_competition_by_id";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.squash.presentation.services.update_competition";
    public static final String ACTION_GET_OVERVIEW = "fit.cvut.org.cz.squash.presentation.services.get_competition_overview";

    public CompetitionService() {
        super("Squash Competition Service");
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, CompetitionService.class);
        intent.putExtra(ExtraConstants.EXTRA_ACTION, action);

        return intent;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action){
            case ACTION_CREATE:{
                Competition c = intent.getParcelableExtra(ExtraConstants.EXTRA_COMPETITION);
                ManagerFactory.getInstance(this).getEntityManager(Competition.class).insert(c);
                break;
            }
            case ACTION_GET_BY_ID:{
                Intent result = new Intent();
                result.setAction(ACTION_GET_BY_ID);
                Competition c = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(intent.getLongExtra(ExtraConstants.EXTRA_ID, -1));
                result.putExtra(ExtraConstants.EXTRA_COMPETITION, c);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE:{
                Competition c = intent.getParcelableExtra(ExtraConstants.EXTRA_COMPETITION);
                ManagerFactory.getInstance(this).getEntityManager(Competition.class).update(c);
                break;
            }
            case ACTION_GET_OVERVIEW:{
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                Competition c = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(id);
                List<Tournament> tournaments = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getByCompetitionId(id);
                List<Player> players = ((ICompetitionManager)ManagerFactory.getInstance(this).getEntityManager(Competition.class)).getCompetitionPlayers(id);

                Intent result = new Intent(action);
                result.putExtra(ExtraConstants.EXTRA_COMPETITION, c);
                result.putExtra(ExtraConstants.EXTRA_PLAYERS_COUNT, players.size());
                result.putExtra(ExtraConstants.EXTRA_TOURNAMENTS_COUNT, tournaments.size());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }
}
