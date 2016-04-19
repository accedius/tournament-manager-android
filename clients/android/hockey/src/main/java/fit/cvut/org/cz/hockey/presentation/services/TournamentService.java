package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
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

    public static final String ACTION_CREATE = "fit.cvut.org.cz.hockey.presentation.services.tournament_create";
    public static final String ACTION_FIND_BY_ID = "fit.cvut.org.cz.hockey.presentation.services.tournament_find_by_id";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.hockey.presentation.services.tournament_update";
    public static final String ACTION_GET_ALL = "fit.cvut.org.cz.hockey.presentation.services.tournament_all";
    public static final String ACTION_GET_CONFIG_BY_ID = "fit.cvut.org.cz.hockey.presentation.services.tournament_get_configuration_by_tournament_id";
    public static final String ACTION_SET_CONFIG = "fit.cvut.org.cz.hockey.presentation.services.tournament_set_configuration";

    public TournamentService() {
        super("Hockey Tournament Service");
    }

    public static Intent newStartIntent(String action, Context context)
    {
        Intent res = new Intent(context, TournamentService.class);
        res.putExtra(EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    private ArrayList<Tournament> getData( long competitionID )
    {
        ArrayList<Tournament> res = ManagerFactory.getInstance().tournamentManager.getByCompetitionId( this, competitionID );
        return res;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action)
        {
            case ACTION_CREATE:
            {
                Tournament t = intent.getParcelableExtra( EXTRA_TOURNAMENT );
                ManagerFactory.getInstance().tournamentManager.insert( this, t);

                break;
            }
            case ACTION_FIND_BY_ID:
            {
                Intent res = new Intent();
                res.setAction(ACTION_FIND_BY_ID);
                long id = intent.getLongExtra(EXTRA_ID, -1);

                Tournament c = ManagerFactory.getInstance().tournamentManager.getById(this, id);
                res.putExtra(EXTRA_TOURNAMENT, c);

                ArrayList<Team> teams = ManagerFactory.getInstance().teamManager.getByTournamentId( this, id );
                ArrayList<Player> players = ManagerFactory.getInstance().packagePlayerManager.getPlayersByTournament(this, id);
                ArrayList<ScoredMatch> matches = ManagerFactory.getInstance().matchManager.getByTournamentId( this, id );

                res.putExtra(EXTRA_MATCHES_SUM, matches.size());
                res.putExtra(EXTRA_TEAMS_SUM, teams.size());
                res.putExtra(EXTRA_PLAYER_SUM, players.size());
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_UPDATE:
            {
                Tournament c = intent.getParcelableExtra(EXTRA_TOURNAMENT);
                ManagerFactory.getInstance().tournamentManager.update( this, c );

                break;
            }
            case ACTION_GET_ALL:
            {
                Intent res = new Intent();
                res.setAction( ACTION_GET_ALL );
                res.putParcelableArrayListExtra(EXTRA_LIST, getData( intent.getLongExtra(EXTRA_COMP_ID, -1) ));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_GET_CONFIG_BY_ID:
            {
                Intent res = new Intent();
                res.setAction(ACTION_GET_CONFIG_BY_ID);
                res.putExtra(EXTRA_CONFIGURATION, ManagerFactory.getInstance().pointConfigManager.getByTournamentId(this, intent.getLongExtra(EXTRA_ID, -1)));
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_SET_CONFIG:
            {
                PointConfiguration pc = intent.getParcelableExtra( EXTRA_CONFIGURATION );
                Long tourId = intent.getLongExtra(EXTRA_ID, -1);
                ManagerFactory.getInstance().pointConfigManager.update( this, pc, tourId );

                break;
            }
        }
    }
}
