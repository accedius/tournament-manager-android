package fit.cvut.org.cz.tmlibrary.presentation.adapters.vh;

import android.view.View;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractOneActionListAdapter;

/**
 * Created by Vaclav on 15. 3. 2016.
 */
public class SimpleOneActionViewHolder extends OneActionViewHolder {

    public TextView name;

    public SimpleOneActionViewHolder(View itemView, AbstractOneActionListAdapter adapter) {
        super(itemView, adapter);
        name = (TextView) itemView.findViewById(R.id.tv_name);
    }

    @Override
    protected View findViewWAction(View v) {
        return v.findViewById(R.id.aView);
    }
}
