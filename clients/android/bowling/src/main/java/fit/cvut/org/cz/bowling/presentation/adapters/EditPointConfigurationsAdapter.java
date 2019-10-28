package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.presentation.adapters.vh.PointConfigurationEditViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class EditPointConfigurationsAdapter extends AbstractListAdapter<Float, PointConfigurationEditViewHolder> {
    String label_1, label_2;

    @NonNull
    @Override
    public PointConfigurationEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        label_1 = parent.getResources().getString(R.string.pc_place_label_1);
        label_2 = parent.getResources().getString(R.string.pc_place_label_2);
        return new PointConfigurationEditViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_point_configuration , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PointConfigurationEditViewHolder holder, int position) {
        String label = label_1 + " " + (position + 1) + label_2;
        holder.label.setText(label);
        holder.points.setText(String.format(Locale.getDefault(), "%f", data.get(position)));
    }
}
