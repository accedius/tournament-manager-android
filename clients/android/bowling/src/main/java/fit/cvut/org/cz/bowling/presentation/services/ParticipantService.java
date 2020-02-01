package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

public class ParticipantService extends AbstractIntentServiceWProgress {
    public static final String ACTION_GET_BY_MATCH_ID = "action_get_by_match_id";
    public static final String ACTION_GET_BY_MATCH_ID_FOR_MANAGING = "action_get_by_match_id_for_managing";
    public static final String ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS = "action_get_by_match_id_with_all_contents";

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, ParticipantService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);
        return res;
    }

    public ParticipantService() {
        super("Bowling Participant Service");
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);

        switch (action) {
            case ACTION_GET_BY_MATCH_ID_FOR_MANAGING:
            case ACTION_GET_BY_MATCH_ID: {
                Intent res = new Intent(action);

                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_MATCH_ID, -1);
                if(matchId == -1)
                    break;
                ArrayList<Participant> participants = new ArrayList<>(((IParticipantManager) ManagerFactory.getInstance(this).getEntityManager(Participant.class)).getByMatchIdWithPlayerStats(matchId));

                res.putExtra(ExtraConstants.EXTRA_MATCH_ID,matchId);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS, participants);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }

            case ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS: {
                Intent res = new Intent(action);

                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_MATCH_ID, -1);
                if(matchId == -1)
                    break;
                ArrayList<Participant> participants = new ArrayList<>(((IParticipantManager) ManagerFactory.getInstance(this).getEntityManager(Participant.class)).getByMatchIdWithAllContents(matchId));

                res.putExtra(ExtraConstants.EXTRA_MATCH_ID,matchId);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS, participants);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}
