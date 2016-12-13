package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.ConflictAdapter;

/**
 * Created by kevin on 24.10.2016.
 */
public class FileConflictsDialog extends DialogFragment {
    private View view;
    private RecyclerView recyclerView;
    private AbstractListAdapter adapter;
    private ArrayList<Conflict> conflicts;

    public static FileConflictsDialog newInstance(View v, ArrayList<Conflict> conflicts) {
        FileConflictsDialog fragment = new FileConflictsDialog();
        fragment.view = v;
        fragment.conflicts = conflicts;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle("There is a conflict!");
        View fragmentView = inflater.inflate(R.layout.dialog_conflict, null);
        recyclerView = (RecyclerView) fragmentView.findViewById(fit.cvut.org.cz.tmlibrary.R.id.recycler_view);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        builder.setView(fragmentView);

        final AlertDialog fileConflictsDialog = builder.create();
        adapter.swapData(conflicts);

        final Button keepLocalButton = (Button) fragmentView.findViewById(R.id.keep_local);
        final Button takeFileButton = (Button) fragmentView.findViewById(R.id.take_file);
        keepLocalButton.setOnClickListener(getKeepLocalListener(fileConflictsDialog));
        takeFileButton.setOnClickListener(getTakeFileListener(fileConflictsDialog));
        return fileConflictsDialog;
    }

    private AbstractListAdapter getAdapter() {
        return new ConflictAdapter(getActivity());
    }

    private View.OnClickListener getKeepLocalListener(final AlertDialog fileConflictsDialog) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                for(Object o : adapter.getData()) {
                    Conflict c = (Conflict) o;
                    Log.d("IMPORT", c.getTitle()+" "+c.getAction());
                };
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
