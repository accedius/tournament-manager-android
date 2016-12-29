package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.fragments.AddPlayersFragment;
import fit.cvut.org.cz.squash.presentation.fragments.MatchPlayersFragment;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/** Dialog that specifies to which team players should be added in match rosters
 * Created by Vaclav on 11. 4. 2016.
 */
public class SelectTeamDialog extends DialogFragment {
    public SelectTeamDialog(){}

    public static SelectTeamDialog newInstance(long homeId, long awayId, String homeName, String awayName, long matchId) {
        SelectTeamDialog fragment = new SelectTeamDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_HOME_ID, homeId);
        args.putLong(ExtraConstants.EXTRA_AWAY_ID, awayId);
        args.putString(ExtraConstants.EXTRA_HOME_NAME, homeName);
        args.putString(ExtraConstants.EXTRA_AWAY_NAME, awayName);
        args.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    private void homeClick(){
        ArrayList<Player> omitStats = ((MatchPlayersFragment)getTargetFragment()).getOmitPlayers();
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_MATCH, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
        intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT, omitStats);
        getTargetFragment().startActivityForResult(intent, MatchPlayersFragment.REQUEST_HOME);
    }
    private void awayClick(){
        ArrayList<Player> omitStats = ((MatchPlayersFragment)getTargetFragment()).getOmitPlayers();
        Intent intent = AddPlayersActivity.newStartIntent(getContext(), AddPlayersFragment.OPTION_MATCH, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
        intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT, omitStats);
        getTargetFragment().startActivityForResult(intent, MatchPlayersFragment.REQUEST_AWAY);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(new String[]{getString(R.string.add_player_to) + " " + getArguments().getString(ExtraConstants.EXTRA_HOME_NAME), getString(R.string.add_player_to) + " " + getArguments().getString(ExtraConstants.EXTRA_AWAY_NAME)},
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
