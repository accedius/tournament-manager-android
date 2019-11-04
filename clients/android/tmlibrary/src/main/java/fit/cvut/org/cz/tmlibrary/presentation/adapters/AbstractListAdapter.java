package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaclav on 12. 3. 2016.
 * Simple adapter that already provides container where you can store data
 */
public abstract class AbstractListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected final List<T> data = new ArrayList<>();

    /**
     * Method to refresh a view and see the changes
     * @param list new data to replace
     */
    public void swapData(List<T> list){
        this.data.clear();
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<T> getData() {return new ArrayList<>(data);}
    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }
}
