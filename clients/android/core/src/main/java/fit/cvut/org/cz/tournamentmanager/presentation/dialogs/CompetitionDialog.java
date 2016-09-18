package fit.cvut.org.cz.tournamentmanager.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.CompetitionsListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.services.CompetitionService;

/**
 * Created by kevin on 14. 4. 2016.
 */
public class CompetitionDialog extends DialogFragment {
    public static final String ARG_POSITION = "arg_position";
    public static final String ACTION_DELETE_COMPETITION = "action_delete_competition";

    private Long competition_id;
    private String competition_name;
    private String package_name;
    private String activity_create_competition;

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment fr = getTargetFragment();
                if (fr != null && fr instanceof CompetitionsListFragment) {
                    switch (which) {
                        case 0: {
                            Intent intent = new Intent();
                            intent.setClassName(package_name, activity_create_competition);
                            Bundle b = new Bundle();
                            b.putLong(CrossPackageComunicationConstants.EXTRA_ID, competition_id);
                            intent.putExtras(b);
                            startActivity(intent);
                            dialog.dismiss();
                            break;
                        }
                        case 1: {
                            Intent intent = CompetitionService.getStartIntent(ACTION_DELETE_COMPETITION, package_name, competition_id.toString(), getContext());
                            intent.putExtra(CompetitionService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
                            getContext().startService(intent);
                            dialog.dismiss();
                            break;
                        }
                    }
                }
            }
        };
    }

    public static CompetitionDialog newInstance(long competitionId, int position, String name, String package_name, String activity_create_competition) {
        CompetitionDialog fragment = new CompetitionDialog();
        fragment.competition_id = competitionId;
        fragment.competition_name = name;
        fragment.package_name = package_name;
        fragment.activity_create_competition = activity_create_competition;

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
                new String[]{
                        getResources().getString(R.string.edit),
                        getResources().getString(R.string.delete)},
                supplyListener());

        builder.setTitle(competition_name);
        return builder.create();
    }
}
