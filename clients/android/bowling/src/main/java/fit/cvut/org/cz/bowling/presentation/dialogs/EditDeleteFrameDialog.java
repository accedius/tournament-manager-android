package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;

public class EditDeleteFrameDialog extends DialogFragment {
    int position;
    boolean isLast;
    Fragment targetFragment;
    FrameOverview frameOverview;

    public static EditDeleteFrameDialog newInstance (FrameOverview frameOverview, int position, boolean isLast, String title) {
        EditDeleteFrameDialog dialog = new EditDeleteFrameDialog();
        Bundle args = new Bundle();
        args.putParcelable(ExtraConstants.EXTRA_FRAME_OVERVIEW, frameOverview);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putBoolean(ExtraConstants.EXTRA_BOOLEAN_LAST_FRAME, isLast);
        args.putString(ExtraConstants.EXTRA_TITLE, title);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        targetFragment = getTargetFragment();
        frameOverview = getArguments().getParcelable(ExtraConstants.EXTRA_FRAME_OVERVIEW);
        position = getArguments().getInt(ExtraConstants.EXTRA_POSITION);
        isLast = getArguments().getBoolean(ExtraConstants.EXTRA_BOOLEAN_LAST_FRAME);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        String[] items;
        if(isLast) {
            items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete) };
        } else {
            items = new String[]{ getActivity().getString(R.string.edit)};
        }
        builder.setItems( items, supplyListener());

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        EditFrameDialog dialogEdit = EditFrameDialog.newInstance(position);
                        dialogEdit.setTargetFragment(targetFragment, getTargetRequestCode());
                        dialogEdit.show(getFragmentManager(), "dialogEdit");
                        dialog.dismiss();
                        break;
                    case 1:
                        frameOverviews.remove(position);
                        if (getTargetFragment() != null){
                            int resultCodeUpdateFrom = position >= 2 ? position - 2 : 0 ;
                            getTargetFragment().onActivityResult(REQUEST_CODE_UPDATE_LOCALLY, resultCodeUpdateFrom, null);
                        }
                        dialog.dismiss();
                        break;
                }
                dialog.dismiss();
            }
        };
    }
}