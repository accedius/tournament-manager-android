package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.fragments.AddPlayersFragment;
import fit.cvut.org.cz.squash.presentation.fragments.MatchPlayersFragment;

/** Dialog that specifies to which team players should be added in match rosters
 * Created by Vaclav on 11. 4. 2016.
 */
public class SelectTeamDialog extends DialogFragment {
    public SelectTeamDialog(){}
    public static final String ARG_HOME_ID = "arg_home_id";
    public static final String ARG_AWAY_ID = "arg_away_id";
    public static final String ARG_HOME_NAME = "arg_home_name";
    public static final String ARG_AWAY_NAME = "arg_away_name";

    public static SelectTeamDialog newInstance(long homeId, long awayId, String homeName, String awayName) {
        SelectTeamDialog fragment = new SelectTeamDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_AWAY_ID, awayId);
        args.putLong(ARG_HOME_ID, homeId);
        args.putString(ARG_HOME_NAME, homeName);
        args.putString(ARG_AWAY_NAME, awayName);
        fragment.setArguments(args);
        return fragment;
    }

    private void homeClick(){
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_MATCH, getArguments().getLong(ARG_HOME_ID));
        Fragment fr = getTargetFragment();
        if (fr !=null && fr instanceof MatchPlayersFragment) {
            MatchPlayersFragment ifr = (MatchPlayersFragment) fr;
            intent.putExtra(AddPlayersActivity.EXTRA_OMIT_DATA, ifr.homeAdapter.getData());
            ifr.startActivityForResult(intent, 0);
        }
    }
    private void awayClick(){
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_MATCH, getArguments().getLong(ARG_AWAY_ID));
        Fragment fr = getTargetFragment();
        if (fr !=null && fr instanceof MatchPlayersFragment) {
            MatchPlayersFragment ifr = (MatchPlayersFragment) fr;
            intent.putExtra(AddPlayersActivity.EXTRA_OMIT_DATA, ifr.awayAdapter.getData());
            ifr.startActivityForResult(intent, 1);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(new String[]{getString(R.string.add_player_to) + " " + getArguments().getString(ARG_HOME_NAME), getString(R.string.add_player_to) + " " + getArguments().getString(ARG_AWAY_NAME)},
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

        builder.setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.add_player));
        return builder.create();
    }
}
