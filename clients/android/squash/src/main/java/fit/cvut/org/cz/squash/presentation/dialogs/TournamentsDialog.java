package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;

/** Dialog that allows to edit or delete tournament
 * Created by Vaclav on 11. 4. 2016.
 */
public class TournamentsDialog extends DialogFragment {
    public TournamentsDialog(){}

    public static TournamentsDialog newInstance(long compId, long tourId, int position, String name) {
        TournamentsDialog fragment = new TournamentsDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_COMP_ID, compId);
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tourId);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putString(ExtraConstants.EXTRA_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(new String[]{getString(R.string.edit), getString(R.string.delete)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:{
                                Intent intent = CreateTournamentActivity.newStartIntent(
                                        getContext(),
                                        getArguments().getLong(ExtraConstants.EXTRA_COMP_ID),
                                        getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                                startActivity(intent);
                                dialog.dismiss();
                                break;
                            }
                            case 1:{
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
                });

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }
}
