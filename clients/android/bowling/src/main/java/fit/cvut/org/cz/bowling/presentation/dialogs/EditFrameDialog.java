package fit.cvut.org.cz.bowling.presentation.dialogs;

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
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;

public class EditFrameDialog extends DialogFragment {
    TextView roll1, roll2, roll3;
    int position;
    FrameOverview frameOverview = null;
    int frameNumber;
    boolean toCreate = false;
    boolean initialInput;

    public static EditFrameDialog newInstance(int arrayListPosition) {
        EditFrameDialog dialog = new EditFrameDialog();
        Bundle args = new Bundle();
        args.putInt(ExtraConstants.EXTRA_SELECTED, arrayListPosition);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initialInput = true;

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
        frameNumber = position + 1;
        if(position >= 0 && position < frameOverviews.size() ) {
            frameOverview = frameOverviews.get(position);
        } else if (position == frameOverviews.size()) {
            toCreate = true;
            frameOverview = new FrameOverview();
            frameOverview.setFrameNumber( (byte) frameNumber);
            if(tournamentType.equals(TournamentTypes.individuals())){
                Player player = participantPlayers.get(0);
                frameOverview.setPlayerName(player.getName());
                frameOverview.setPlayerId(player.getId());
                playerSpinner.setVisibility(View.GONE);
            } else {
                //TODO if needed
            }
        }
        if (frameNumber == maxFrames)
            v.findViewById(R.id.throw_3_input_layout).setVisibility(View.VISIBLE);
        builder.setTitle(getResources().getString(R.string.frame_num) + frameNumber + ": " + frameOverview.getPlayerName());

        initialInput = false;

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
                    int resultCodeUpdateFrom = position >= 2 ? position - 2 : 0 ;
                    frameOverview.setFrameScore(score);
                    if(toCreate) {
                        frameOverviews.add(frameOverview);
                    }

                    if (getTargetFragment() != null)
                        getTargetFragment().onActivityResult(REQUEST_CODE_UPDATE_LOCALLY, resultCodeUpdateFrom, null);
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
