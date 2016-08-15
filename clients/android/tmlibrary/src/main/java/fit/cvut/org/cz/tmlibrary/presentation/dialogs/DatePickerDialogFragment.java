package fit.cvut.org.cz.tmlibrary.presentation.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by Vaclav on 27. 3. 2016.
 */
public class DatePickerDialogFragment extends DialogFragment {

    public DatePickerDialog.OnDateSetListener listener = null;

    protected boolean dateSet = false;
    protected int year;
    protected int month;
    protected int day;

    @Override
    public void setArguments(Bundle b) {
        year = b.getInt("y");
        month = b.getInt("m");
        day = b.getInt("d");
        dateSet = true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!dateSet) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), listener, y, m, d);
        } else {
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }
    }
}
