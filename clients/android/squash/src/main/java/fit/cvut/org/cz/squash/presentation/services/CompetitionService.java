package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to Competitions
 * Created by Vaclav on 28. 3. 2016.
 */
public class CompetitionService extends AbstractIntentServiceWProgress{
    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_COMPETITION = "extra_competition";
    public static final String EXTRA_TOURNAMENT_COUNT = "extra_tournament_count";
    public static final String EXTRA_PLAYERS_COUNT = "extra_players_count";

    public static final String ACTION_CREATE = "fit.cvut.org.cz.squash.presentation.services.new_competition";
    public static final String ACTION_GET_BY_ID = "fit.cvut.org.cz.squash.presentation.services.get_competition_by_id";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.squash.presentation.services.update_competition";
    public static final String ACTION_GET_OVERVIEW = "fit.cvut.org.cz.squash.presentation.services.get_competition_overview";

    public CompetitionService() {
        super("Squash Competition Service");
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, CompetitionService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action){
            case ACTION_CREATE:{
                Competition c = intent.getParcelableExtra(EXTRA_COMPETITION);
                ManagerFactory.getInstance(this).competitionManager.insert(c);
                break;
            }
            case ACTION_GET_BY_ID:{
                Intent result = new Intent();
                result.setAction(ACTION_GET_BY_ID);
                Competition c = ManagerFactory.getInstance(this).competitionManager.getById(intent.getLongExtra(EXTRA_ID, -1));
                result.putExtra(EXTRA_COMPETITION, c);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE:{
                Competition c = intent.getParcelableExtra(EXTRA_COMPETITION);
                ManagerFactory.getInstance(this).competitionManager.update(c);
                break;
            }
            case ACTION_GET_OVERVIEW:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Competition c = ManagerFactory.getInstance(this).competitionManager.getById(id);
                List<Tournament> tournaments = ManagerFactory.getInstance(this).tournamentManager.getByCompetitionId(id);
                List<Player> players = ManagerFactory.getInstance(this).competitionManager.getCompetitionPlayers(id);

                Intent result = new Intent(action);
                result.putExtra(EXTRA_COMPETITION, c);
                result.putExtra(EXTRA_PLAYERS_COUNT, players.size());
                result.putExtra(EXTRA_TOURNAMENT_COUNT, tournaments.size());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }
}
