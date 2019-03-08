package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.presentation.activities.CreateMatchActivity;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.MatchService;

/** Dialog that allows user to Edit, Reset or Delete match
 * Created by Vaclav on 11. 4. 2016.
 */
public class MatchesDialog extends DialogFragment {
    public MatchesDialog(){}

    public static MatchesDialog newInstance(long id, long tournamentId, int position, String title) {
        MatchesDialog fragment = new MatchesDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tournamentId);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putString(ExtraConstants.EXTRA_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(new String[]{
                getString(fit.cvut.org.cz.tmlibrary.R.string.edit),
                getString(fit.cvut.org.cz.tmlibrary.R.string.delete),
                getString(fit.cvut.org.cz.tmlibrary.R.string.reset)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:{
                                Intent intent = CreateMatchActivity.newStartIntent(getContext(), getArguments().getLong(ExtraConstants.EXTRA_ID), getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                                startActivity(intent);
                                break;
                            }
                            case 1:{
                                Intent intent =  MatchService.newStartIntent(MatchService.ACTION_DELETE_MATCH, getContext());
                                intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
                                intent.putExtra(ExtraConstants.EXTRA_POSITION, getArguments().getInt(ExtraConstants.EXTRA_POSITION));
                                getContext().startService(intent);
                                break;
                            }
                            case 2:{
                                Intent intent =  MatchService.newStartIntent(MatchService.ACTION_RESET_MATCH, getContext());
                                intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
                                getContext().startService(intent);
                                break;
                            }
                        }
                        if (getTargetFragment() != null) getTargetFragment().onActivityResult(1, 1, null);
                        dialog.dismiss();
                    }
                });

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }
}
