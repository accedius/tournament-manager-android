package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.AddPlayersFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingMatchStatsFragment;

public class HomeAwayDialog extends DialogFragment {
    private String dialHomeName, dialAwayName;

    public static HomeAwayDialog newInstance(String homeName, String awayName, long matchId) {
        HomeAwayDialog fragment = new HomeAwayDialog();
        Bundle b = new Bundle();
        b.putString(ExtraConstants.EXTRA_HOME_NAME, homeName);
        b.putString(ExtraConstants.EXTRA_AWAY_NAME, awayName);
        b.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(b);
        return fragment;
    }

    /**
     * Override to do something when the home team was clicked
     */
    protected void homeClicked() {
        ArrayList<PlayerStat> omitStats = ((BowlingMatchStatsFragment)getTargetFragment()).getOmitPlayers();
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_PARTICIPANT, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
        intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT, omitStats);
        getTargetFragment().startActivityForResult(intent, BowlingMatchStatsFragment.REQUEST_HOME);
    };

    /**
     * Override to do something when the away team was clicked
     */
    protected void awayClicked() {
        ArrayList<PlayerStat> omitStats = ((BowlingMatchStatsFragment)getTargetFragment()).getOmitPlayers();
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_PARTICIPANT, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
        intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT, omitStats);
        getTargetFragment().startActivityForResult(intent, BowlingMatchStatsFragment.REQUEST_AWAY);
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        dialHomeName = getActivity().getString(R.string.add_player_to) + " " + getArguments().getString(ExtraConstants.EXTRA_HOME_NAME);
        dialAwayName = getActivity().getString(R.string.add_player_to) + " " + getArguments().getString(ExtraConstants.EXTRA_AWAY_NAME);

        String[] items = new String[]{ dialHomeName, dialAwayName };
        builder.setItems(items, supplyListener());
        builder.setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.add_player));
        return builder.create();
    }

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        homeClicked();
                        dialog.dismiss();
                        break;
                    case 1: {
                        awayClicked();
                        dialog.dismiss();
                        break;
                    }
                }
            }
        };
    }
}
