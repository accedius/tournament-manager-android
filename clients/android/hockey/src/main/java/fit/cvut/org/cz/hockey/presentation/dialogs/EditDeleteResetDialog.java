package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.activities.CreateMatchActivity;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;

/**
 * Created by atgot_000 on 25. 4. 2016.
 */
public class EditDeleteResetDialog extends DialogFragment {

    private static final String ARG_MATCH_ID = "arg_match_id";
    private static final String ARG_TOUR_ID = "arg_tour_id";
    private static final String ARG_TITLE = "arg_title";

    protected DialogInterface.OnClickListener supplyListener() { return null;}

    public static EditDeleteResetDialog newInstance( long matchId, long tourId, String name ){
        EditDeleteResetDialog fragment = new EditDeleteResetDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_MATCH_ID, matchId);
        args.putLong(ARG_TOUR_ID, tourId);
        args.putString(ARG_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        String[] items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete), getActivity().getString(R.string.restart) };
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch ( which ) {
                    case 0: {
                        Intent intent = CreateMatchActivity.newStartIntent(getContext(), getArguments().getLong(ARG_MATCH_ID), getArguments().getLong(ARG_TOUR_ID));
                        startActivity( intent );
                        break;
                    }
                    case 1: {
                        Intent intent = MatchService.newStartIntent(MatchService.ACTION_DELETE, getContext());
                        intent.putExtra( MatchService.EXTRA_ID, getArguments().getLong(ARG_MATCH_ID) );
                        getContext().startService( intent );
                        break;
                    }
                    case 2: {
                        Intent intent = MatchService.newStartIntent( MatchService.ACTION_RESTART, getContext() );
                        intent.putExtra( MatchService.EXTRA_ID, getArguments().getLong(ARG_MATCH_ID) );
                        getContext().startService( intent );
                        break;
                    }
                }
                dialog.dismiss();
            }
        });

        builder.setTitle(getArguments().getString(ARG_TITLE));
        return builder.create();
    }
}
