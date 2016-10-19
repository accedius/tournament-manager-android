package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;

import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.business.serialization.FilesHelper;

/**
 * Created by kevin on 14. 4. 2016.
 */
public class ImportFileDialog extends DialogFragment {
    private String package_name;
    private String stats_service;
    private String sport_context;

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* Deserialize for specified file */
                File competitionFile = FilesHelper.getFiles(sport_context).get(which);
                String fileContent = FilesHelper.loadFileContent(competitionFile);
                ServerCommunicationItem item = new Gson().fromJson(fileContent, ServerCommunicationItem.class);
                Log.d("IMPORT", item.getSyncData().get("name")+ " chosen for import");
            }
        };
    }

    // TODO rename stats_service to SPORT_SERVICE ... in all application
    public static ImportFileDialog newInstance(String package_name, String sport_context, String stats_service) {
        ImportFileDialog fragment = new ImportFileDialog();
        fragment.package_name = package_name;
        fragment.sport_context = sport_context;
        fragment.stats_service = stats_service;
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
