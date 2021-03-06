package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

/**
 * Dialog for edit and delete.
 */
public class EditDeleteDialog extends DialogFragment {
    private String activityCreatePlayer;
    private long playerId;
    private int position;
    private List<Player> players;
    private String packageName;

    /**
     * DialogInterface.OnClickListener getter.
     * @return on click listener
     */
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent();
                        intent.setClassName(packageName, ExtraConstants.ACTIVITY_CREATE_PLAYER);
                        intent.putExtra(ExtraConstants.EXTRA_ID, playerId);
                        intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, new ArrayList<>(players));
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_DELETE, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_ID, playerId);
                        intent.putExtra(ExtraConstants.EXTRA_POSITION, position);
                        getContext().startService(intent);
                    }
                }
                dialog.dismiss();
            }
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(
                new String[]{
                    getResources().getString(R.string.edit),
                    getResources().getString(R.string.delete)},
                supplyListener());

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }

    /**
     * EditDeleteDialog creator.
     * @param packageName name of package
     * @param playerId id of Player
     * @param position position in list
     * @param players list of all Players
     * @return EditDeleteDialog
     */
    public static EditDeleteDialog newInstance(String packageName, long playerId, int position, List<Player> players) {
        EditDeleteDialog dialog = new EditDeleteDialog();
        dialog.packageName = packageName;
        dialog.playerId = playerId;
        dialog.position = position;
        dialog.players = players;
        return dialog;
    }
}
