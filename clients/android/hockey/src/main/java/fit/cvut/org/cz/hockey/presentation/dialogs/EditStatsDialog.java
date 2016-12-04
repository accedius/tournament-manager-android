package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchStatsFragment;

/**
 * Created by atgot_000 on 24. 4. 2016.
 */
public class EditStatsDialog extends DialogFragment {
    public static final String ARG_DATA = "arg_data";
    public static final String ARG_POSITION = "arg_position";
    public static final String ARG_HOME = "arg_home";

    private TextView goals, assists, plusMinusPoints, saves;
    private PlayerStat stat;

    public static EditStatsDialog newInstance(PlayerStat statistic, int pos, boolean isHome) {
        EditStatsDialog fragment = new EditStatsDialog();
        Bundle b = new Bundle();
        b.putParcelable(ARG_DATA, statistic);
        b.putBoolean(ARG_HOME, isHome);
        b.putInt(ARG_POSITION, pos);
        fragment.setArguments(b);
        return fragment;
    }

    /**
     * Override this function to save the stats when dialog is closed
     */
    protected void saveStats() {
        ((HockeyMatchStatsFragment)getTargetFragment()).setPlayerStats(getArguments().getBoolean(ARG_HOME), getArguments().getInt(ARG_POSITION), stat);
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        stat = getArguments().getParcelable(ARG_DATA);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setPositiveButton(fit.cvut.org.cz.tmlibrary.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (goals.getText().toString().isEmpty())
                    goals.setText(String.valueOf(0));
                if (assists.getText().toString().isEmpty())
                    assists.setText(String.valueOf(0));
                if (plusMinusPoints.getText().toString().isEmpty())
                    plusMinusPoints.setText(String.valueOf(0));
                if (saves.getText().toString().isEmpty())
                    saves.setText(String.valueOf(0));

                stat.setGoals(Integer.parseInt(goals.getText().toString()));
                stat.setAssists(Integer.parseInt(assists.getText().toString()));
                stat.setPlusMinus(Integer.parseInt(plusMinusPoints.getText().toString()));
                stat.setSaves(Integer.parseInt(saves.getText().toString()));

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

        goals = (TextView) v.findViewById(R.id.tv_goals);
        assists = (TextView) v.findViewById(R.id.tv_assists);
        plusMinusPoints = (TextView) v.findViewById(R.id.tv_plus_minus);
        saves = (TextView) v.findViewById(R.id.tv_saves);

        goals.setText(String.valueOf(stat.getGoals()));
        assists.setText(String.valueOf(stat.getAssists()));
        plusMinusPoints.setText(String.valueOf(stat.getPlusMinus()));
        saves.setText(String.valueOf(stat.getSaves()));

        builder.setTitle(stat.getName());
        builder.setView(v);
        return builder.create();
    }
}