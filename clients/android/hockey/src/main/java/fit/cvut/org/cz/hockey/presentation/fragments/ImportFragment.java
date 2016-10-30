package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

import fit.cvut.org.cz.hockey.presentation.services.HockeyService;
import fit.cvut.org.cz.tmlibrary.business.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;

/**
 * Created by kevin on 30.10.2016.
 */
public class ImportFragment extends fit.cvut.org.cz.tmlibrary.presentation.fragments.ImportFragment {

    public static ImportFragment newInstance() {
        Bundle args = new Bundle();
        ImportFragment fragment = new ImportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onConfirmClick() {
        HashMap<String, String> playersModified = new HashMap<>();
        for (Conflict c : (ArrayList<Conflict>)conflictsAdapter.getData()) {
            playersModified.put(c.getTitle(), c.getAction());
        }

        Intent intent = new Intent(getActivity(), HockeyService.class);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_JSON, jsonContent);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sportContext);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_ACTION, CrossPackageCommunicationConstants.ACTION_IMPORT_FILE_COMPETITION);
        Bundle b = new Bundle();
        b.putSerializable(CrossPackageCommunicationConstants.EXTRA_CONFLICTS, playersModified);
        intent.putExtras(b);
        getActivity().startService(intent);
        getActivity().finish();
    }
}
