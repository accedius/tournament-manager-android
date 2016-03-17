package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SelectableViewHolder;


/**
 * Created by Vaclav on 15. 3. 2016.
 */
public abstract class AbstractSelectableListAdapter<T, VH extends SelectableViewHolder> extends RecyclerView.Adapter<VH>  {

    protected SparseBooleanArray selectedIndeces = new SparseBooleanArray();
    protected ArrayList<T> data = new ArrayList<>();

    public void swapData(ArrayList<T> data, SparseBooleanArray selectedIndeces){

        this.data.clear();
        this.data.addAll(data);

        this.selectedIndeces.clear();
        for (int i =0; i< selectedIndeces.size(); i++){
            int key = selectedIndeces.keyAt(i);
            this.selectedIndeces.append(key, selectedIndeces.get(key));
        }

        notifyDataSetChanged();
    }

    public ArrayList<T> getSelectedItems(){

        ArrayList<T> selected = new ArrayList<>();
        for (int i = 0; i < selectedIndeces.size(); i++){
            int index = selectedIndeces.keyAt(i);
            selected.add(data.get(index));
        }

        return selected;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public final void onBindViewHolder(VH holder, int position) {
        ((SelectableViewHolder) holder).checkbox.setChecked(selectedIndeces.get(position, false));
        ((SelectableViewHolder) holder).position = position;
        bindView(holder, position);
    }

    /**
     * This method is called inside onBindViewHolder it gives you the same params so treat is the same
     * It is made to be sure that super.onBindViewHolder is called
     * @param holder to bind data to
     * @param position position of data
     */
    protected abstract void bindView(VH holder, int position);

    public void click(int position){

       if (!selectedIndeces.get(position, false)){
           selectedIndeces.delete(position);
           selectedIndeces.append(position, true);
       } else selectedIndeces.delete(position);

    }


}


