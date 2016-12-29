package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import fit.cvut.org.cz.hockey.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;

/**
 * Created by atgot_000 on 12. 4. 2016.
 */
public class TournamentsDialog extends EditDeleteDialog {
    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = CreateTournamentActivity.newStartIntent(getContext(), getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID), getArguments().getLong(ExtraConstants.EXTRA_COMP_ID));
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_DELETE, getContext());
                        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));
                        intent.putExtra(ExtraConstants.EXTRA_POSITION, getArguments().getInt(ExtraConstants.EXTRA_POSITION));
                        getContext().startService(intent);
                        break;
                    }
                }
                dialog.dismiss();
            }
        };
    }

    public static TournamentsDialog newInstance(long id, long otherId, int position, String name){
        TournamentsDialog fragment = new TournamentsDialog();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);
        args.putLong(ExtraConstants.EXTRA_COMP_ID, otherId);
        args.putInt(ExtraConstants.EXTRA_POSITION, position);
        args.putString(ExtraConstants.EXTRA_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }
}
