package fit.cvut.org.cz.bowling.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class EditableStatsAdapter extends AbstractListAdapter<PlayerStat, EditableStatsAdapter.EditableStatsViewHolder> {
    @Override
    public EditableStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditableStatsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_editable_stats, parent, false));
    }

    /**
     * Method to bind data to View Holder
     * @param holder View Holder
     * @param position position of stats in data array
     */
    @Override
    public void onBindViewHolder(EditableStatsViewHolder holder, int position) {
        PlayerStat rowItem = data.get(position);

        holder.strikeLsnr.updatePosition(position);
        holder.spareLsnr.updatePosition(position);
        holder.pointLsnr.updatePosition(position);

        holder.strikes.setText(Integer.toString(rowItem.getStrikes()));
        holder.spares.setText(Integer.toString(rowItem.getSpares()));
        holder.points.setText(Integer.toString(rowItem.getPoints()));

        holder.name.setText(rowItem.getName());
    }

    /**
     * Editable Stats specific View Holder
     */
    public class EditableStatsViewHolder extends RecyclerView.ViewHolder {
        public long id;
        TextView name;
        EditText strikes, spares, points;
        public View wholeView;
        public EditDataListener strikeLsnr, spareLsnr, pointLsnr;

        public EditableStatsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.as_name);
            strikes = (EditText) itemView.findViewById(R.id.et_strikes);
            spares = (EditText) itemView.findViewById(R.id.et_spares);
            points = (EditText) itemView.findViewById(R.id.et_points);
            wholeView = itemView;

            this.strikeLsnr = new EditDataListener(EditDataListener.LISTEN_STRIKES);
            this.spareLsnr = new EditDataListener(EditDataListener.LISTEN_SPARES);
            this.pointLsnr = new EditDataListener(EditDataListener.LISTEN_POINTS);

            strikes.addTextChangedListener(strikeLsnr);
            spares.addTextChangedListener(spareLsnr);
            points.addTextChangedListener(pointLsnr);
        }
    }

    /**
     * Listener that changes the data in adapter whenever something changed on the screen
     */
    private class EditDataListener implements TextWatcher {
        public static final int LISTEN_STRIKES = 0;
        public static final int LISTEN_SPARES = 1;
        public static final int LISTEN_POINTS = 2;

        private int listenType;
        private int position;

        public EditDataListener(int listenType){
            this.listenType = listenType;
        }

        public void updatePosition(int position){
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int value;
            if (s.length() <= 0)
                value = 0;
            else if (s.length() == 1) {
                if (s.charAt(0) == '-')
                    value = 0;
                else
                    value = Integer.parseInt(s.toString());
            }
            else
                value = Integer.parseInt(s.toString());

            switch (listenType) {
                case LISTEN_STRIKES:
                    data.get(position).setStrikes(value);
                    break;
                case LISTEN_SPARES:
                    data.get(position).setSpares(value);
                    break;
                case LISTEN_POINTS:
                    data.get(position).setPoints(value);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

}
