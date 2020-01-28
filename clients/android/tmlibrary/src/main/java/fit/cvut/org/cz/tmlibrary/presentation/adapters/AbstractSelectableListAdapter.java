package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.util.SparseBooleanArray;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;

/**
 * Created by Vaclav on 15. 3. 2016.
 */
public abstract class AbstractSelectableListAdapter<T, VH extends OneActionViewHolder> extends AbstractOneActionListAdapter<T, VH>  {
    protected SparseBooleanArray selectedIndices = new SparseBooleanArray();

    public void swapData(ArrayList<T> data, SparseBooleanArray selectedIndices){
        super.swapData(data);
        this.selectedIndices.clear();
        for (int i =0; i< selectedIndices.size(); i++) {
            int key = selectedIndices.keyAt(i);
            this.selectedIndices.append(key, selectedIndices.get(key));
        }
        notifyDataSetChanged();
    }

    public ArrayList<T> getSelectedItems(){
        ArrayList<T> selected = new ArrayList<>();
        for (int i = 0; i < selectedIndices.size(); i++) {
            int index = selectedIndices.keyAt(i);
            if (selectedIndices.get(index))
                selected.add(data.get(index));
        }

        return selected;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        ((AppCompatCheckBox) holder.viewWAction).setChecked(selectedIndices.get(position, false));
        bindView(holder, position);
    }

    /**
     * This method is called inside onBindViewHolder it gives you the same params so treat is the same
     * It is made to be sure that super.onBindViewHolder is called
     * @param holder to bind data to
     * @param position position of data
     */
    protected abstract void bindView(VH holder, int position);

    @Override
    public void doAction(int position) {
        if (!selectedIndices.get(position, false)) {
            selectedIndices.delete(position);
            selectedIndices.append(position, true);
        } else selectedIndices.delete(position);
    }
}

