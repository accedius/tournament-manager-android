package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.presentation.adapters.FrameOverviewAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

public class FrameListFragment extends AbstractListFragment<FrameOverview> {

    protected static List<FrameOverview> frameOverviews = new ArrayList<>();

    @Override
    protected AbstractListAdapter getAdapter() {
        return new FrameOverviewAdapter(getResources()) {
            @Override
            protected void setOnClickListeners(View v, int position, final long playerId, final byte frameNumber, List<Integer> rolls, String playerName, int currentScore) {
                super.setOnClickListeners(v, position, playerId, frameNumber, rolls, playerName, currentScore);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = 0;
                        while(index < frameOverviews.size()) {
                            FrameOverview frameOw = frameOverviews.get(index);
                            if (frameOw.getFrameNumber() == frameNumber && frameOw.getPlayerId() == playerId)
                                break;
                            ++index;
                        }
                        EditFrameListDialog dialog = EditFrameListDialog.newInstance(index);
                        dialog.show(getFragmentManager(), "dialog");
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true; //should deleting frames be possible?
                    }
                });
            }
        };
    }

    @Override
    protected String getDataKey() {
        return null;
    }

    @Override
    public void askForData() {

    }

    @Override
    protected boolean isDataSourceWorking() {
        return false;
    }

    @Override
    protected void registerReceivers() {

    }

    @Override
    protected void unregisterReceivers() {

    }

    public static class EditFrameListDialog extends DialogFragment {
        TextView roll1, roll2, roll3;
        int position;
        FrameOverview frameOverview = null;

        public static EditFrameListDialog newInstance(int arrayListPosition) {
            EditFrameListDialog dialog = new EditFrameListDialog();
            Bundle args = new Bundle();
            args.putInt(ExtraConstants.EXTRA_SELECTED, arrayListPosition);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton(fit.cvut.org.cz.tmlibrary.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //empty, because must be overridden in onResume in order not to close on bad input and instead check for constraints (https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked)
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_frame_throws, null);
            builder.setView(v);
            roll1 = v.findViewById(R.id.throw_1_input);
            roll2 = v.findViewById(R.id.throw_2_input);
            roll3 = v.findViewById(R.id.throw_3_input);
            position = getArguments().getInt(ExtraConstants.EXTRA_SELECTED);
            if(position >= 0 && position < frameOverviews.size() ) {
                frameOverview = frameOverviews.get(position);
                if (frameOverview.getFrameNumber() == 10)
                    v.findViewById(R.id.throw_3_input_layout).setVisibility(View.VISIBLE);
            }
            builder.setTitle(getResources().getString(R.string.frame_num) + frameOverview.getFrameNumber() + ": " + frameOverview.getPlayerName());
            return builder.create();
        }

        @Override
        public void onResume() {
            super.onResume();
            final AlertDialog dialog = (AlertDialog) getDialog();
            if(dialog != null) {
                Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean returnToken = false;
                        if (roll1.getText().toString().isEmpty() ) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_1_input);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated));
                            returnToken = true;
                        }

                        if (roll2.getText().toString().isEmpty() ) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated));
                            returnToken = true;
                        }

                        if (frameOverview.getFrameNumber() == 10 && roll3.getText().toString().isEmpty() ) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_3_input);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated));
                            returnToken = true;
                        }

                        if(returnToken) {
                            return;
                        }

                        int pinsInRoll1 = Integer.parseInt(roll1.getText().toString());
                        int pinsInRoll2 = Integer.parseInt(roll2.getText().toString());
                        int pinsInRoll3 = 0;
                        ArrayList<Integer> rolls = new ArrayList<>();

                        if( pinsInRoll1 > 10 || pinsInRoll1 < 0){
                            TextInputLayout til = getDialog().findViewById(R.id.throw_1_input);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated));
                            returnToken = true;
                        } else rolls.add(pinsInRoll1);
                        if( pinsInRoll2 > 10 || pinsInRoll2 < 0 ) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated));
                            returnToken = true;
                        } else rolls.add(pinsInRoll2);
                        if (frameOverview.getFrameNumber() == 10) {
                            pinsInRoll3 = Integer.parseInt(roll3.getText().toString());
                            if (pinsInRoll3 > 10 || pinsInRoll3 < 0) {
                                TextInputLayout til = getDialog().findViewById(R.id.throw_3_input);
                                til.setError(getResources().getString(R.string.flf_roll_format_violated));
                                returnToken = true;
                            } else rolls.add(pinsInRoll3);
                        }

                        if (pinsInRoll1 + pinsInRoll2 > 10 && frameOverview.getFrameNumber() != 10) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input);
                            til.setError(getResources().getString(R.string.flf_regular_frame_too_many_pins_error));
                            returnToken = true;
                        }

                        if (pinsInRoll1 + pinsInRoll2 < 10
                                && frameOverview.getFrameNumber() == 10
                                && pinsInRoll3 > 0) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input);
                            til.setError(getResources().getString(R.string.flf_last_frame_invalid_extra_roll_error));
                            returnToken = true;
                        }

                        if(returnToken)
                            return;

                        frameOverview.setRolls(rolls);

                        if (getTargetFragment() != null)
                            getTargetFragment().onActivityResult(0, 1, null);
                        dialog.dismiss();
                    }
                });
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            roll1.setFocusableInTouchMode(true);
            roll1.requestFocus();
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
