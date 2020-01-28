package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.helpers.FilesHelper;

/**
 * Dialog for adding new competition.
 */
public class AddCompetitionDialog extends DialogFragment {
    private String packageName;
    private String activityCreateCompetition;
    private String packageService;
    private String sportContext;
    private View view;

    /**
     * DialogInterface.OnClickListener getter.
     * @return on click listener
     */
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent();
                        intent.setClassName(packageName, activityCreateCompetition);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    }
                    case 1: {/*
                        dialog.dismiss();
                        break;
                    }
                    case 2: {*/
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            Snackbar.make(view, fit.cvut.org.cz.tmlibrary.R.string.no_storage_permission, Snackbar.LENGTH_LONG).show();
                            break;
                        }
                        if (FilesHelper.getFileNames(sportContext).length == 0) {
                            dialog.dismiss();
                            Snackbar.make(view, fit.cvut.org.cz.tmlibrary.R.string.no_competition_file_error, Snackbar.LENGTH_LONG).show();
                            break;
                        }
                        DialogFragment importFileDialog = ImportFileDialog.newInstance(packageName, sportContext, packageService);
                        importFileDialog.show(getFragmentManager(), "IMPORT_FILE");
                        dialog.dismiss();
                        break;
                    }
                }
            }
        };
    }

    /**
     * Dialog creator.
     * @param view View
     * @param packageName name of package
     * @param sportContext name of sport
     * @param activityCreateCompetition path to activity for create and edit competition
     * @param packageService path to package exported service
     * @return AddCompetitionDialog
     */
    public static AddCompetitionDialog newInstance(View view, String packageName, String sportContext, String activityCreateCompetition, String packageService) {
        AddCompetitionDialog fragment = new AddCompetitionDialog();
        fragment.view = view;
        fragment.packageName = packageName;
        fragment.sportContext = sportContext;
        fragment.activityCreateCompetition = activityCreateCompetition;
        fragment.packageService = packageService;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(
                new String[]{getResources().getString(R.string.competition_create_manually),
                        //getResources().getString(R.string.competition_import_server),
                        getResources().getString(R.string.competition_import_file)},
                supplyListener());

        int sportContextIdentifier = getResources().getIdentifier(sportContext, "string", getContext().getPackageName());
        builder.setTitle(getResources().getString(sportContextIdentifier));
        return builder.create();
    }
}
