package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.presentation.fragments.AddPlayersFragment;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

/**
 * This activty allows user to specify list of players to be added to competition,
 * tournament, team, or match roaster and then returns this list
 * Created by Vaclav on 12. 4. 2016.
 */
public class AddPlayersActivity extends SelectableListActivity<Player> {
    public static final String EXTRA_OPTION = "extra_option_key";

    /**
     *
     * @param c
     * @param option is passed to accomodated fragment and should specify which players should fragment display
     * @param id
     * @return
     */
    public static Intent newStartIntent(Context c, int option, long id){
        Intent intent = new Intent(c, AddPlayersActivity.class);
        intent.putExtra(EXTRA_OPTION, option);
        intent.putExtra(EXTRA_ID, id);

        return intent;
    }

    @Override
    protected AbstractSelectableListFragment<Player> getListFragment() {
        long id = getIntent().getLongExtra(EXTRA_ID, -1);
        int option = getIntent().getIntExtra(EXTRA_OPTION, -1);
        ArrayList<Player> players = getIntent().getParcelableArrayListExtra(EXTRA_OMIT_DATA);
        if (players != null) return AddPlayersFragment.newInstance(option, id, players);

        return AddPlayersFragment.newInstance(option, id);
    }
}
