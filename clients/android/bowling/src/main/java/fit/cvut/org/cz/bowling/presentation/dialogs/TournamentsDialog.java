package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.bowling.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.TournamentService;

public class TournamentsDialog extends EditDeleteDialog {
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = CreateTournamentActivity.newStartIntent(getContext(), getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID), getArguments().getLong(ExtraConstants.EXTRA_COMP_ID));
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    }
                    case 1: {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        Dialog dialog  = (Dialog) dialogInterface;
                                        Context context = dialog.getContext();
                                        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_DELETE, context);
                                        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                                        intent.putExtra(ExtraConstants.EXTRA_POSITION, getArguments().getInt(ExtraConstants.EXTRA_POSITION));
                                        context.startService(intent);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                                dialogInterface.dismiss();
                            }
                        };
                        dialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.tournament_delete))
                                .setPositiveButton(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.yes), dialogClickListener)
                                .setNegativeButton(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.no), dialogClickListener)
                                .show();
                        break;
                    }
                }
            }
        };
    }

    public static TournamentsDialog newInstance(long id, long otherId, int position, String name){
        TournamentsDialog fragment = new TournamentsDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);
        args.putLong(ExtraConstants.EXTRA_COMP_ID, otherId);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putString(ExtraConstants.EXTRA_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }
}
