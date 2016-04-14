package fit.cvut.org.cz.hockey.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
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

    public static final String ACTION_FIND_BY_ID = "action_find_match_by_id";
    public static final String ACTION_CREATE = "action_create_match";
    public static final String ACTION_UPDATE = "action_update_match";

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

                //TODO

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

//                if( intent.getLongExtra( EXTRA_ID, -1 ) != 1 )
//                    break;

                ScoredMatch m = new ScoredMatch();
                m.setId(1);
                m.setDate(new Date(2011, 11, 11));
                m.setNote("My Note");
                m.setAwayParticipantId(1);
                m.setAwayName("Awaynameblabla");
                m.setHomeParticipantId(3);
                m.setHomeName("Homenameblabla");
                m.setPeriod(1);
                m.setRound(1);
                res.putExtra(EXTRA_MATCH, m);

                ArrayList<Participant> participants = new ArrayList<>();
                participants.add( new Participant(1, "Awaynameblabla" ) );
                participants.add( new Participant(3, "Homenameblabla" ) );
                participants.add( new Participant(2, "NotSelcttedParticipant" ) );

                res.putParcelableArrayListExtra( EXTRA_PART_LIST, participants );


                LocalBroadcastManager.getInstance(this).sendBroadcast(res);

                break;
            }
        }
    }
}
