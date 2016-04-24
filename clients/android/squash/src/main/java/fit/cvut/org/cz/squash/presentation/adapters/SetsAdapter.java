package fit.cvut.org.cz.squash.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.ParseException;
import java.util.ArrayList;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Created by Vaclav on 24. 4. 2016.
 */
public class SetsAdapter extends AbstractListAdapter<SetRowItem, SetsAdapter.SetRowItemVH> {

    //private ArrayList<SetRowItemVH> holders = new ArrayList<>();
    private int errorCount = 0;

    public boolean hasErrors() { return errorCount != 0;}

    public void addItem(SetRowItem item){
        data.add(item);
        notifyItemInserted(data.size() -1);
    }

    public void deleteItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public SetRowItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SetRowItemVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_set, parent, false), new ParsingDataListener(true), new ParsingDataListener(false));
    }


    @Override
    public void onBindViewHolder(SetRowItemVH holder, int position) {

        SetRowItem item = data.get(position);


        holder.homeLsnr.updatePosition(position);
        holder.awayLsnr.updatePosition(position);
        holder.awayLsnr.updateEditText(holder.away);
        holder.homeLsnr.updateEditText(holder.home);
        holder.home.setText(Integer.toString(item.getHomeScore()));
        holder.away.setText(Integer.toString(item.getAwayScore()));

    }



    public class SetRowItemVH extends RecyclerView.ViewHolder{

        public EditText home, away;
        public ParsingDataListener homeLsnr, awayLsnr;

        public SetRowItemVH(View itemView, ParsingDataListener homeLsnr, ParsingDataListener awayLsnr) {
            super(itemView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteItem(getAdapterPosition());
                    return false;
                }
            });
            home = (EditText) itemView.findViewById(R.id.et_home);
            away = (EditText) itemView.findViewById(R.id.et_away);
            this.homeLsnr = homeLsnr;
            this.awayLsnr = awayLsnr;
            home.addTextChangedListener(homeLsnr);
            away.addTextChangedListener(awayLsnr);
        }
    }

    private class ParsingDataListener implements TextWatcher{

        private int position;
        private EditText editText;
        private boolean home;

        public ParsingDataListener(boolean home) {
            this.home = home;
        }

        public void updatePosition(int position){
            this.position = position;
        }
        public void updateEditText(EditText editText){
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (errorCount > 0) errorCount--;
            editText.setError(null);
            try {
                int value = Integer.parseInt(s.toString());
                if (value < 0){
                    errorCount++;
                    editText.setError(editText.getResources().getText(R.string.zero_score));
                    return;
                }
                if (home) data.get(position).setHomeScore(value);
                else  data.get(position).setAwayScore(value);
                if (data.get(position).getHomeScore() > data.get(position).getAwayScore()) data.get(position).setWinner(1);
                else if (data.get(position).getHomeScore() < data.get(position).getAwayScore()) data.get(position).setWinner(-1);
                    else data.get(position).setWinner(0);
            } catch (NumberFormatException e){
                errorCount++;
                editText.setError(editText.getResources().getText(R.string.parse_number_error));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
