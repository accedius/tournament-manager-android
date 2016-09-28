package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;

/** Dialog that allows to edit or delete tournament
 * Created by Vaclav on 11. 4. 2016.
 */
public class TournamentsDialog extends DialogFragment {
    public TournamentsDialog(){}

    public static final String COMP_ID = "comp_id";
    public static final String TOUR_ID = "tour_id";
    public static final String ARG_POSITION = "arg_pos";
    public static final String ARG_TITLE = "arg_title";

    public static TournamentsDialog newInstance(long compId, long tourId, int position, String name) {
        TournamentsDialog fragment = new TournamentsDialog();
        Bundle args = new Bundle();
        args.putLong(COMP_ID, compId);
        args.putLong(TOUR_ID, tourId);
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_TITLE, name);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(new String[]{getString(R.string.edit), getString(R.string.delete)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:{
                                Intent intent = CreateTournamentActivity.newStartIntent(getContext(), getArguments().getLong(COMP_ID), getArguments().getLong(TOUR_ID));
                                startActivity(intent);
                                break;
                            }
                            case 1:{
                                Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_DELETE, getContext());
                                intent.putExtra(TournamentService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
                                intent.putExtra(TournamentService.EXTRA_ID, getArguments().getLong(TOUR_ID));
                                getContext().startService(intent);
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                });

        builder.setTitle(getArguments().getString(ARG_TITLE));
        return builder.create();
    }
}
