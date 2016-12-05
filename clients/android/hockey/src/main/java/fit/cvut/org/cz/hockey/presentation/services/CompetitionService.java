package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class CompetitionService extends AbstractIntentServiceWProgress {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_COMPETITION = "extra_competition";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TOURNAMENT_COUNT = "extra_tour_count";
    public static final String EXTRA_PLAYERS_COUNT = "extra_ply_count";

    public static final String ACTION_CREATE = "fit.cvut.org.cz.hockey.presentation.services.competition_create";
    public static final String ACTION_FIND_BY_ID = "fit.cvut.org.cz.hockey.presentation.services.competition_find_by_id";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.hockey.presentation.services.competition_update";

    public CompetitionService() {
        super("Hockey Competition Service");
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, CompetitionService.class);
        res.putExtra(EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected void doWork(Intent intent) throws SQLException {
        String action = intent.getStringExtra(EXTRA_ACTION);
        Competition c;

        switch (action) {
            case ACTION_CREATE:
                c = intent.getParcelableExtra(EXTRA_COMPETITION);
                ManagerFactory.getInstance(this).competitionManager.insert(this, c);
                break;

            case ACTION_UPDATE:
                c = intent.getParcelableExtra(EXTRA_COMPETITION);
                ManagerFactory.getInstance(this).competitionManager.update(this, c);
                break;

            case ACTION_FIND_BY_ID:
                Intent res = new Intent();
                long competitionId = intent.getLongExtra(EXTRA_ID, -1);
                res.setAction(ACTION_FIND_BY_ID);
                Log.d("ERR", "Get By Id before");
                c = ManagerFactory.getInstance(this).competitionManager.getById(this, competitionId);
                Log.d("ERR", "Get By Id after");
                if (ManagerFactory.getInstance(this).sportDBHelper == null)
                    Log.d("ERR", "Sport db helper is null already :(");
                List<Tournament> tournaments = ManagerFactory.getInstance(this).tournamentManager.getByCompetitionId(this, competitionId);
                if (ManagerFactory.getInstance(this).sportDBHelper == null)
                    Log.d("ERR", "Sport db helper is null already :(");
                List<Player> players = ManagerFactory.getInstance(this).competitionManager.getCompetitionPlayers(this, competitionId);

                res.putExtra(EXTRA_COMPETITION, c);
                res.putExtra(EXTRA_PLAYERS_COUNT, players.size());
                res.putExtra(EXTRA_TOURNAMENT_COUNT, tournaments.size());
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
        }
    }
}
