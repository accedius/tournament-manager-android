package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Vaclav on 19. 3. 2016.
 */
public abstract class AbstractOneActionListAdapter<T, VH extends RecyclerView.ViewHolder> extends AbstractListAdapter<T, VH> {
    /**
     * action that is fired upon clicking on View specified in OneActionViewHolder
     * @param position
     */
    public abstract void doAction(int position);

}
