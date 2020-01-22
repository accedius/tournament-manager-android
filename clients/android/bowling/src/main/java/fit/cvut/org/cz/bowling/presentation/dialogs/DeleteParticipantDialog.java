package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;

public class DeleteParticipantDialog extends DialogFragment {
    public static DeleteParticipantDialog newInstance (long participantId, int position, String name) {
        DeleteParticipantDialog dialog = new DeleteParticipantDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(ExtraConstants.EXTRA_PARTICIPANT_ID, participantId);
        bundle.putInt(ExtraConstants.EXTRA_POSITION, position);
        bundle.putString(ExtraConstants.EXTRA_TITLE, name);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = new String[]{ getActivity().getString(R.string.delete) };
        builder.setItems(items, supplyListener());

        builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
        return builder.create();
    }

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        if(getTargetFragment() != null) {
                            Intent intent = new Intent();
                            long participantId = getArguments().getLong(ExtraConstants.EXTRA_PARTICIPANT_ID);
                            int position = getArguments().getInt(ExtraConstants.EXTRA_POSITION);
                            intent.putExtra(ExtraConstants.EXTRA_PARTICIPANT_ID, participantId);
                            intent.putExtra(ExtraConstants.EXTRA_POSITION, position);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }
                        break;
                    }
                }
                dialog.dismiss();
            }
        };
    }
}
