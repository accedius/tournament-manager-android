package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.business.serialization.helpers.FilesHelper;

/**
 * Created by kevin on 14. 4. 2016.
 */
public class AddCompetitionDialog extends DialogFragment {
    private String package_name;
    private String activity_create_competition;
    private String package_service;
    private String sport_context;
    private View view;

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent();
                        intent.setClassName(package_name, activity_create_competition);
                        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    }
                    case 1: {
                        dialog.dismiss();
                        break;
                    }
                    case 2: {
                        if (FilesHelper.getFileNames(sport_context).length == 0) {
                            dialog.dismiss();
                            Snackbar.make(view, fit.cvut.org.cz.tmlibrary.R.string.no_competition_file_error, Snackbar.LENGTH_LONG).show();
                            break;
                        }
                        DialogFragment importFileDialog = ImportFileDialog.newInstance(package_name, sport_context, package_service);
                        importFileDialog.show(getFragmentManager(), "IMPORT_FILE");
                        dialog.dismiss();
                        break;
                    }
                }
            }
        };
    }

    public static AddCompetitionDialog newInstance(View view, String package_name, String sport_context, String activity_create_competition, String package_service) {
        AddCompetitionDialog fragment = new AddCompetitionDialog();
        fragment.view = view;
        fragment.package_name = package_name;
        fragment.sport_context = sport_context;
        fragment.activity_create_competition = activity_create_competition;
        fragment.package_service = package_service;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(
                new String[]{getResources().getString(R.string.competition_create_manually),
                        getResources().getString(R.string.competition_import_server),
                        getResources().getString(R.string.competition_import_file)},
                supplyListener());

        int sportContextIdentifier = getResources().getIdentifier(sport_context, "string", getContext().getPackageName());
        builder.setTitle(getResources().getString(sportContextIdentifier));
        return builder.create();
    }
}
