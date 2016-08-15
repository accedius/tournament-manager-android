package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

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
    private static final String SECOND_ID = "second_id";

    protected DialogInterface.OnClickListener supplyListener() { return null;}

    public static EditDeleteDialog newInstance( long id, long otherId ){
        EditDeleteDialog fragment = new EditDeleteDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(SECOND_ID, otherId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        String[] items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete) };

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment fr = getTargetFragment();
                if(fr != null) {
                    if( fr instanceof HockeyTeamsListFragment ) {
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
                                getContext().startService(intent);
                                dialog.dismiss();
                                break;
                        }
                    } else if( fr instanceof HockeyTournamentsListFragment) {
                        switch ( which ) {
                            case 0:
                                Intent intent0 = CreateTournamentActivity.newStartIntent(getContext(), getArguments().getLong(ARG_ID), getArguments().getLong(SECOND_ID));
                                startActivity( intent0 );
                                break;
                            case 1:
                                Intent intent1 = TournamentService.newStartIntent(TournamentService.ACTION_DELETE, getContext());
                                intent1.putExtra( TournamentService.EXTRA_ID, getArguments().getLong(ARG_ID));
                                getContext().startService( intent1 );
                                break;
                        }
                        dialog.dismiss();
                    }
                }
            }
        });

        return builder.create();
    }
}
