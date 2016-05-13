package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.hockey.presentation.fragments.AddPlayersFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchStatsFragment;

/**
 * Created by atgot_000 on 29. 4. 2016.
 */
public class HomeAwayDialog extends DialogFragment {

    private static final String ARG_HOME_NAME = "arg_home";
    private static final String ARG_AWAY_NAME = "arg_away";
    private static final String ARG_MATCH_ID = "arg_match_id";

    private String dialHomeName, dialAwayName;

    public static HomeAwayDialog newInstance(String homeName, String awayName, long matchId)
    {
        HomeAwayDialog fragment = new HomeAwayDialog();

        Bundle b = new Bundle();
        b.putString(ARG_HOME_NAME, homeName);
        b.putString(ARG_AWAY_NAME, awayName);
        b.putLong(ARG_MATCH_ID, matchId);

        fragment.setArguments( b );
        return fragment;
    }

    /**
     * overrride this function to show home name in dialog
     * @return String with home name
     */
    protected String getHomeName() { return null; }

    /**
     * overrride this function to show away name in dialog
     * @return String with away name
     */
    protected String getAwayName() { return null; }

    /**
     * Override to do something when the home team was clicked
     */
    protected void homeClicked() {
        ArrayList<MatchPlayerStatistic> omitStats = ((HockeyMatchStatsFragment)getTargetFragment()).getOmitPlayers();
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_PARTICIPANT, getArguments().getLong(ARG_MATCH_ID));
        intent.putParcelableArrayListExtra(AddPlayersActivity.EXTRA_OMIT_DATA, omitStats);
        getTargetFragment().startActivityForResult(intent, HockeyMatchStatsFragment.REQUEST_HOME);
    };

    /**
     * Override to do something when the away team was clicked
     */
    protected void awayClicked() {
        ArrayList<MatchPlayerStatistic> omitStats = ((HockeyMatchStatsFragment)getTargetFragment()).getOmitPlayers();
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_PARTICIPANT, getArguments().getLong(ARG_MATCH_ID));
        intent.putParcelableArrayListExtra(AddPlayersActivity.EXTRA_OMIT_DATA, omitStats);
        getTargetFragment().startActivityForResult(intent, HockeyMatchStatsFragment.REQUEST_AWAY);
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        if(getArguments() != null)
        {
            dialHomeName = getActivity().getString(R.string.add_player) + " " + getArguments().getString(ARG_HOME_NAME, getActivity().getString(R.string.add_home_complete) );
            dialAwayName = getActivity().getString(R.string.add_player) + " " + getArguments().getString(ARG_AWAY_NAME, getActivity().getString(R.string.add_away_complete));
        } else if( getHomeName() != null && getAwayName() != null ){
            dialHomeName = getActivity().getString(R.string.add_player) + " " + getHomeName();
            dialAwayName = getActivity().getString(R.string.add_player) + " " + getAwayName();
        }
        else {
            dialHomeName = getActivity().getString(R.string.add_home_complete);
            dialAwayName = getActivity().getString(R.string.add_away_complete);
        }


        String[] items = new String[]{ dialHomeName, dialAwayName };

        builder.setItems( items, supplyListener() );

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
                    default:
                        break;
                }
            }
        };
    }
}
