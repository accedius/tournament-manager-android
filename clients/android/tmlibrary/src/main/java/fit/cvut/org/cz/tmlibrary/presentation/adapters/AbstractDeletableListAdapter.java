package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;

/**
 * Adapter for displaying deletable list of entities.
 */
public abstract class AbstractDeletableListAdapter<T, VH extends OneActionViewHolder> extends AbstractOneActionListAdapter<T, VH> {
    @Override
    public void doAction(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

}
