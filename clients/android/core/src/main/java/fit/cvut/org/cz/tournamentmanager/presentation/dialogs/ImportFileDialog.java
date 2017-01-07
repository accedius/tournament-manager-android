package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.io.File;

import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tournamentmanager.presentation.helpers.FilesHelper;

/**
 * Dialog for display files possible for import.
 */
public class ImportFileDialog extends DialogFragment {
    private String packageName;
    private String packageService;
    private String sportContext;

    /**
     * DialogInterface.OnClickListener getter.
     * @return on click listener
     */
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* Send request for deserialize specified file */
                File competitionFile = FilesHelper.getFiles(sportContext).get(which);
                String fileContent = FilesHelper.loadFileContent(competitionFile);

                Intent intent = new Intent();
                intent.setClassName(packageName, packageService);
                intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_GET_COMPETITION_IMPORT_INFO);
                intent.putExtra(CrossPackageConstants.EXTRA_PACKAGE, packageName);
                intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                intent.putExtra(CrossPackageConstants.EXTRA_JSON, fileContent);
                getContext().startService(intent);
                dialog.dismiss();
            }
        };
    }

    /**
     * ImportFileDialog creator.
     * @param packageName name of package
     * @param sportContext name of sport
     * @param packageService path to exported service
     * @return ImportFileDialog instance
     */
    public static ImportFileDialog newInstance(String packageName, String sportContext, String packageService) {
        ImportFileDialog fragment = new ImportFileDialog();
        fragment.packageName = packageName;
        fragment.sportContext = sportContext;
        fragment.packageService = packageService;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(FilesHelper.getFileNames(sportContext), supplyListener());
        builder.setTitle(fit.cvut.org.cz.tmlibrary.R.string.choose_file);
        return builder.create();
    }
}
