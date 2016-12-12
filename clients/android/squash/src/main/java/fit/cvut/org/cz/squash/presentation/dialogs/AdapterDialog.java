package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.fragments.MatchPlayersFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SetsFragment;

/**
 * Dialog that is bound to MatchPlayersFragment or SetsFragment and allows user to delete
 * data from their adapters
 * Created by Vaclav on 11. 4. 2016.
 */
public class AdapterDialog extends DialogFragment {
    public AdapterDialog(){}

    public static final String ARG_POSITION = "arg_pos";
    public static final String ARG_OPTION = "arg_option";
    public static final String ARG_TITLE = "arg_title";

    public static AdapterDialog newInstance(int position, int option, String name){
        AdapterDialog fragment = new AdapterDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_OPTION, option);
        args.putString(ARG_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(new String[]{getString(R.string.delete)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Fragment fr = getTargetFragment();
                    if (fr != null) {
                        if (fr instanceof SetsFragment) {
                            ((SetsFragment)fr).adapter.deleteItem(getArguments().getInt(ARG_POSITION));
                        }
                        if (fr instanceof MatchPlayersFragment) {
                            if (getArguments().getInt(ARG_OPTION) == 0)
                                ((MatchPlayersFragment)fr).homeAdapter.deleteItem(getArguments().getInt(ARG_POSITION));
                            else
                                ((MatchPlayersFragment)fr).awayAdapter.deleteItem(getArguments().getInt(ARG_POSITION));
                        }
                    }
                }
            }
        });

        builder.setTitle(getArguments().getString(ARG_TITLE));
        return builder.create();
    }
}
