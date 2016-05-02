package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import fit.cvut.org.cz.hockey.R;

/**
 * Created by atgot_000 on 29. 4. 2016.
 */
public class HomeAwayDialog extends DialogFragment {

    private static final String ARG_HOME_NAME = "arg_home";
    private static final String ARG_AWAY_NAME = "arg_away";

    private String dialHomeName, dialAwayName;

    public static HomeAwayDialog newInstance(String homeName, String awayName)
    {
        HomeAwayDialog fragment = new HomeAwayDialog();

        Bundle b = new Bundle();
        b.putString(ARG_HOME_NAME, homeName);
        b.putString(ARG_AWAY_NAME, awayName);

        fragment.setArguments( b );
        return fragment;
    }
    protected String getHomeName() { return null; }
    protected String getAwayName() { return null; }

    protected void homeClicked() {};

    protected void awayClicked() {};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        if(getArguments() != null)
        {
            dialHomeName = getArguments().getString(ARG_HOME_NAME, getActivity().getString(R.string.add_home_complete) );
            dialAwayName = getArguments().getString(ARG_AWAY_NAME, getActivity().getString(R.string.add_away_complete));
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
