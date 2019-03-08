package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.data.entities.PlayerStat;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchStatsFragment;

/**
 * Created by atgot_000 on 24. 4. 2016.
 */
public class PlayerMatchStatDialog extends DialogFragment {
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        EditStatsDialog editDial = EditStatsDialog.newInstance(
                                (PlayerStat)getArguments().getParcelable(ExtraConstants.EXTRA_DATA),
                                getArguments().getInt(ExtraConstants.EXTRA_POSITION),
                                getArguments().getBoolean(ExtraConstants.EXTRA_BOOLEAN_HOME));
                        editDial.setTargetFragment(getTargetFragment(), 1);
                        editDial.show(getTargetFragment().getFragmentManager(), "EDIT_STATS_DIAL");
                        break;
                    }
                    case 1: {
                        ((HockeyMatchStatsFragment)getTargetFragment()).removePlayer(getArguments().getBoolean(ExtraConstants.EXTRA_BOOLEAN_HOME), getArguments().getInt(ExtraConstants.EXTRA_POSITION));
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
        b.putParcelable(ExtraConstants.EXTRA_DATA, statistic);
        b.putBoolean(ExtraConstants.EXTRA_BOOLEAN_HOME, isHome);
        b.putInt(ExtraConstants.EXTRA_POSITION, pos);
        b.putString(ExtraConstants.EXTRA_TITLE, name);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = new String[]{ getActivity().getString(R.string.edit_stats), getActivity().getString(R.string.delete) };
        builder.setItems(items, supplyListener());

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }
}