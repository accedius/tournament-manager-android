package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.TeamComplexStatsFragment;

public class EditDeleteFrameDialog extends DialogFragment {
    long participantId;
    int position;
    boolean isLast;
    Fragment targetFragment;
    FrameOverview frameOverview;

    public static EditDeleteFrameDialog newInstance (FrameOverview frameOverview, int position, boolean isLast, long participantId) {
        EditDeleteFrameDialog dialog = new EditDeleteFrameDialog();
        Bundle args = new Bundle();
        args.putParcelable(ExtraConstants.EXTRA_FRAME_OVERVIEW, frameOverview);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putBoolean(ExtraConstants.EXTRA_BOOLEAN_LAST_FRAME, isLast);
        args.putLong(ExtraConstants.EXTRA_PARTICIPANT_ID, participantId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        targetFragment = getTargetFragment();
        frameOverview = getArguments().getParcelable(ExtraConstants.EXTRA_FRAME_OVERVIEW);
        position = getArguments().getInt(ExtraConstants.EXTRA_POSITION);
        isLast = getArguments().getBoolean(ExtraConstants.EXTRA_BOOLEAN_LAST_FRAME);
        participantId = getArguments().getLong(ExtraConstants.EXTRA_PARTICIPANT_ID);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        String[] items;
        if(isLast) {
            items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete) };
        } else {
            items = new String[]{ getActivity().getString(R.string.edit)};
        }
        builder.setItems( items, supplyListener());

        int positionForTitle = position + 1;
        String title = getResources().getString(R.string.frame_num) + positionForTitle;
        builder.setTitle(title);
        return builder.create();
    }

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        EditFrameDialog dialogEdit = EditFrameDialog.newInstance(frameOverview, position);
                        dialogEdit.setTargetFragment(targetFragment, getTargetRequestCode());
                        dialogEdit.show(getFragmentManager(), "dialogEdit");
                        dialog.dismiss();
                        break;
                    case 1:
                        Intent data = new Intent();
                        data.putExtra(ExtraConstants.EXTRA_POSITION, position);
                        getTargetFragment().onActivityResult(TeamComplexStatsFragment.RequestCodes.REMOVE_FRAME, Activity.RESULT_OK, data);
                        dialog.dismiss();
                        break;
                }
                dialog.dismiss();
            }
        };
    }
}