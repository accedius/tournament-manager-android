package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyTeamsListFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyTournamentsListFragment;
import fit.cvut.org.cz.hockey.presentation.services.TeamService;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;

/**
 * Created by atgot_000 on 12. 4. 2016.
 */
public class EditDeleteDialog extends DialogFragment {

    private static final String ARG_ID = "arg_id";
    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_POSITION = "arg_position";
    private static final String SECOND_ID = "second_id";

    protected DialogInterface.OnClickListener supplyListener() { return null;}

    public static EditDeleteDialog newInstance( long id, long otherId, int position, String name ){
        EditDeleteDialog fragment = new EditDeleteDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(SECOND_ID, otherId);
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO rozdělit na více stejných dialogů, zdědily by stejný newInstance, měly by rozdílný onCreateDialog
    // alespoň by nebylo nutné používat instanceof
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        String[] items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete) };
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment fr = getTargetFragment();
                if (fr != null) {
                    if (fr instanceof HockeyTeamsListFragment) {
                        switch (which) {
                            case 0:
                                InsertTeamDialog insertTeamDialog = InsertTeamDialog.newInstance(getArguments().getLong(ARG_ID), false, HockeyInsertTeamDialog.class);
                                insertTeamDialog.setTargetFragment(fr, 0);
                                insertTeamDialog.show(getFragmentManager(), "tag2");
                                dialog.dismiss();
                                break;
                            case 1:
                                Intent intent = TeamService.newStartIntent(TeamService.ACTION_DELETE, getContext());
                                intent.putExtra(TeamService.EXTRA_ID, getArguments().getLong(ARG_ID));
                                intent.putExtra(TeamService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
                                getContext().startService(intent);
                                dialog.dismiss();
                                break;
                        }
                    } else if (fr instanceof HockeyTournamentsListFragment) {
                        switch (which) {
                            case 0: {
                                Intent intent = CreateTournamentActivity.newStartIntent(getContext(), getArguments().getLong(ARG_ID), getArguments().getLong(SECOND_ID));
                                startActivity(intent);
                                break;
                            }
                            case 1: {
                                Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_DELETE, getContext());
                                intent.putExtra(TournamentService.EXTRA_ID, getArguments().getLong(ARG_ID));
                                intent.putExtra(TeamService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
                                getContext().startService(intent);
                                break;
                            }
                        }
                        dialog.dismiss();
                    }
                }
            }
        });

        builder.setTitle(getArguments().getString(ARG_TITLE));
        return builder.create();
    }
}
