package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;

/**
 * Created by kevin on 2.9.2016.
 */
public class GenerateRostersDialog extends DialogFragment {
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GENERATE_ROSTERS, getContext());
                intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_COMP_ID));
                intent.putExtra(ExtraConstants.EXTRA_TOURNAMENT, getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                intent.putExtra(ExtraConstants.EXTRA_GENERATING_TYPE, which);
                getContext().startService(intent);
                dialog.dismiss();
            }
        };
    }

    public static GenerateRostersDialog newInstance(long competitionId, long tournamentId) {
        GenerateRostersDialog fragment = new GenerateRostersDialog();
        Bundle b = new Bundle();
        b.putLong(ExtraConstants.EXTRA_COMP_ID, competitionId);
        b.putLong(ExtraConstants.EXTRA_TOUR_ID, tournamentId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = new String[TournamentService.GENERATING_TYPES_CNT];
        items[TournamentService.GENERATE_BY_TEAM_POINTS] = getActivity().getString(R.string.generate_by_team_points);
        items[TournamentService.GENERATE_BY_SETS] = getActivity().getString(R.string.generate_by_sets);
        items[TournamentService.GENERATE_BY_BALLS] = getActivity().getString(R.string.generate_by_balls);
        items[TournamentService.GENERATE_RANDOMLY] = getActivity().getString(R.string.generate_randomly);
        builder.setItems(items, supplyListener());

        builder.setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.generate_rosters));
        return builder.create();
    }
}
