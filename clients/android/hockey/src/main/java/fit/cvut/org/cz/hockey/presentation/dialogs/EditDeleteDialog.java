package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;

/**
 * Created by atgot_000 on 12. 4. 2016.
 */
abstract public class EditDeleteDialog extends DialogFragment {
    abstract protected DialogInterface.OnClickListener supplyListener();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete) };
        builder.setItems(items, supplyListener());

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }
}
