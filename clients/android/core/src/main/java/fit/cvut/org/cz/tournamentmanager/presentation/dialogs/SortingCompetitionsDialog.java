package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Dialog for choose column for order of Competitions.
 */
public class SortingCompetitionsDialog extends DialogFragment {
    private DialogInterface.OnClickListener listener = null;

    /**
     * DialogInterface.OnClickListener getter.
     * @return on click listener
     */
    protected DialogInterface.OnClickListener supplyListener() { return listener;}

    /**
     * SortingCompetitionsDialog creator
     * @return SortingCompetitionsDialog instance
     */
    public static SortingCompetitionsDialog newInstance() {
        SortingCompetitionsDialog fragment = new SortingCompetitionsDialog();
        return fragment;
    }

    /**
     * OnClickListener setter
     * @param onClickListener listener
     */
    public void setListener(DialogInterface.OnClickListener onClickListener) {
        listener = onClickListener;
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

        builder.setTitle(getResources().getString(R.string.sort_competitions));
        return builder.create();
    }
}
