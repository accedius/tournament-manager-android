package fit.cvut.org.cz.tmlibrary.presentation.adapters.vh;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;

/**
 * Created by Vaclav on 16. 3. 2016.
 */
public abstract class SelectableViewHolder extends RecyclerView.ViewHolder{

    public AppCompatCheckBox checkbox;
    public View parent;
    public int position;
    private AbstractSelectableListAdapter adapter;
    protected abstract AppCompatCheckBox findCheckbox(View v);

    public SelectableViewHolder(View itemView, AbstractSelectableListAdapter adapter) {
        super(itemView);
        checkbox = findCheckbox(itemView);
        parent = itemView;
        this.adapter = adapter;
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });

    }

    private void click(){
        adapter.click(checkbox, position);
    }


}
