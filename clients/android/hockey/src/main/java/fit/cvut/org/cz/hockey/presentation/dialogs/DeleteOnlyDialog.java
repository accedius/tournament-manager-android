package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.hockey.presentation.services.PlayerService;

/**
 * Created by atgot_000 on 29. 4. 2016.
 */
public class DeleteOnlyDialog extends DialogFragment {
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (getArguments().getLong(ExtraConstants.EXTRA_COMP_ID) != -1) {
                        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_DELETE_PLAYER_FROM_COMPETITION, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_PLAYER_ID, getArguments().getLong(ExtraConstants.EXTRA_PLAYER_ID));
                        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_COMP_ID));
                        getContext().startService(intent);
                    } else {
                        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_DELETE_PLAYER_FROM_TOURNAMENT, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_PLAYER_ID, getArguments().getLong(ExtraConstants.EXTRA_PLAYER_ID));
                        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                        getContext().startService(intent);
                    }
                }
                dismiss();
            }
        };
    }

    public static DeleteOnlyDialog newInstance(long playerId, long compId, long tourId, String name){
        DeleteOnlyDialog fragment = new DeleteOnlyDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_PLAYER_ID, playerId);
        args.putLong(ExtraConstants.EXTRA_COMP_ID, compId);
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tourId);
        args.putString(ExtraConstants.EXTRA_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = new String[]{ getActivity().getString(R.string.delete) };
        builder.setItems(items, supplyListener());

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }
}
