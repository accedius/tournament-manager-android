package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.communication.ExtraConstants;

/**
 * Dialog for edit only.
 */
public class EditDialog extends DialogFragment {
    private String activityCreateCompetition;
    private String packageName;
    private String sportContext;
    private long competitionId;

    protected DialogInterface.OnClickListener supplyListener() {
        return  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent();
                        intent.setClassName(packageName, activityCreateCompetition);
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionId);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                        startActivity(intent);
                        break;
                    }
                }
                dialog.dismiss();
            }
        };
    }

    public static EditDialog newInstance(String packageName, String activityCreateCompetition, long competitionId, String sportContext) {
        EditDialog dialog = new EditDialog();
        dialog.packageName = packageName;
        dialog.activityCreateCompetition = activityCreateCompetition;
        dialog.competitionId = competitionId;
        dialog.sportContext = sportContext;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(
                new String[]{getResources().getString(R.string.edit)},
                supplyListener());

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }
}
