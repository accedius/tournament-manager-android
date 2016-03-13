package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionAdapter extends AbstractListAdapter<CompetitionAdapter.CompetitonViewHolder, Competition> {


    public CompetitionAdapter(List<Competition> data) {
        super(data);
    }

    @Override
    public CompetitonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
        //inflate
    }

    @Override
    public void onBindViewHolder(CompetitonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CompetitonViewHolder extends RecyclerView.ViewHolder{

        public CompetitonViewHolder(View itemView) {
            super(itemView);
        }
    }
}
