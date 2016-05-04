package fit.cvut.org.cz.squash.presentation.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Vaclav on 11. 4. 2016.
 */
public class SelectTeamDialog extends DialogFragment {

    protected void homeClick(){}
    protected void awayClick(){}
    protected String getHomeName() {return "";}
    protected String getAwayName() {return "";}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setItems(new String[]{"Add Player to " + getHomeName(), "Add Player to " + getAwayName()},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                homeClick();
                                break;
                            case 1:
                                awayClick();
                                break;
                        }
                        dismiss();
                    }
                });

        return builder.create();
    }
}
