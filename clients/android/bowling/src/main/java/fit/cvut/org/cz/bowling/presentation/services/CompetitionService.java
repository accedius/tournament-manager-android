package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.sql.SQLException;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

public class CompetitionService extends AbstractIntentServiceWProgress {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public static final String ACTION_CREATE = "fit.cvut.org.cz.bowling.presentation.services.competition_create";
    public static final String ACTION_FIND_BY_ID = "fit.cvut.org.cz.bowling.presentation.services.competition_find_by_id";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.bowling.presentation.services.competition_update";
    public static final String ACTION_GET_OVERVIEW = "fit.cvut.org.cz.squash.presentation.services.get_competition_overview";

    public CompetitionService() {
        super("Bowling Competition Service");
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, CompetitionService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected void doWork(Intent intent) throws SQLException {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);
        Competition c;

        switch (action) {
            case ACTION_CREATE:
                c = intent.getParcelableExtra(ExtraConstants.EXTRA_COMPETITION);
                ManagerFactory.getInstance(this).getEntityManager(Competition.class).insert(c);
                break;

            case ACTION_UPDATE:
                c = intent.getParcelableExtra(ExtraConstants.EXTRA_COMPETITION);
                ManagerFactory.getInstance(this).getEntityManager(Competition.class).update(c);
                break;

            case ACTION_FIND_BY_ID:
                Intent res = new Intent();
                long competitionId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                res.setAction(ACTION_FIND_BY_ID);
                c = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(competitionId);
                //List<Tournament> tournaments = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getByCompetitionId(competitionId);
                List<Player> players = ((ICompetitionManager)ManagerFactory.getInstance(this).getEntityManager(Competition.class)).getCompetitionPlayers(competitionId);

                res.putExtra(ExtraConstants.EXTRA_COMPETITION, c);
                res.putExtra(ExtraConstants.EXTRA_PLAYERS_COUNT, players.size());
                //res.putExtra(ExtraConstants.EXTRA_TOURNAMENTS_COUNT, tournaments.size());
                res.putExtra(ExtraConstants.EXTRA_TOURNAMENTS_COUNT, 0);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            case ACTION_GET_OVERVIEW:{
                long id = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                c = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(id);
                //List<Tournament> tournaments = ((ITournamentManager)ManagerFactory.getInstance(this).getEntityManager(Tournament.class)).getByCompetitionId(id);
                List<Player> players = ((ICompetitionManager)ManagerFactory.getInstance(this).getEntityManager(Competition.class)).getCompetitionPlayers(id);

                Intent result = new Intent(action);
                result.putExtra(ExtraConstants.EXTRA_COMPETITION, c);
                result.putExtra(ExtraConstants.EXTRA_PLAYERS_COUNT, 0);
                result.putExtra(ExtraConstants.EXTRA_TOURNAMENTS_COUNT, 0);
                result.putExtra(ExtraConstants.EXTRA_PLAYERS_COUNT, players.size());
                //result.putExtra(ExtraConstants.EXTRA_TOURNAMENTS_COUNT, tournaments.size());

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }
}

