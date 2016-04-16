package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.fragments.AddPlayersFragment;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

/**
 * Created by atgot_000 on 15. 4. 2016.
 */
public class AddPlayersActivity extends SelectableListActivity<Player> {

    private static final String ARG_OPTION = "option_arg";
    private static final String ARG_ID = "id_arg";

    public static Intent newStartIntent( Context context,  int option, long id )
    {
        Intent intent = new Intent( context, AddPlayersActivity.class );
        intent.putExtra( ARG_OPTION, option );
        intent.putExtra( ARG_ID, id );

        return intent;
    }

    @Override
    protected AbstractSelectableListFragment<Player> getListFragment() {

        int option = getIntent().getIntExtra( ARG_OPTION, -1 );
        long id = getIntent().getLongExtra( ARG_ID, -1 );

        return AddPlayersFragment.newInstance(option, id);
    }
}
