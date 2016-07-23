package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.InsertTeamDialog;

/** Dialog that allows to delete team
 * Created by Vaclav on 11. 4. 2016.
 */
public class TeamsDialog extends DialogFragment {

    public TeamsDialog(){}

    public static final String ARG_ID = "arg_id";
    public static final String ARG_POSITION = "arg_pos";

    public static TeamsDialog newInstance(long teamId, int position){
        TeamsDialog fragment = new TeamsDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, teamId);
        args.putInt(ARG_POSITION, position);
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
                                InsertTeamDialog insertTeamDialog = InsertTeamDialog.newInstance(getArguments().getLong(ARG_ID), false, SquashInsertTeamDialog.class);
                                if (getTargetFragment() != null)
                                    insertTeamDialog.setTargetFragment(getTargetFragment(), 0);
                                insertTeamDialog.show(getFragmentManager(), "edit_team_dialog");
                                dialog.dismiss();
                                break;
                            case 1:
                                Intent intent = TeamService.newStartIntent(TeamService.ACTION_DELETE, getContext());
                                intent.putExtra(TeamService.EXTRA_ID, getArguments().getLong(ARG_ID));
                                intent.putExtra(TeamService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
                                getContext().startService(intent);
                                if (getTargetFragment() != null)
                                    getTargetFragment().onActivityResult(0, 0, null);
                                break;
                            default:break;
                        }
                    }
                });
        return builder.create();
    }
}
