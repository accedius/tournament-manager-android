package fit.cvut.org.cz.squash.presentation.activities;

import fit.cvut.org.cz.squash.presentation.fragments.SelectedStringsFragment;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

/**
 * Created by Vaclav on 4. 4. 2016.
 */
public class SelectPlayersActivity extends SelectableListActivity<Player> {
    @Override
    protected AbstractSelectableListFragment<Player> getListFragment() {

        long id = getIntent().getLongExtra(EXTRA_ID, -1);

        return AbstractSelectableListFragment.newInstance(id, SelectedStringsFragment.class);
    }
}
