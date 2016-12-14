package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.data.entities.PlayerStat;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchStatsFragment;

/**
 * Created by atgot_000 on 24. 4. 2016.
 */
public class PlayerMatchStatDialog extends DialogFragment {
    public static final String ARG_DATA = "data";
    public static final String ARG_POSITION = "position";
    public static final String ARG_HOME = "home";
    public static final String ARG_TITLE = "arg_title";

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        EditStatsDialog editDial = EditStatsDialog.newInstance(
                                (PlayerStat)getArguments().getParcelable(ARG_DATA),
                                getArguments().getInt(ARG_POSITION),
                                getArguments().getBoolean(ARG_HOME));
                        editDial.setTargetFragment(getTargetFragment(), 1);
                        editDial.show(getTargetFragment().getFragmentManager(), "EDIT_STATS_DIAL");
                        break;
                    }
                    case 1: {
                        ((HockeyMatchStatsFragment)getTargetFragment()).removePlayer(getArguments().getBoolean(ARG_HOME), getArguments().getInt(ARG_POSITION));
                        break;
                    }
                }
                dialog.dismiss();
            }
        };
    }

    public static PlayerMatchStatDialog newInstance(PlayerStat statistic, int pos, boolean isHome, String name) {
        PlayerMatchStatDialog fragment = new PlayerMatchStatDialog();
        Bundle b = new Bundle();
        b.putParcelable(ARG_DATA, statistic);
        b.putBoolean(ARG_HOME, isHome);
        b.putInt(ARG_POSITION, pos);
        b.putString(ARG_TITLE, name);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = new String[]{ getActivity().getString(R.string.edit_stats), getActivity().getString(R.string.delete) };
        builder.setItems(items, supplyListener());

        builder.setTitle(getArguments().getString(ARG_TITLE));
        return builder.create();
    }
}