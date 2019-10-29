package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class EditPointConfigurationsAdapter extends AbstractListAdapter<Float, EditPointConfigurationsAdapter.PointConfigurationEditViewHolder> {
    String label_1, label_2;
    final String regexp1 = "^[+-]?[0-9]*[,.]?[0-9]*$";
    final String regexp2 = "^.*[0-9].*$";
    Pattern pattern1 = Pattern.compile(regexp1);
    Pattern pattern2 = Pattern.compile(regexp2);

    @NonNull
    @Override
    public PointConfigurationEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        label_1 = parent.getResources().getString(R.string.pc_place_label_1);
        label_2 = parent.getResources().getString(R.string.pc_place_label_2);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_point_configuration , parent, false);
        PointConfigurationEditViewHolder vh = new PointConfigurationEditViewHolder(v, new PCEditTextListener());
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PointConfigurationEditViewHolder holder, final int position) {
        String label = label_1 + " " + (position + 1) + label_2;
        holder.label.setText(label);
        int adapterPosition = holder.getAdapterPosition();
        holder.listener.updatePosition(adapterPosition);
        holder.points.setText(String.format(Locale.getDefault(), "%f", data.get(adapterPosition)));
    }

    public class PointConfigurationEditViewHolder extends RecyclerView.ViewHolder {
        public EditText points;
        public TextView label;
        public PCEditTextListener listener;

        public PointConfigurationEditViewHolder (View itemView, PCEditTextListener editTextListener) {
            super(itemView);
            points = (EditText) itemView.findViewById(R.id.place_points);
            label = (TextView) itemView.findViewById(R.id.place_label);
            listener = editTextListener;
            points.addTextChangedListener(listener);
        }
    }

    protected class PCEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            String text = charSequence.toString();
            Matcher matcher1 = pattern1.matcher(text);
            Matcher matcher2 = pattern2.matcher(text);
            if(matcher1.matches() && matcher2.matches()) {
                Float points;
                try {
                    points = NumberFormat.getNumberInstance().parse(text).floatValue();
                } catch (ParseException pe) {
                    try {
                        points = Float.parseFloat(text);
                    } catch (NumberFormatException nfe) {
                        return;
                    }
                }
                data.set(position, points);
            } else {
                return;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
