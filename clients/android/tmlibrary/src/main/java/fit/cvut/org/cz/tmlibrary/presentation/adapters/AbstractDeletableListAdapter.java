package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;

/**
 * Created by Vaclav on 19. 3. 2016.
 */
public abstract class AbstractDeletableListAdapter<T, VH extends OneActionViewHolder> extends AbstractOneActionListAdapter<T, VH> {
    @Override
    public void doAction(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

}
