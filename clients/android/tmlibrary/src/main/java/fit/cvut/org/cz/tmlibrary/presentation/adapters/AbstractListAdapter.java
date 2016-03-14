package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public abstract class AbstractListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected final List<T> data = new ArrayList<>();

    public void swapData(List<T> list){
        this.data.clear();
        this.data.addAll(list);
        notifyDataSetChanged();
    }
}
