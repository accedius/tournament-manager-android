package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingFFAMatchStatsFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingMatchStatsFragment;

/**
 * Dialog, that occurs, when you hold player's field in match players fragment to edit his stats
 */
public class EditStatsDialog extends DialogFragment {
    private TextView strikes, spares, points;
    private PlayerStat stat;

    public static EditStatsDialog newInstance(PlayerStat statistic, int pos, boolean isHome) {
        EditStatsDialog fragment = new EditStatsDialog();
        Bundle b = new Bundle();
        b.putParcelable(ExtraConstants.EXTRA_DATA, statistic);
        b.putBoolean(ExtraConstants.EXTRA_BOOLEAN_HOME, isHome);
        b.putInt(ExtraConstants.EXTRA_POSITION, pos);
        fragment.setArguments(b);
        return fragment;
    }

    /**
     * Override this function to save the stats when dialog is closed
     */
    protected void saveStats() {
        ((BowlingFFAMatchStatsFragment)getTargetFragment()).setPlayerStats(((PlayerStat)getArguments().getParcelable(ExtraConstants.EXTRA_DATA)).getParticipantId(), getArguments().getInt(ExtraConstants.EXTRA_POSITION), stat);
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        stat = getArguments().getParcelable(ExtraConstants.EXTRA_DATA);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setPositiveButton(fit.cvut.org.cz.tmlibrary.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (strikes.getText().toString().isEmpty())
                    strikes.setText(String.valueOf(0));
                if (spares.getText().toString().isEmpty())
                    spares.setText(String.valueOf(0));
                if (points.getText().toString().isEmpty())
                    points.setText(String.valueOf(0));

                stat.setStrikes(Integer.parseInt(strikes.getText().toString()));
                stat.setSpares(Integer.parseInt(spares.getText().toString()));
                stat.setPoints(Integer.parseInt(points.getText().toString()));

                saveStats();
                dismiss();
            }
        });
        builder.setNegativeButton(fit.cvut.org.cz.tmlibrary.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_stats, null);

        strikes = (TextView) v.findViewById(R.id.tv_strikes);
        spares = (TextView) v.findViewById(R.id.tv_spares);
        points = (TextView) v.findViewById(R.id.tv_points);

        strikes.setText(String.valueOf(stat.getStrikes()));
        spares.setText(String.valueOf(stat.getSpares()));
        points.setText(String.valueOf(stat.getPoints()));

        builder.setTitle(stat.getName());
        builder.setView(v);
        return builder.create();
    }
}
