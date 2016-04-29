package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import fit.cvut.org.cz.hockey.R;

/**
 * Created by atgot_000 on 12. 4. 2016.
 */
public class EditDeleteDialog extends DialogFragment {

    protected DialogInterface.OnClickListener supplyListener() { return null;}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        String[] items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete) };

        builder.setItems( items, supplyListener() );

        return builder.create();
    }
}
