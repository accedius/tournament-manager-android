package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.constraints.ConstraintsConstants;

public class EditPlayerStatDialog extends DialogFragment {
    PlayerStat statToEdit;
    byte availableFrames;
    TextView score, frames, spares, strikes;

    final int maxFrames = ConstraintsConstants.tenPinMatchParticipantMaxFrames;
    final int maxFrameScore = ConstraintsConstants.tenPinFrameMaxScore;
    final int maxScore = ConstraintsConstants.tenPinMatchParticipantMaxScore;

    public static EditPlayerStatDialog newInstance(PlayerStat statToEdit, byte numberOfFramesLeftAvailableForPlaying, int position) {
        Bundle args = new Bundle();
        args.putParcelable(ExtraConstants.EXTRA_PLAYER_STAT, statToEdit);
        args.putByte(ExtraConstants.EXTRA_FRAMES, numberOfFramesLeftAvailableForPlaying);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);

        EditPlayerStatDialog fragment = new EditPlayerStatDialog();
        fragment.setArguments(args);
        return fragment;
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_player_stat, null);

        builder.setView(v);
        score = v.findViewById(R.id.as_score);
        frames = v.findViewById(R.id.as_frames);
        spares = v.findViewById(R.id.as_spares);
        strikes = v.findViewById(R.id.as_strikes);

        statToEdit = getArguments().getParcelable(ExtraConstants.EXTRA_PLAYER_STAT);
        availableFrames = getArguments().getByte(ExtraConstants.EXTRA_FRAMES);
        score.setText(formatNumber(statToEdit.getPoints()));
        frames.setText(formatNumber(statToEdit.getFramesPlayedNumber()));
        spares.setText(formatNumber(statToEdit.getSpares()));
        strikes.setText(formatNumber(statToEdit.getStrikes()));

        builder.setTitle(statToEdit.getName());
        return builder.create();
    }

    private String formatNumber(Number num) { return String.format(Locale.getDefault(),"%d", num); }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog dialog = (AlertDialog) getDialog();

        Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean returnToken = false;

                if(score.getText().toString().isEmpty()) {
                    TextInputLayout til = dialog.findViewById(R.id.score_input_layout);
                    til.setError(getResources().getString(R.string.mpof_score_format_violated) + " " + maxScore + "!");
                    returnToken = true;
                }
                if(frames.getText().toString().isEmpty()) {
                    TextInputLayout til = dialog.findViewById(R.id.frames_input_layout);
                    til.setError(getResources().getString(R.string.mpof_frames_played_format_violated) + " " + availableFrames + "!");
                    returnToken = true;
                }
                if(spares.getText().toString().isEmpty()) {
                    TextInputLayout til = dialog.findViewById(R.id.spares_input_layout);
                    til.setError(getResources().getString(R.string.number_required_warning));
                    returnToken = true;
                }
                if(strikes.getText().toString().isEmpty()) {
                    TextInputLayout til = dialog.findViewById(R.id.strikes_input_layout);
                    til.setError(getResources().getString(R.string.number_required_warning));
                    returnToken = true;
                }

                if(returnToken)
                    return;

                int inputScore, inputFrames, inputStrikes, inputSpares;
                inputScore = Integer.parseInt(score.getText().toString());
                inputFrames = Integer.parseInt(frames.getText().toString());
                inputStrikes = Integer.parseInt(spares.getText().toString());
                inputSpares = Integer.parseInt(strikes.getText().toString());

                if(inputFrames < 1 || inputFrames > maxFrames || inputFrames > availableFrames) {
                    TextInputLayout til = dialog.findViewById(R.id.frames_input_layout);
                    til.setError(getResources().getString(R.string.mpof_frames_played_format_violated) + " " + availableFrames + "!");
                    returnToken = true;
                }

                if( inputScore > maxScore || !checkIfScoreIsPossible(inputScore, inputFrames)) {
                    TextInputLayout til = dialog.findViewById(R.id.score_input_layout);
                    til.setError(getResources().getString(R.string.mpof_score_violated));
                    returnToken = true;
                }

                if(returnToken)
                    return;

                if(!(inputScore == statToEdit.getPoints() && inputFrames == statToEdit.getFramesPlayedNumber() && inputSpares == statToEdit.getSpares() && inputStrikes == statToEdit.getStrikes())) {
                    Intent editedPlayerStat = new Intent();
                    editedPlayerStat.putExtra(ExtraConstants.EXTRA_BOOLEAN_IS_SCORE_CHANGED, inputScore != statToEdit.getPoints());
                    editedPlayerStat.putExtra(ExtraConstants.EXTRA_BOOLEAN_IS_FRAMES_CHANGED, inputFrames != statToEdit.getFramesPlayedNumber());

                    statToEdit.setStrikes(inputStrikes);
                    statToEdit.setSpares(inputSpares);
                    statToEdit.setPoints(inputScore);
                    statToEdit.setFramesPlayedNumber((byte) inputFrames);
                    editedPlayerStat.putExtra(ExtraConstants.EXTRA_PLAYER_STAT, statToEdit);

                    int position = getArguments().getInt(ExtraConstants.EXTRA_POSITION);
                    editedPlayerStat.putExtra(ExtraConstants.EXTRA_POSITION, position);
                    if(getTargetFragment() != null) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, editedPlayerStat);
                    }
                }
                dismiss();
            }
        });
    }

    private boolean checkIfScoreIsPossible(int score, int frames) {
        //TODO implement some method that will moreover consider other players possible throws (after our last strike/spare)
        return true;
    }
}
