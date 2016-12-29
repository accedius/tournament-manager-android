package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.activities.CreateMatchActivity;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;

/**
 * Created by atgot_000 on 22. 4. 2016.
 */
public class AddMatchDialog extends DialogFragment {
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = CreateMatchActivity.newStartIntent(getContext(), getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = MatchService.newStartIntent(MatchService.ACTION_GENERATE_ROUND, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_TOUR_ID, getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                        getContext().startService(intent);
                        break;
                    }
                }
                dialog.dismiss();
            }
        };
    }

    public static AddMatchDialog newInstance(long tourId){
        AddMatchDialog fragment = new AddMatchDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tourId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = new String[]{ getActivity().getString(fit.cvut.org.cz.tmlibrary.R.string.add_single_match), getActivity().getString(R.string.add_round) };

        builder.setItems(items, supplyListener());

        builder.setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.add_match));
        return builder.create();
    }
}

