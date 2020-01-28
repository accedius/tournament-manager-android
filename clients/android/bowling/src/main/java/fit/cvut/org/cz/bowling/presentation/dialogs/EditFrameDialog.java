package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.constraints.ConstraintsConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;

public class EditFrameDialog extends DialogFragment {
    TextView roll1, roll2, roll3;
    long participantId;
    long playerId;
    String playerName;
    int position;
    FrameOverview frameOverview = null;
    int frameNumber;
    boolean toCreate;
    int maxFrames = ConstraintsConstants.tenPinMatchParticipantMaxFrames;
    int maxFrameScore = ConstraintsConstants.tenPinFrameMaxScore;

    /**
     * To create frame
     * @param toCreate
     * @param participantId
     * @param playerId
     * @param playerName
     * @return
     */
    public static EditFrameDialog newInstance(boolean toCreate, long participantId, long playerId, String playerName) {
        EditFrameDialog dialog = new EditFrameDialog();
        Bundle args = new Bundle();
        args.putBoolean(ExtraConstants.EXTRA_BOOLEAN_FRAME_TO_CREATE, toCreate);
        args.putLong(ExtraConstants.EXTRA_PARTICIPANT_ID, participantId);
        args.putLong(ExtraConstants.EXTRA_PLAYER_ID, playerId);
        args.putString(ExtraConstants.EXTRA_PLAYER_NAME, playerName);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * To update frame
     * @param frameOverview
     * @param arrayListPosition
     * @return
     */
    public static EditFrameDialog newInstance(FrameOverview frameOverview, int arrayListPosition) {
        EditFrameDialog dialog = new EditFrameDialog();
        Bundle args = new Bundle();
        args.putParcelable(ExtraConstants.EXTRA_FRAME_OVERVIEW, frameOverview);
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
        toCreate = getArguments().getBoolean(ExtraConstants.EXTRA_BOOLEAN_FRAME_TO_CREATE, false);
        participantId = getArguments().getLong(ExtraConstants.EXTRA_PARTICIPANT_ID);
        playerId = getArguments().getLong(ExtraConstants.EXTRA_PLAYER_ID);
        playerName = getArguments().getString(ExtraConstants.EXTRA_PLAYER_NAME);
        frameNumber = position + 1;
        if(toCreate) {
            frameOverview = new FrameOverview();
            frameOverview.setParticipantId(participantId);
            frameOverview.setFrameNumber( (byte) frameNumber);
            frameOverview.setPlayerId(playerId);
            frameOverview.setPlayerName(playerName);
        } else {
            frameOverview = getArguments().getParcelable(ExtraConstants.EXTRA_FRAME_OVERVIEW);
        }
        if (frameNumber == maxFrames)
            v.findViewById(R.id.throw_3_input_layout).setVisibility(View.VISIBLE);
        builder.setTitle(getResources().getString(R.string.frame_num) + frameNumber + ": " + frameOverview.getPlayerName());

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
                    int pinsInRoll1 = 0;
                    int pinsInRoll2 = 0;
                    int pinsInRoll3 = 0;
                    if (!roll1.getText().toString().isEmpty() ) {
                        pinsInRoll1 = Integer.parseInt(roll1.getText().toString());
                    }

                    if (!roll2.getText().toString().isEmpty() ) {
                        pinsInRoll2 = Integer.parseInt(roll2.getText().toString());
                    }

                    if (frameNumber == maxFrames && !roll3.getText().toString().isEmpty() ) {
                        pinsInRoll3 = Integer.parseInt(roll3.getText().toString());
                    }

                    ArrayList<Byte> rolls = new ArrayList<>();

                    if( pinsInRoll1 > maxFrameScore || pinsInRoll1 < 0){
                        TextInputLayout til = getDialog().findViewById(R.id.throw_1_input_layout);
                        til.setError(getResources().getString(R.string.flf_roll_format_violated) + " " + maxFrameScore + "!");
                        returnToken = true;
                    } else rolls.add((byte) pinsInRoll1);

                    if( pinsInRoll2 > maxFrameScore || pinsInRoll2 < 0 ) {
                        TextInputLayout til = getDialog().findViewById(R.id.throw_2_input_layout);
                        til.setError(getResources().getString(R.string.flf_roll_format_violated) + " " + maxFrameScore + "!");
                        returnToken = true;
                    } else if (frameNumber == maxFrames || pinsInRoll1 != maxFrameScore) {
                        rolls.add((byte) pinsInRoll2);
                    }

                    if (frameNumber == maxFrames) {
                        if (pinsInRoll3 > maxFrameScore || pinsInRoll3 < 0) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_3_input_layout);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated) + " " + maxFrameScore + "!");
                            returnToken = true;
                        } else if (pinsInRoll1 + pinsInRoll2 >= maxFrameScore) {
                            rolls.add((byte) pinsInRoll3);
                        }
                    }

                    if(returnToken)
                        return;

                    if ( pinsInRoll1 + pinsInRoll2 > maxFrameScore && ( frameNumber != maxFrames || pinsInRoll1 != maxFrameScore) ) {
                        TextInputLayout til = getDialog().findViewById(R.id.throw_2_input_layout);
                        til.setError(getResources().getString(R.string.flf_regular_frame_too_many_pins_error) + " " + maxFrameScore + "!");
                        returnToken = true;
                    }

                    if (pinsInRoll1 + pinsInRoll2 < maxFrameScore
                            && frameNumber == maxFrames
                            && pinsInRoll3 > 0) {
                        TextInputLayout til = getDialog().findViewById(R.id.throw_2_input_layout);
                        til.setError(getResources().getString(R.string.flf_last_frame_invalid_extra_roll_error));
                        returnToken = true;
                    }

                    if(returnToken)
                        return;

                    frameOverview.setRolls(rolls);
                    byte score = (byte)(pinsInRoll1 + pinsInRoll2 + pinsInRoll3);
                    int positionUpdateFrom = position >= 2 ? position - 2 : 0 ;
                    frameOverview.setFrameScore(score);
                    Intent data = new Intent();
                    data.putExtra(ExtraConstants.EXTRA_FRAME_OVERVIEW, frameOverview);
                    data.putExtra(ExtraConstants.EXTRA_POSITION, position);
                    data.putExtra(ExtraConstants.EXTRA_POSITION_UPDATE_FROM, positionUpdateFrom);
                    if (getTargetFragment() != null)
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
                    dialog.dismiss();
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(frameOverview != null && frameOverview.getRolls()!=null && !frameOverview.getRolls().isEmpty() ) {
            String previousRoll1, previousRoll2, previousRoll3;
            int playedRolls = frameOverview.getRolls().size();
            previousRoll1 = String.format(Locale.getDefault(), "%d", frameOverview.getRolls().get(0));
            roll1.setText(previousRoll1);
            if(playedRolls > 1) {
                previousRoll2 = String.format(Locale.getDefault(), "%d", frameOverview.getRolls().get(1));
                roll2.setText(previousRoll2);
            }
            if(playedRolls > 2) {
                previousRoll3 = String.format(Locale.getDefault(), "%d", frameOverview.getRolls().get(2));
                roll3.setText(previousRoll3);
            }
        }
        roll1.setFocusableInTouchMode(true);
        roll1.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
