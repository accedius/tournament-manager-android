package fit.cvut.org.cz.tournamentmanager.presentation.adapters.vh;

import android.view.View;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractOneActionListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;
import fit.cvut.org.cz.tournamentmanager.R;

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
