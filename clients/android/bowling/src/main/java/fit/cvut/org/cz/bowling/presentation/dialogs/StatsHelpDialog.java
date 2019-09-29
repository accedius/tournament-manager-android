package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import fit.cvut.org.cz.bowling.R;

/**
 * Help dialog that is showed, when the help button (in a shape of question mark) is pressed in players fragment of tournament's menu, describes the player's statistics abbreviations used
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
