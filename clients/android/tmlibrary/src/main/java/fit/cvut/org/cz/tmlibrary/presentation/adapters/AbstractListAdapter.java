package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public abstract class AbstractListAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    protected List<T> data;

    public AbstractListAdapter(List<T> data) {
        this.data = data;
    }

    public void swapData(List<T> list){
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }
}
