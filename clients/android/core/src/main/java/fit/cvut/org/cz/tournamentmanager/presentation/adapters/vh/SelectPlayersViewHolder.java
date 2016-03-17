package fit.cvut.org.cz.tournamentmanager.presentation.adapters.vh;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SelectableViewHolder;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 15. 3. 2016.
 */
public class SelectPlayersViewHolder extends SelectableViewHolder {

    public TextView name;


    public SelectPlayersViewHolder(View itemView, AbstractSelectableListAdapter adapter) {
        super(itemView, adapter);
        name = (TextView) itemView.findViewById(R.id.tv_name);
    }

    @Override
    protected AppCompatCheckBox findCheckbox(View v) {

        return (AppCompatCheckBox) v.findViewById(R.id.checkbox);
    }


}
