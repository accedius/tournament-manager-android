package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;

/**
 * Created by atgot_000 on 12. 4. 2016.
 */
public class TeamsDialog extends EditDeleteDialog {
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        InsertTeamDialog insertTeamDialog = InsertTeamDialog.newInstance(getArguments().getLong(ExtraConstants.EXTRA_ID), false, BowlingInsertTeamDialog.class);
                        insertTeamDialog.setTargetFragment(getTargetFragment(), 0);
                        insertTeamDialog.show(getFragmentManager(), "tag2");
                        dialog.dismiss();
                        break;
                    case 1:
                        Intent intent = TeamService.newStartIntent(TeamService.ACTION_DELETE, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
                        intent.putExtra(ExtraConstants.EXTRA_POSITION, getArguments().getInt(ExtraConstants.EXTRA_POSITION));
                        getContext().startService(intent);
                        dialog.dismiss();
                        break;
                }
                dialog.dismiss();
            }
        };
    }

    public static TeamsDialog newInstance(long id, int position, String name){
        TeamsDialog fragment = new TeamsDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putString(ExtraConstants.EXTRA_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }
}
