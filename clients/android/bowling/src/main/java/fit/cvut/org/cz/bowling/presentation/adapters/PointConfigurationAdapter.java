package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Adapter for displaying list of PointConfigurations
 */
public class PointConfigurationAdapter extends AbstractListAdapter<PointConfiguration, PointConfigurationAdapter.PointConfigurationViewHolder> {

    @Override
    public PointConfigurationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PointConfigurationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_point_configuration, parent, false));
    }

    protected void setOnClickListeners(View v, final long pointConfigurationId, int position, long sidesNumber) {}

    @Override
    public void onBindViewHolder(PointConfigurationViewHolder holder, int position) {
        PointConfiguration pointConfiguration = data.get(position);
        setOnClickListeners(holder.wholeView, pointConfiguration.getId(), position, pointConfiguration.getSidesNumber());

        Long sidesNumber = pointConfiguration.getSidesNumber();
        holder.sidesNumber.setText(String.format(Locale.getDefault(),"%d", sidesNumber));
        holder.sidesNumber.setVisibility(View.VISIBLE);
    }

    public class PointConfigurationViewHolder extends RecyclerView.ViewHolder{
        public TextView sidesNumber;
        public View wholeView;

        public PointConfigurationViewHolder(View itemView) {
            super(itemView);
            sidesNumber = (TextView) itemView.findViewById(R.id.pcv_sides_number);
            wholeView = itemView;
        }
    }
}
