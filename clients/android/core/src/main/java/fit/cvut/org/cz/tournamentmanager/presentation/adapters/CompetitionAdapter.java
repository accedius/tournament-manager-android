package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionAdapter extends AbstractListAdapter<Competition, CompetitionAdapter.CompetitionViewHolder> {

    private View v;
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
        this.v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_competition, parent, false);
        CompetitionViewHolder holder = new CompetitionViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CompetitionViewHolder holder, int position) {
        Competition competition = data.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        holder.name.setText(competition.getName());

        if (competition.getStartDate() != null) {
            holder.startDate.setText("From: "+dateFormat.format(competition.getStartDate()));
        }
        if (competition.getEndDate() != null) {
            holder.endDate.setText("To: "+dateFormat.format(competition.getEndDate()));
        }

        final long id = competition.getId();

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(package_name, activity_detail_competition);
                Bundle b = new Bundle();

                b.putLong(CrossPackageComunicationConstants.EXTRA_ID, id);
                intent.putExtras(b);
                c.startActivity(intent);
            }
        });

        v.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("Edit");
                menu.add("Delete");
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
