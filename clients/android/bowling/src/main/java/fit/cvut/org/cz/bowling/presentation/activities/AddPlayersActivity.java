package fit.cvut.org.cz.bowling.presentation.activities;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.AddPlayersFragment;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

public class AddPlayersActivity extends SelectableListActivity<Player> {
    /**
     * Creates a new intent to start this activity
     * @param context
     * @param option option where are we adding players - it is passed into the fragment
     * @param id id of where are we adding players, also passed into fragment
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, int option, long id) {
        Intent intent = new Intent(context, AddPlayersActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_OPTION, option);
        intent.putExtra(ExtraConstants.EXTRA_ID, id);

        return intent;
    }

    @Override
    protected AbstractSelectableListFragment<Player> getListFragment() {
        long id = getIntent().getLongExtra(ExtraConstants.EXTRA_ID, -1);
        int option = getIntent().getIntExtra(ExtraConstants.EXTRA_OPTION, -1);
        if (option == AddPlayersFragment.OPTION_PARTICIPANT) {
            ArrayList<PlayerStat> playerStatistics = getIntent().getParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT);
            if (playerStatistics != null) return AddPlayersFragment.newInstance(option, id, playerStatistics, 1);
            return AddPlayersFragment.newInstance(option, id);
        }
        ArrayList<Player> players = getIntent().getParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT);
        if (players != null) return AddPlayersFragment.newInstance(option, id, players);

        return AddPlayersFragment.newInstance(option, id);


    }
}
