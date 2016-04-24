package fit.cvut.org.cz.hockey.presentation.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.lang.reflect.InvocationTargetException;

import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;

/**
 * Created by atgot_000 on 24. 4. 2016.
 */
public abstract class EditStatsDialog extends DialogFragment {

    private static final String ARG_STATS = "stats";

    protected DialogInterface.OnClickListener supplyListener() { return null;}

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

    protected abstract void saveStats( MatchPlayerStatistic statistic );

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //TODO udelat layou, napojit ho... a skoro hotovo

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        String[] items = new String[]{ "Edit stats", "Delete" };

        builder.setItems( items, supplyListener() );

        return builder.create();
    }
}