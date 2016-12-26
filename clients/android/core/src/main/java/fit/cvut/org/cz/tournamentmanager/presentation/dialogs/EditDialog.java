package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by kevin on 14. 4. 2016.
 */
public class EditDialog extends DialogFragment {
    public static final String ARG_TITLE = "arg_title";

    protected DialogInterface.OnClickListener supplyListener() { return null;};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(
                new String[]{getResources().getString(R.string.edit)},
                supplyListener());

        builder.setTitle(getArguments().getString(ARG_TITLE));
        return builder.create();
    }
}
