package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import fit.cvut.org.cz.hockey.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;

/**
 * Created by atgot_000 on 12. 4. 2016.
 */
public class TeamsDialog extends EditDeleteDialog {

    private static final String ARG_ID = "arg_id";
    private static final String ARG_POSITION = "arg_position";

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        InsertTeamDialog insertTeamDialog = InsertTeamDialog.newInstance(getArguments().getLong(ARG_ID), false, HockeyInsertTeamDialog.class);
                        insertTeamDialog.setTargetFragment(getTargetFragment(), 0);
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
                dialog.dismiss();
            }
        };
    }

    public static TeamsDialog newInstance( long id, int position, String name ){
        TeamsDialog fragment = new TeamsDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }
}
