package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by kevin on 24.10.2016.
 */
public class FileConflictsDialog extends DialogFragment {
    private View view;

    public static FileConflictsDialog newInstance(View v) {
        FileConflictsDialog fragment = new FileConflictsDialog();
        fragment.view = v;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle("There is a conflict!");
        View v = inflater.inflate(R.layout.dialog_conflict, null);
        builder.setView(v);

        final AlertDialog fileConflictsDialog = builder.create();

        final Button keepLocalButton = (Button) v.findViewById(R.id.keep_local);
        final Button takeFileButton = (Button) v.findViewById(R.id.take_file);
        keepLocalButton.setOnClickListener(getKeepLocalListener(fileConflictsDialog));
        takeFileButton.setOnClickListener(getTakeFileListener(fileConflictsDialog));
        return fileConflictsDialog;
    }

    private View.OnClickListener getKeepLocalListener(final AlertDialog fileConflictsDialog) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                fileConflictsDialog.dismiss();
                Snackbar.make(view, "Local data has been kept!", Snackbar.LENGTH_LONG).show();
            }
        };
    }

    private View.OnClickListener getTakeFileListener(final AlertDialog fileConflictsDialog) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                fileConflictsDialog.dismiss();
                // TODO import only players from file
                Snackbar.make(view, "Changes from file has been applied!", Snackbar.LENGTH_LONG).show();
            }
        };
    }
}
