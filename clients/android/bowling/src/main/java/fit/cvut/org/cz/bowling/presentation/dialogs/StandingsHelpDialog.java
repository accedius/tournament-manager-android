package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.bowling.R;

/**
 * Help dialog that is showed, when the help button (in a shape of question mark) is pressed in standings fragment of tournament's menu, describes the standings abbreviations used
 */
public class StandingsHelpDialog extends DialogFragment {

    public static StandingsHelpDialog newInstance() {
        StandingsHelpDialog dialog = new StandingsHelpDialog();
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.standings_dialog_help);
        return builder.create();
    }
}

