package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.activities.CreateMatchActivity;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;

/**
 * Created by atgot_000 on 22. 4. 2016.
 */
public class AddMatchDialog extends DialogFragment {

    private static final String ARG_TOUR_ID = "arg_tour_id";

    protected DialogInterface.OnClickListener supplyListener() { return null;}

    public static AddMatchDialog newInstance( long tourId ){
        AddMatchDialog fragment = new AddMatchDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_TOUR_ID, tourId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        String[] items = new String[]{ getActivity().getString(R.string.add_single_match), getActivity().getString(R.string.add_round) };

        builder.setItems( items, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch ( which )
                {
                    case 0:
                    {
                        Intent intent = CreateMatchActivity.newStartIntent(getContext(), getArguments().getLong(ARG_TOUR_ID));

                        startActivity(intent);
                        break;
                    }
                    case 1:
                    {
                        Intent intent = MatchService.newStartIntent(MatchService.ACTION_GENERATE_ROUND, getContext());
                        intent.putExtra( MatchService.EXTRA_TOUR_ID, getArguments().getLong(ARG_TOUR_ID) );

                        getContext().startService( intent );
                        break;
                    }
                }
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}

