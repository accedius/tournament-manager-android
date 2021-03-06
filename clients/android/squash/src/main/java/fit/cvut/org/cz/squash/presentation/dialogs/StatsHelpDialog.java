package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;

/**
 * Created by kevin on 7.1.2017.
 */

public class StatsHelpDialog extends DialogFragment {

    public static StatsHelpDialog newInstance() {
        StatsHelpDialog dialog = new StatsHelpDialog();
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.dialog_help);
        return builder.create();
    }
}
