package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.PointConfigurationService;

/**
 * Dialog, that occurs, when the point configuration in Configure Points Fragment is held (long click). Edits point configuration's number of places/sides + widens/shortens it's place point's list or deletes this point configuration
 */
public class PointConfigurationsDialog extends EditDeleteDialog {

    @Override
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        InsertPointConfigurationDialog insertPointConfigurationDialog = InsertPointConfigurationDialog.newInstance(getArguments().getLong(ExtraConstants.EXTRA_ID), false, BowlingInsertPointConfigurationDialog.class);
                        insertPointConfigurationDialog.setTargetFragment(getTargetFragment(), 0);
                        insertPointConfigurationDialog.show(getFragmentManager(), "tag2");
                        dialog.dismiss();
                        break;
                    case 1:
                        Intent intent = PointConfigurationService.newStartIntent(PointConfigurationService.ACTION_DELETE, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
                        intent.putExtra(ExtraConstants.EXTRA_POSITION, getArguments().getInt(ExtraConstants.EXTRA_POSITION));
                        getContext().startService(intent);
                        dialog.dismiss();
                        break;
                }
                dialog.dismiss();
            }
        };
    }

    public static PointConfigurationsDialog newInstance (long id, int position, String title) {
        PointConfigurationsDialog dialog = new PointConfigurationsDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putString(ExtraConstants.EXTRA_TITLE, title);
        dialog.setArguments(args);
        return dialog;
    }
}
