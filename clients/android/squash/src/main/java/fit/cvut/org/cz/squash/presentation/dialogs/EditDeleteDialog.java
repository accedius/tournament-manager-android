package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Vaclav on 11. 4. 2016.
 */
public class EditDeleteDialog extends DialogFragment {

    protected DialogInterface.OnClickListener supplyListener() { return null;};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(new String[]{"Edit", "Delete"}, supplyListener());

        return builder.create();
    }
}
