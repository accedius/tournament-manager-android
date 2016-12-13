package fit.cvut.org.cz.hockey.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Adapter holding editable match statistics
 * Created by atgot_000 on 1. 5. 2016.
 */
public class EditableStatsAdapter extends AbstractListAdapter<PlayerStat, EditableStatsAdapter.EditableStatsViewHolder> {
    @Override
    public EditableStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditableStatsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_editable_stats, parent, false));
    }

    @Override
    public void onBindViewHolder(EditableStatsViewHolder holder, int position) {
        PlayerStat rowItem = data.get(position);

        holder.goalLsnr.updatePosition(position);
        holder.assLsnr.updatePosition(position);
        holder.pmpLsnr.updatePosition(position);
        holder.interLsnr.updatePosition(position);

        holder.goals.setText(Integer.toString(rowItem.getGoals()));
        holder.assists.setText(Integer.toString(rowItem.getAssists()));
        holder.plusMinus.setText(Integer.toString(rowItem.getPlusMinus()));
        holder.interv.setText(Integer.toString(rowItem.getSaves()));

        holder.name.setText(rowItem.getName());
    }

    public class EditableStatsViewHolder extends RecyclerView.ViewHolder {
        public long id;
        TextView name;
        EditText goals, assists, plusMinus, interv;
        public View wholeView;
        public EditDataListener goalLsnr, assLsnr, pmpLsnr, interLsnr;

        public EditableStatsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.as_name);
            goals = (EditText) itemView.findViewById(R.id.et_goals);
            assists = (EditText) itemView.findViewById(R.id.et_assists);
            plusMinus = (EditText) itemView.findViewById(R.id.et_plus_minus);
            interv = (EditText) itemView.findViewById(R.id.et_saves);
            wholeView = itemView;

            this.goalLsnr = new EditDataListener(EditDataListener.LISTEN_GOALS);
            this.assLsnr = new EditDataListener(EditDataListener.LISTEN_ASSISTS);
            this.pmpLsnr = new EditDataListener(EditDataListener.LISTEN_PLUSMP);
            this.interLsnr = new EditDataListener(EditDataListener.LISTEN_SAVES);

            goals.addTextChangedListener(goalLsnr);
            assists.addTextChangedListener(assLsnr);
            plusMinus.addTextChangedListener(pmpLsnr);
            interv.addTextChangedListener(interLsnr);
        }
    }

    /**
     * Listener that changes the data in adapter whenever something changed on the screen
     */
    private class EditDataListener implements TextWatcher{
        public static final int LISTEN_GOALS = 0;
        public static final int LISTEN_ASSISTS = 1;
        public static final int LISTEN_PLUSMP = 2;
        public static final int LISTEN_SAVES = 3;

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
                case LISTEN_GOALS:
                    data.get(position).setGoals(value);
                    break;
                case LISTEN_ASSISTS:
                    data.get(position).setAssists(value);
                    break;
                case LISTEN_SAVES:
                    data.get(position).setSaves(value);
                    break;
                case LISTEN_PLUSMP:
                    data.get(position).setPlusMinus(value);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
