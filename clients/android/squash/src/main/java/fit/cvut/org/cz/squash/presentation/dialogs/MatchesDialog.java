package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.CreateMatchActivity;
import fit.cvut.org.cz.squash.presentation.services.MatchService;

/** Dialog that allows user to Edit, Reset or Delete match
 * Created by Vaclav on 11. 4. 2016.
 */
public class MatchesDialog extends DialogFragment {

    public MatchesDialog(){}

    public static final String ARG_ID = "arg_id";
    public static final String ARG_TOURNAMENT_ID = "arg_tournament_id";
    public static final String ARG_POSITION = "arg_pos";
    public static final String ARG_TITLE = "arg_title";

    public static MatchesDialog newInstance(long id, long tournamentId, int position, String title) {
        MatchesDialog fragment = new MatchesDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(ARG_TOURNAMENT_ID, tournamentId);
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_TITLE, title);
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
                getString(fit.cvut.org.cz.tmlibrary.R.string.restart)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:{
                                Intent intent = CreateMatchActivity.newStartIntent(getContext(), getArguments().getLong(ARG_ID), getArguments().getLong(ARG_TOURNAMENT_ID));
                                startActivity(intent);
                                break;
                            }
                            case 1:{
                                Intent intent =  MatchService.newStartIntent(MatchService.ACTION_DELETE_MATCH, getContext());
                                intent.putExtra(MatchService.EXTRA_ID, getArguments().getLong(ARG_ID));
                                intent.putExtra(MatchService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
                                getContext().startService(intent);
                                break;
                            }
                            case 2:{
                                Intent intent =  MatchService.newStartIntent(MatchService.ACTION_RESET_MATCH, getContext());
                                intent.putExtra(MatchService.EXTRA_ID, getArguments().getLong(ARG_ID));
                                getContext().startService(intent);
                                break;
                            }
                        }
                        if (getTargetFragment() != null) getTargetFragment().onActivityResult(1, 1, null);
                        dialog.dismiss();
                    }
                });

        builder.setTitle(getArguments().getString(ARG_TITLE));
        return builder.create();
    }
}
