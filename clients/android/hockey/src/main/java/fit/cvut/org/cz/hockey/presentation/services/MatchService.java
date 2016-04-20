package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.NewMatchSpinnerParticipant;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by atgot_000 on 10. 4. 2016.
 */
public class MatchService extends AbstractIntentServiceWProgress {

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_MATCH = "extra_match";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TOUR_ID = "extra_tour_id";
    public static final String EXTRA_PART_LIST = "extra_participants_list";
    public static final String EXTRA_MATCH_LIST = "extra_match_list";

    public static final String ACTION_FIND_BY_ID = "action_find_match_by_id";
    public static final String ACTION_FIND_BY_TOURNAMENT_ID = "action_find_match_by_tournament_id";
    public static final String ACTION_CREATE = "action_create_match";
    public static final String ACTION_UPDATE = "action_update_match";
    public static final String ACTION_BEGIN = "action_begin_match";


    public MatchService() {
        super("Hockey Match Service");
    }

    public static Intent newStartIntent(String action, Context context)
    {
        Intent res = new Intent(context, MatchService.class);
        res.putExtra(EXTRA_ACTION, action);

        return res;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action)
        {
            case ACTION_CREATE:
            {
                ScoredMatch m = intent.getParcelableExtra( EXTRA_MATCH );

                ManagerFactory.getInstance().matchManager.insert( this, m );

                break;
            }
            case ACTION_UPDATE:
            {
                ScoredMatch m = intent.getParcelableExtra( EXTRA_MATCH );

                //TODO

                break;
            }
            case ACTION_FIND_BY_ID:
            {
                Intent res = new Intent();
                res.setAction(ACTION_FIND_BY_ID);
                long matchId = intent.getLongExtra( EXTRA_ID, -1 );
                long tourId = intent.getLongExtra( EXTRA_TOUR_ID, -1);

                if( matchId != -1 ) {
                    ScoredMatch m = ManagerFactory.getInstance().matchManager.getById( this, matchId );
                    res.putExtra(EXTRA_MATCH, m);
                }

                ArrayList<Team> tourTeams = ManagerFactory.getInstance().teamManager.getByTournamentId( this, tourId );

                ArrayList<NewMatchSpinnerParticipant> participants = new ArrayList<>();

                for( Team t : tourTeams )
                {
                    participants.add( new NewMatchSpinnerParticipant(t.getId(), t.getName()) );
                }

                res.putParcelableArrayListExtra( EXTRA_PART_LIST, participants );


                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
            case ACTION_FIND_BY_TOURNAMENT_ID:
            {
                Intent res = new Intent(ACTION_FIND_BY_TOURNAMENT_ID);
                //TODO remove mock

                long tourId = intent.getLongExtra(EXTRA_TOUR_ID, -1);
                ArrayList<ScoredMatch> matches = ManagerFactory.getInstance().matchManager.getByTournamentId( this, tourId );
                res.putParcelableArrayListExtra( EXTRA_MATCH_LIST, matches );

                LocalBroadcastManager.getInstance( this ).sendBroadcast( res );
                break;
            }
            case ACTION_BEGIN:
            {
                Intent res = new Intent(ACTION_BEGIN);

                long matchId = intent.getLongExtra(EXTRA_ID, -1);
                ManagerFactory.getInstance().matchManager.beginMatch( this, matchId );

                LocalBroadcastManager.getInstance( this ).sendBroadcast( res );
                break;
            }
        }
    }
}
