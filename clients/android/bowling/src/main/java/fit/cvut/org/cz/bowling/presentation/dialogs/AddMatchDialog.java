package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Date;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.presentation.activities.CreateMatchActivity;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.NewBowlingMatchFragment;
import fit.cvut.org.cz.bowling.presentation.services.MatchService;

import static java.lang.Integer.parseInt;

/**
 * Dialog, that occurs, when the + button in matches fragment is clicked
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
                    case 2: {
                        final Activity a = getActivity();
                        Intent intent = MatchService.newStartIntent(MatchService.ACTION_GENERATE_BY_LANES, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_TOUR_ID, getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                        intent.putExtra(ExtraConstants.EXTRA_LANES, 2);
                        getContext().startService(intent);
                        a.finish();
                        /*
                        Log.i("info", "Generate");
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle("Generate matches by lines");
                        alertDialog.setMessage("Enter number of lines");
                        final EditText input = new EditText(getActivity());
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        int lanes = Integer.parseInt(input.getText().toString()) ;

                                    }
                                });

                        alertDialog.setNegativeButton("Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();*/
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

    public boolean generateMatchByLines(int lines,Activity activity, Context context)
    {
        try {
            //sort list

            while (lines > 0) {
                generateMatch(activity,context);
                lines--;
            }
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public void generateMatch(Activity activity, Context context)
    {
        Log.d("Generete Match","generateMatach -> přidávám tour");
        long tourId = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID);
        Date date = new Date(System.currentTimeMillis());
        fit.cvut.org.cz.tmlibrary.data.entities.Match match = new fit.cvut.org.cz.tmlibrary.data.entities.Match(-1,tourId,date,false, "", 1, 1);
        fit.cvut.org.cz.tmlibrary.data.entities.Match BowlingMatch = new fit.cvut.org.cz.tmlibrary.data.entities.Match(match);

        Intent intent;
        if (BowlingMatch.getId() == -1) {
            intent = MatchService.newStartIntent(MatchService.ACTION_CREATE, activity);
            intent.setAction(MatchService.ACTION_CREATE);
        } else {
            intent = MatchService.newStartIntent(MatchService.ACTION_UPDATE, activity);
            intent.setAction(MatchService.ACTION_UPDATE);
        }
        intent.putExtra(ExtraConstants.EXTRA_MATCH, BowlingMatch);

        context.startService(intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = new String[]{ getActivity().getString(fit.cvut.org.cz.tmlibrary.R.string.add_single_match), getActivity().getString(R.string.add_round), getActivity().getString(R.string.generate_matches) };

        builder.setItems(items, supplyListener());

        builder.setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.add_match));
        return builder.create();
    }
}
