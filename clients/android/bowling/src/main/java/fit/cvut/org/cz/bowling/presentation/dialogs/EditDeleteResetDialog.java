package fit.cvut.org.cz.bowling.presentation.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.presentation.activities.CreateMatchActivity;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.MatchService;

/**
 * Created by soucem10 on 17. 1. 2019.
 */
public class EditDeleteResetDialog extends DialogFragment {
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = CreateMatchActivity.newStartIntent(
                                getContext(),
                                getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID),
                                getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = MatchService.newStartIntent(MatchService.ACTION_DELETE, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
                        getContext().startService(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = MatchService.newStartIntent(MatchService.ACTION_RESET, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
                        getContext().startService(intent);
                        break;
                    }
                }
                dialog.dismiss();
            }
        };
    }

    public static EditDeleteResetDialog newInstance(long matchId, long tourId, String name){
        EditDeleteResetDialog fragment = new EditDeleteResetDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tourId);
        args.putString(ExtraConstants.EXTRA_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete), getActivity().getString(R.string.reset) };
        builder.setItems(items, supplyListener());

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }
}
