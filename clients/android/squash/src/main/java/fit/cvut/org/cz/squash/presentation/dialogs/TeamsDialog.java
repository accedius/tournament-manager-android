package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;

/** Dialog that allows to delete team
 * Created by Vaclav on 11. 4. 2016.
 */
public class TeamsDialog extends DialogFragment {
    public TeamsDialog(){}

    public static TeamsDialog newInstance(long teamId, int position, String name) {
        TeamsDialog fragment = new TeamsDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, teamId);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putString(ExtraConstants.EXTRA_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(new String[]{getString(R.string.edit), getString(R.string.delete)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                InsertTeamDialog insertTeamDialog = InsertTeamDialog.newInstance(getArguments().getLong(ExtraConstants.EXTRA_ID), false, SquashInsertTeamDialog.class);
                                if (getTargetFragment() != null)
                                    insertTeamDialog.setTargetFragment(getTargetFragment(), 0);
                                insertTeamDialog.show(getFragmentManager(), "edit_team_dialog");
                                dialog.dismiss();
                                break;
                            case 1:
                                Intent intent = TeamService.newStartIntent(TeamService.ACTION_DELETE, getContext());
                                intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));
                                intent.putExtra(ExtraConstants.EXTRA_POSITION, getArguments().getInt(ExtraConstants.EXTRA_POSITION));
                                getContext().startService(intent);
                                if (getTargetFragment() != null)
                                    getTargetFragment().onActivityResult(0, 0, null);
                                break;
                            default:break;
                        }
                    }
                });

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }
}
