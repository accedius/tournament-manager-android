package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingFFAMatchStatsFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingMatchStatsFragment;

/**
 * Dialog, that occurs, when the player is selected to change his stats
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
                        ((BowlingFFAMatchStatsFragment)getTargetFragment()).removePlayer(((PlayerStat)getArguments().getParcelable(ExtraConstants.EXTRA_DATA)).getParticipantId(), getArguments().getInt(ExtraConstants.EXTRA_POSITION));
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
