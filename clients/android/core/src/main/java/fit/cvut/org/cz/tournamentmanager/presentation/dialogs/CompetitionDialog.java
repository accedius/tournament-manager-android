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
public class CompetitionDialog extends DialogFragment {
    private Long competitionId;
    private String competitionName;
    private String packageName;
    private String activityCreateCompetition;
    private String packageService;
    private String sportContext;

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent();
                        intent.setClassName(packageName, packageService);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                        intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_GET_COMPETITION_SERIALIZED);
                        intent.putExtra(CrossPackageConstants.EXTRA_PACKAGE, packageName);
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionId);
                        getContext().startService(intent);
                        dialog.dismiss();
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent();
                        intent.setClassName(packageName, activityCreateCompetition);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionId);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent();
                        intent.setClassName(packageName, packageService);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                        intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_DELETE_COMPETITION);
                        intent.putExtra(CrossPackageConstants.EXTRA_POSITION, getArguments().getInt(ExtraConstants.EXTRA_POSITION));
                        intent.putExtra(CrossPackageConstants.EXTRA_PACKAGE, packageName);
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionId);
                        getContext().startService(intent);
                        dialog.dismiss();
                        break;
                    }
                }
            }
        };
    }

    public static CompetitionDialog newInstance(long competitionId, int position, String name, String package_name, String sport_context, String activity_create_competition, String package_service) {
        CompetitionDialog fragment = new CompetitionDialog();
        fragment.competitionId = competitionId;
        fragment.competitionName = name;
        fragment.packageName = package_name;
        fragment.sportContext = sport_context;
        fragment.activityCreateCompetition = activity_create_competition;
        fragment.packageService = package_service;

        Bundle args = new Bundle();
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(
                new String[]{getResources().getString(R.string.export_to_file),
                        getResources().getString(R.string.edit),
                        getResources().getString(R.string.delete)},
                supplyListener());

        builder.setTitle(competitionName);
        return builder.create();
    }
}
