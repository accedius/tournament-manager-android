package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.io.File;

import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
import fit.cvut.org.cz.tournamentmanager.business.serialization.helpers.FilesHelper;

/**
 * Created by kevin on 14. 4. 2016.
 */
public class ImportFileDialog extends DialogFragment {
    private String package_name;
    private String package_service;
    private String sport_context;

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* Send request for deserialize specified file */
                File competitionFile = FilesHelper.getFiles(sport_context).get(which);
                String fileContent = FilesHelper.loadFileContent(competitionFile);

                Intent intent = new Intent();
                intent.setClassName(package_name, package_service);
                intent.putExtra(CrossPackageCommunicationConstants.EXTRA_ACTION, CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_IMPORT_INFO);
                intent.putExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE, package_name);
                intent.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
                intent.putExtra(CrossPackageCommunicationConstants.EXTRA_JSON, fileContent);
                getContext().startService(intent);
                dialog.dismiss();
            }
        };
    }

    public static ImportFileDialog newInstance(String package_name, String sport_context, String package_service) {
        ImportFileDialog fragment = new ImportFileDialog();
        fragment.package_name = package_name;
        fragment.sport_context = sport_context;
        fragment.package_service = package_service;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(FilesHelper.getFileNames(sport_context), supplyListener());
        builder.setTitle(fit.cvut.org.cz.tmlibrary.R.string.choose_file);
        return builder.create();
    }
}
