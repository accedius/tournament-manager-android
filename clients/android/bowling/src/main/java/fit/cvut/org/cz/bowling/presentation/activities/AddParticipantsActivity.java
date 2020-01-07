package fit.cvut.org.cz.bowling.presentation.activities;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.AddParticipantsFragment;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

public class AddParticipantsActivity extends SelectableListActivity<Participant> {
    /**
     * Creates a new intent to start this activity
     * @param context context of intent
     * @param option option if to add individuals or teams
     * @param matchId id of match to add participant to
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, int option, long matchId) {
        Intent intent = new Intent(context, AddParticipantsActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_OPTION, option);
        intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, matchId);

        return intent;
    }

    @Override
    protected AbstractSelectableListFragment<Participant> getListFragment() {
        long matchId = getIntent().getLongExtra(ExtraConstants.EXTRA_MATCH_ID, -1);
        int option = getIntent().getIntExtra(ExtraConstants.EXTRA_OPTION, -1);
        ArrayList<Participant> omitParticipants = getIntent().getParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT);
        return AddParticipantsFragment.newInstance(option, matchId, omitParticipants);
    }
}
