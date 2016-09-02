package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;

/**
 * Created by kevin on 2.9.2016.
 */
public class GenerateRostersDialog extends DialogFragment {

    private static final String ARG_COMP_ID = "competition_id";
    private static final String ARG_TOUR_ID = "tournament_id";

    protected DialogInterface.OnClickListener supplyListener() { return null;}

    public static GenerateRostersDialog newInstance( long competitionId, long tournamentId) {
        GenerateRostersDialog fragment = new GenerateRostersDialog();

        Bundle b = new Bundle();
        b.putLong(ARG_COMP_ID, competitionId);
        b.putLong(ARG_TOUR_ID, tournamentId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        String[] items = new String[TournamentService.GENERATING_TYPES_CNT];
        items[TournamentService.GENERATE_BY_TEAM_POINTS] = getActivity().getString(R.string.generate_by_team_points);
        items[TournamentService.GENERATE_BY_SETS] = getActivity().getString(R.string.generate_by_sets);
        items[TournamentService.GENERATE_BY_BALLS] = getActivity().getString(R.string.generate_by_balls);
        items[TournamentService.GENERATE_RANDOMLY] = getActivity().getString(R.string.generate_randomly);
        builder.setItems( items, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    default:
                        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GENERATE_ROSTERS, getContext());
                        intent.putExtra(TournamentService.EXTRA_ID, getArguments().getLong(ARG_COMP_ID));
                        intent.putExtra(TournamentService.EXTRA_TOURNAMENT, getArguments().getLong(ARG_TOUR_ID));
                        intent.putExtra(TournamentService.EXTRA_GENERATING_TYPE, which);
                        getContext().startService(intent);
                        break;
                }
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
