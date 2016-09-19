package fit.cvut.org.cz.tmlibrary.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.tmlibrary.R;

/**
 * Created by kevin on 14. 4. 2016.
 */
public class SortingTournamentsDialog extends DialogFragment {
    private DialogInterface.OnClickListener listener = null;

    protected DialogInterface.OnClickListener supplyListener() { return listener;}

    public static SortingTournamentsDialog newInstance() {
        SortingTournamentsDialog fragment = new SortingTournamentsDialog();
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
                        getResources().getString(R.string.sort_by_name),
                        getResources().getString(R.string.sort_by_start_date),
                        getResources().getString(R.string.sort_by_end_date)},
                supplyListener());

        builder.setTitle(getResources().getString(R.string.sort_tournaments));
        return builder.create();
    }
}
