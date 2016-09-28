package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import fit.cvut.org.cz.hockey.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.hockey.presentation.services.TeamService;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;

/**
 * Created by atgot_000 on 12. 4. 2016.
 */
public class TournamentsDialog extends EditDeleteDialog {
    private static final String ARG_ID = "arg_id";
    private static final String ARG_POSITION = "arg_position";
    private static final String SECOND_ID = "second_id";

    protected DialogInterface.OnClickListener supplyListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = CreateTournamentActivity.newStartIntent(getContext(), getArguments().getLong(ARG_ID), getArguments().getLong(SECOND_ID));
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_DELETE, getContext());
                        intent.putExtra(TournamentService.EXTRA_ID, getArguments().getLong(ARG_ID));
                        intent.putExtra(TeamService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
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
        args.putLong(ARG_ID, id);
        args.putLong(SECOND_ID, otherId);
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }
}
