package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.services.PlayerService;

/**
 * Created by atgot_000 on 29. 4. 2016.
 */
public class DeleteOnlyDialog extends DialogFragment {

    private static final String ARG_PLAYER_ID = "arg_player_id";
    private static final String ARG_COMP_ID = "arg_comp_id";
    private static final String ARG_TOUR_ID = "arg_tour_id";

    protected DialogInterface.OnClickListener supplyListener() { return null;}

    public static DeleteOnlyDialog newInstance( long playerId, long compId, long tourId ){
        DeleteOnlyDialog fragment = new DeleteOnlyDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_PLAYER_ID, playerId);
        args.putLong(ARG_COMP_ID, compId);
        args.putLong(ARG_TOUR_ID, tourId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        String[] items = new String[]{ getActivity().getString(R.string.delete) };

        builder.setItems( items, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( which == 0 ){
                    if(getArguments().getLong(ARG_COMP_ID) != -1){
                        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_DELETE_PLAYER_FROM_COMPETITION, getContext());
                        intent.putExtra( PlayerService.EXTRA_PLAYER_ID, getArguments().getLong(ARG_PLAYER_ID) );
                        intent.putExtra( PlayerService.EXTRA_ID, getArguments().getLong(ARG_COMP_ID) );
                        getContext().startService( intent );
                    } else {
                        Intent intent = PlayerService.newStartIntent( PlayerService.ACTION_DELETE_PLAYER_FROM_TOURNAMENT, getContext());
                        intent.putExtra( PlayerService.EXTRA_PLAYER_ID, getArguments().getLong(ARG_PLAYER_ID) );
                        intent.putExtra( PlayerService.EXTRA_ID, getArguments().getLong(ARG_TOUR_ID) );
                        getContext().startService( intent );
                    }
                }
                dismiss();
            }
        });

        return builder.create();
    }
}
