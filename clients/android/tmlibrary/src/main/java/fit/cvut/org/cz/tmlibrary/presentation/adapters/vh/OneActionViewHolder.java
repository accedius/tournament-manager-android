package fit.cvut.org.cz.tmlibrary.presentation.adapters.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractOneActionListAdapter;

/**
 * Created by Vaclav on 19. 3. 2016.
 */
public abstract class OneActionViewHolder extends RecyclerView.ViewHolder {
    public View viewWAction;
    private AbstractOneActionListAdapter adapter;

    /**
     * Purpose of this method is to return View that on click will fire Adapter action
     * @param v Inflated View that is suplied to constructor
     * @return View that will fire action
     */
    protected abstract View findViewWAction(View v);

    public OneActionViewHolder(View itemView, final AbstractOneActionListAdapter adapter) {
        super(itemView);
        this.adapter = adapter;
        viewWAction = findViewWAction(itemView);
        if (viewWAction != null)
            viewWAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.doAction(getAdapterPosition());
                }
            });
    }
}
