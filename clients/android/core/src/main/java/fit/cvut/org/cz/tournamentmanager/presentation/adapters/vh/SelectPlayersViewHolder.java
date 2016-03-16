package fit.cvut.org.cz.tournamentmanager.presentation.adapters.vh;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SelectableViewHolder;

/**
 * Created by Vaclav on 15. 3. 2016.
 */
public class SelectPlayersViewHolder extends SelectableViewHolder {


    public SelectPlayersViewHolder(View itemView, AbstractSelectableListAdapter adapter) {
        super(itemView, adapter);
    }

    @Override
    protected AppCompatCheckBox findCheckbox(View v) {
        return null;
    }


}
