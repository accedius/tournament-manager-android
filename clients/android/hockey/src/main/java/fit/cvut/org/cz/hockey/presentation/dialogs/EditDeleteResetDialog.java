package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by atgot_000 on 25. 4. 2016.
 */
public class EditDeleteResetDialog extends DialogFragment {

    protected DialogInterface.OnClickListener supplyListener() { return null;}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        String[] items = new String[]{ "Edit", "Delete", "Restart" };

        builder.setItems( items, supplyListener() );

        return builder.create();
    }
}
