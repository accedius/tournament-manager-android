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
 * Created by kevin on 14. 4. 2016.
 */
public class EditDialog extends DialogFragment {
    private String activity_create_competition;
    private String package_name;
    private String sport_context;
    private long competitionId;

    protected DialogInterface.OnClickListener supplyListener() {
        return  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent();
                        intent.setClassName(package_name, activity_create_competition);
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionId);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                        startActivity(intent);
                        break;
                    }
                }
                dialog.dismiss();
            }
        };
    }

    public static EditDialog newInstance(String package_name, String activity_create_competition, long competitionId, String sport_context) {
        EditDialog dialog = new EditDialog();
        dialog.package_name = package_name;
        dialog.activity_create_competition = activity_create_competition;
        dialog.competitionId = competitionId;
        dialog.sport_context = sport_context;
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
