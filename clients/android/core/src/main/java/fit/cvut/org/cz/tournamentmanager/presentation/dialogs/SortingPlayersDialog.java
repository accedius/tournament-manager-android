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
public class SortingPlayersDialog extends DialogFragment {
    private DialogInterface.OnClickListener listener = null;

    protected DialogInterface.OnClickListener supplyListener() { return listener;}

    public static SortingPlayersDialog newInstance() {
        SortingPlayersDialog fragment = new SortingPlayersDialog();
        return fragment;
    }

    public void setListener(DialogInterface.OnClickListener l) {
        listener = l;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(
                new String[]{
                        getResources().getString(R.string.sort_by_player_name),
                        getResources().getString(R.string.sort_by_email)},
                supplyListener());

        builder.setTitle(getResources().getString(R.string.sort_competitions));
        return builder.create();
    }
}
