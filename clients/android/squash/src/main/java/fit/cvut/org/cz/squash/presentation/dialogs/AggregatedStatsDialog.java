package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;

/**
 * Dialog that allows to remove player from competition or tournament
 * Created by Vaclav on 11. 4. 2016.
 */
public class AggregatedStatsDialog extends DialogFragment {

    public AggregatedStatsDialog(){}

    public static final String ARG_ID = "arg_id";
    public static final String ARG_PLAYER_ID = "player_id";
    public static final String ARG_ACTION = "arg_action";
    public static final String ARG_POSITION = "arg_pos";
    public static final String ARG_TITLE = "arg_title";

    public static AggregatedStatsDialog newInstance(long id, long playerId, int position, String action, String name){
        AggregatedStatsDialog fragment = new AggregatedStatsDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(ARG_PLAYER_ID, playerId);
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_ACTION, action);
        args.putString(ARG_TITLE, name);
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
                            Intent intent = PlayerService.newStartIntent(getArguments().getString(ARG_ACTION), getContext());
                            intent.putExtra(PlayerService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
                            intent.putExtra(PlayerService.EXTRA_PLAYER_ID, getArguments().getLong(ARG_PLAYER_ID));
                            intent.putExtra(PlayerService.EXTRA_ID, getArguments().getLong(ARG_ID));
                            getContext().startService(intent);
                            break;
                        }
                    }
                    if (getTargetFragment() != null){
                        getTargetFragment().onActivityResult(3, SelectableListActivity.RESULT_OK, null);
                    }
                    dialog.dismiss();
                }
            });

        builder.setTitle(getArguments().getString(ARG_TITLE));
        return builder.create();
    }
}
