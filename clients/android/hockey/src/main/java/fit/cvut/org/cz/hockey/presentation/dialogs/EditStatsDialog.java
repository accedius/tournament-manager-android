package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;

/**
 * Created by atgot_000 on 24. 4. 2016.
 */
public class EditStatsDialog extends DialogFragment {

    public static final String ARG_STATS = "stats";

    protected DialogInterface.OnClickListener supplyListener() { return null;}

    private TextView goals, assists, plusMinusPoints, interventions, name;
    private MatchPlayerStatistic stat;

    public static EditStatsDialog newInstance( MatchPlayerStatistic statistic, Class<? extends EditStatsDialog> clazz)
    {
        EditStatsDialog fragment = null;
        try {
            fragment = clazz.getConstructor().newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Bundle b = new Bundle();
        b.putParcelable(ARG_STATS, statistic);
        fragment.setArguments( b );

        return fragment;
    }

    protected void saveStats( MatchPlayerStatistic statistic ) {};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        stat = getArguments().getParcelable( ARG_STATS );

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

        builder.setPositiveButton(fit.cvut.org.cz.tmlibrary.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( goals.getText().toString().isEmpty() ) goals.setText( String.valueOf(0) );
                if( assists.getText().toString().isEmpty() ) assists.setText( String.valueOf(0) );
                if( plusMinusPoints.getText().toString().isEmpty() ) plusMinusPoints.setText( String.valueOf(0) );
                if( interventions.getText().toString().isEmpty() ) interventions.setText( String.valueOf(0) );

                stat.setGoals(Integer.parseInt(goals.getText().toString()));
                stat.setAssists( Integer.parseInt(assists.getText().toString()) );
                stat.setPlusMinusPoints( Integer.parseInt(plusMinusPoints.getText().toString()) );
                stat.setInterventions( Integer.parseInt(interventions.getText().toString()) );

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