package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.squash.presentation.fragments.AddPlayersFragment;
import fit.cvut.org.cz.squash.presentation.fragments.MatchPlayersFragmentImproved;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;

/**
 * Created by Vaclav on 11. 4. 2016.
 */
public class SelectTeamDialogImproved extends DialogFragment {

    public SelectTeamDialogImproved(){}
    public static final String ARG_HOME_ID = "arg_home_id";
    public static final String ARG_AWAY_ID = "arg_away_id";
    public static final String ARG_HOME_NAME = "arg_home_name";
    public static final String ARG_AWAY_NAME = "arg_away_name";

    public static SelectTeamDialogImproved newInstance(long homeId, long awayId, String homeName, String awayName){
        SelectTeamDialogImproved fragment = new SelectTeamDialogImproved();
        Bundle args = new Bundle();
        args.putLong(ARG_AWAY_ID, awayId);
        args.putLong(ARG_HOME_ID, homeId);
        args.putString(ARG_HOME_NAME, homeName);
        args.putString(ARG_AWAY_NAME, awayName);
        fragment.setArguments(args);
        return fragment;
    }

    private void homeClick(){
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_TEAM, getArguments().getLong(ARG_HOME_ID));
        Fragment fr = getTargetFragment();
        if (fr !=null && fr instanceof MatchPlayersFragmentImproved){
            MatchPlayersFragmentImproved ifr = (MatchPlayersFragmentImproved) fr;
            intent.putExtra(AddPlayersActivity.EXTRA_OMIT_DATA, ifr.homeAdapter.getData());
            ifr.startActivityForResult(intent, 0);
        }
    }
    private void awayClick(){
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_TEAM, getArguments().getLong(ARG_AWAY_ID));
        Fragment fr = getTargetFragment();
        if (fr !=null && fr instanceof MatchPlayersFragmentImproved){
            MatchPlayersFragmentImproved ifr = (MatchPlayersFragmentImproved) fr;
            intent.putExtra(AddPlayersActivity.EXTRA_OMIT_DATA, ifr.awayAdapter.getData());
            ifr.startActivityForResult(intent, 1);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(new String[]{"Add Player to " + getArguments().getString(ARG_HOME_NAME), "Add Player to " + getArguments().getString(ARG_AWAY_NAME)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                homeClick();
                                break;
                            case 1:
                                awayClick();
                                break;
                        }
                        dismiss();
                    }
                });
        return builder.create();
    }
}
