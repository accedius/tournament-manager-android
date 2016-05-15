package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;

/**
 * Created by Vaclav on 11. 4. 2016.
 */
public class TournamentsDialog extends DialogFragment {

    public TournamentsDialog(){}

    public static final String ARG_ID = "arg_id";
    public static final String ARG_POSITION = "arg_pos";

    public static TournamentsDialog newInstance(long tournamentId, int position){
        TournamentsDialog fragment = new TournamentsDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, tournamentId);
        args.putInt(ARG_POSITION, position);
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
                                Intent intent = CreateTournamentActivity.newStartIntent(getContext(), getArguments().getLong(ARG_ID), false);
                                startActivity(intent);
                                break;
                            }
                            case 1:{
                                Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_DELETE, getContext());
                                intent.putExtra(TournamentService.EXTRA_POSITION, getArguments().getInt(ARG_POSITION));
                                intent.putExtra(TournamentService.EXTRA_ID, getArguments().getLong(ARG_ID));
                                getContext().startService(intent);
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
