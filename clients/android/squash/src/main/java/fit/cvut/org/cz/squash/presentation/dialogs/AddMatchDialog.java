package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.MatchService;

/**Dialog, that allows user to specify if he wants to add match manually or to add round of matches
 * Created by Vaclav on 11. 4. 2016.
 */
public class AddMatchDialog extends DialogFragment {
    public AddMatchDialog(){}

    public static AddMatchDialog newInstance(long id){
        AddMatchDialog fragment = new AddMatchDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(new String[]{getString(R.string.add_single_match), getString(R.string.add_round)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:{
                                Intent intent = MatchService.newStartIntent(MatchService.ACTION_CAN_ADD_MATCH, getContext());
                                intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
                                getContext().startService(intent);
                                break;
                            }
                            case 1:{
                                Intent intent = MatchService.newStartIntent(MatchService.ACTION_GENERATE_ROUND, getContext());
                                intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
                                getContext().startService(intent);
                                break;
                            }
                        }
                        if (getTargetFragment() != null) getTargetFragment().onActivityResult(0, 0, null);
                        dialog.dismiss();
                    }
                });

        builder.setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.add_match));
        return builder.create();
    }
}
