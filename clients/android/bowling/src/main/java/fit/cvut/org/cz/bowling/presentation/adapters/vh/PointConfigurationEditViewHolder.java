package fit.cvut.org.cz.bowling.presentation.adapters.vh;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fit.cvut.org.cz.bowling.R;

public class PointConfigurationEditViewHolder extends RecyclerView.ViewHolder {
    public EditText points;
    public TextView label;
    public PointConfigurationEditViewHolder (View itemView) {
        super(itemView);
        points = (EditText) itemView.findViewById(R.id.place_points);
        label = (TextView) itemView.findViewById(R.id.place_label);
    }
}
