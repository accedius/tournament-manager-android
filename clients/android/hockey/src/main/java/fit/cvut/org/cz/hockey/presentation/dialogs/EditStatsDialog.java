package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchStatsFragment;

/**
 * Created by atgot_000 on 24. 4. 2016.
 */
public class EditStatsDialog extends DialogFragment {

    public static final String ARG_DATA = "data";
    public static final String ARG_POSITION = "position";
    public static final String ARG_HOME = "homea";


    protected DialogInterface.OnClickListener supplyListener() { return null;}

    private TextView goals, assists, plusMinusPoints, interventions, name;
    private MatchPlayerStatistic stat;

    public static EditStatsDialog newInstance( MatchPlayerStatistic statis, int pos, boolean isHome)
    {
        EditStatsDialog fragment = new EditStatsDialog();

        Bundle b = new Bundle();
        b.putParcelable(ARG_DATA, statis);
        b.putBoolean(ARG_HOME, isHome);
        b.putInt(ARG_POSITION, pos);
        fragment.setArguments(b);

        return fragment;
    }

    /**
     * Override this function to save the stats when dialog is closed
     * @param statistic
     */
    protected void saveStats( MatchPlayerStatistic statistic ) {

        ((HockeyMatchStatsFragment)getTargetFragment()).setPlayerStats(getArguments().getBoolean(ARG_HOME), getArguments().getInt(ARG_POSITION), stat);

    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        stat = getArguments().getParcelable(ARG_DATA);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

        builder.setPositiveButton(fit.cvut.org.cz.tmlibrary.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( goals.getText().toString().isEmpty() ) goals.setText( String.valueOf(0) );
                if( assists.getText().toString().isEmpty() ) assists.setText( String.valueOf(0) );
                if( plusMinusPoints.getText().toString().isEmpty() ) plusMinusPoints.setText( String.valueOf(0) );
                if( interventions.getText().toString().isEmpty() ) interventions.setText( String.valueOf(0) );

                stat.setGoals(Integer.parseInt(goals.getText().toString()));
                stat.setAssists(Integer.parseInt(assists.getText().toString()));
                stat.setPlusMinusPoints(Integer.parseInt(plusMinusPoints.getText().toString()));
                stat.setInterventions( Integer.parseInt(interventions.getText().toString()));

                saveStats( stat );
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
        goals.setText(String.valueOf(stat.getGoals()));

        assists = (TextView) v.findViewById(R.id.tv_assists);
        assists.setText( String.valueOf(stat.getAssists()));

        plusMinusPoints = (TextView) v.findViewById(R.id.tv_plus_minus);
        plusMinusPoints.setText( String.valueOf(stat.getPlusMinusPoints()));

        interventions = (TextView) v.findViewById(R.id.tv_interventions);
        interventions.setText(String.valueOf(stat.getInterventions()));

        name = (TextView) v.findViewById(R.id.tv_name);
        name.setText( stat.getName() );

        builder.setView(v);

        return builder.create();
    }
}