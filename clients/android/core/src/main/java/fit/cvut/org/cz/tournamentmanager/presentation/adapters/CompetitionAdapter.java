package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionAdapter extends AbstractListAdapter<DCompetition, CompetitionAdapter.CompetitionViewHolder> {

    View v;
    private Context c;

    private String package_name;
    private String activity_detail_competition;


    public CompetitionAdapter(String package_name, String activity_detail_competition, Context c) {
        this.c = c;
        this.package_name = package_name;
        this.activity_detail_competition = activity_detail_competition;
    }

    @Override
    public CompetitionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //inflate
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_competition, parent, false);
        CompetitionViewHolder holder = new CompetitionViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CompetitionViewHolder holder, int position) {
        Competition competition = new Competition(data.get(position));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        holder.name.setText(competition.getName());
        holder.startDate.setText("From: ");
        holder.endDate.setText("to: ");
        holder.name.setText(competition.getName());

        final long id = competition.getId();

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(package_name, activity_detail_competition);
                Bundle b = new Bundle();
                // TODO string kosntanta, definovat pravděpodobně někde bokem
                b.putLong("competition_id", id);
                intent.putExtras(b);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CompetitionViewHolder extends RecyclerView.ViewHolder{
        public TextView name, startDate, endDate;
        public CompetitionViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            startDate = (TextView) itemView.findViewById(R.id.tv_start);
            endDate = (TextView) itemView.findViewById(R.id.tv_end);
        }
    }
}
