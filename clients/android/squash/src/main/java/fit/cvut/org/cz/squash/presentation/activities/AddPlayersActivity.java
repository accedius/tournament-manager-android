package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.fragments.AddPlayersFragment;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

/**
 * This activty allows user to specify list of players to be added to competition,
 * tournament, team, or match roaster and then returns this list
 * Created by Vaclav on 12. 4. 2016.
 */
public class AddPlayersActivity extends SelectableListActivity<Player> {
    /**
     *
     * @param c
     * @param option is passed to accommodated fragment and should specify which players should fragment display
     * @param id
     * @return intent for this activity
     */
    public static Intent newStartIntent(Context c, int option, long id){
        Intent intent = new Intent(c, AddPlayersActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_OPTION, option);
        intent.putExtra(ExtraConstants.EXTRA_ID, id);

        return intent;
    }

    @Override
    protected AbstractSelectableListFragment<Player> getListFragment() {
        long id = getIntent().getLongExtra(ExtraConstants.EXTRA_ID, -1);
        int option = getIntent().getIntExtra(ExtraConstants.EXTRA_OPTION, -1);
        if (option == AddPlayersFragment.OPTION_MATCH) {
            ArrayList<PlayerStat> playerStatistics = getIntent().getParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT);
            if (playerStatistics != null) return AddPlayersFragment.newInstance(option, id, playerStatistics, 1);
            return AddPlayersFragment.newInstance(option, id);
        }
        ArrayList<Player> players = getIntent().getParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT);
        if (players != null) return AddPlayersFragment.newInstance(option, id, players);

        return AddPlayersFragment.newInstance(option, id);
    }
}
