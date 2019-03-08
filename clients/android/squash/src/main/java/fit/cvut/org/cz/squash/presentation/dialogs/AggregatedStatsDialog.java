package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;

/**
 * Dialog that allows to remove player from competition or tournament
 * Created by Vaclav on 11. 4. 2016.
 */
public class AggregatedStatsDialog extends DialogFragment {
    public AggregatedStatsDialog(){}

    public static AggregatedStatsDialog newInstance(long id, long playerId, int position, String action, String name){
        AggregatedStatsDialog fragment = new AggregatedStatsDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);
        args.putLong(ExtraConstants.EXTRA_PLAYER_ID, playerId);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putString(ExtraConstants.EXTRA_ACTION, action);
        args.putString(ExtraConstants.EXTRA_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(new String[]{getString(R.string.delete)},
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:{
                            Intent intent = PlayerService.newStartIntent(getArguments().getString(ExtraConstants.EXTRA_ACTION), getContext());
                            intent.putExtra(ExtraConstants.EXTRA_POSITION, getArguments().getInt(ExtraConstants.EXTRA_POSITION));
                            intent.putExtra(ExtraConstants.EXTRA_PLAYER_ID, getArguments().getLong(ExtraConstants.EXTRA_PLAYER_ID));
                            intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
                            getContext().startService(intent);
                            break;
                        }
                    }
                    if (getTargetFragment() != null) {
                        getTargetFragment().onActivityResult(3, SelectableListActivity.RESULT_OK, null);
                    }
                    dialog.dismiss();
                }
            });

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }
}
