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
import fit.cvut.org.cz.tournamentmanager.presentation.services.CompetitionService;

/**
 * Created by kevin on 14. 4. 2016.
 */
public class CompetitionDialog extends DialogFragment {
    public static final String ARG_POSITION = "arg_position";

    private Long competition_id;
    private String competition_name;
    private String package_name;
    private String activity_create_competition;
    private String package_service;
    private String sport_context;

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent();
                        intent.setClassName(package_name, package_service);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                        intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_GET_COMPETITION_SERIALIZED);
                        intent.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competition_id);
                        getContext().startService(intent);
                        dialog.dismiss();
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent();
                        intent.setClassName(package_name, activity_create_competition);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competition_id);
                        startActivity(intent);
                        dialog.dismiss();
                        break;
                    }
                    case 2: {
                        Intent intent = CompetitionService.getStartIntent(CompetitionService.ACTION_DELETE_COMPETITION, package_name, sport_context, competition_id.toString(), getContext());
                        intent.putExtra(CompetitionService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
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
        fragment.competition_id = competitionId;
        fragment.competition_name = name;
        fragment.package_name = package_name;
        fragment.sport_context = sport_context;
        fragment.activity_create_competition = activity_create_competition;
        fragment.package_service = package_service;

        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
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

        builder.setTitle(competition_name);
        return builder.create();
    }
}
