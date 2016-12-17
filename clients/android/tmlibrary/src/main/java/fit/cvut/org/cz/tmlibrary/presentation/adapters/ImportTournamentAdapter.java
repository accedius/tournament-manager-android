package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;

/**
 * Created by kevin on 28.10.2016.
 */
public class ImportTournamentAdapter extends AbstractListAdapter<TournamentImportInfo, ImportTournamentAdapter.ImportTournamentViewHolder> {
    private CompetitionImportInfo competitionInfo;

    public ImportTournamentAdapter(CompetitionImportInfo competitionInfo) {
        this.competitionInfo = competitionInfo;
    }

    @Override
    public ImportTournamentAdapter.ImportTournamentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_import_tournament, parent, false);
        ImportTournamentViewHolder holder = new ImportTournamentViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImportTournamentAdapter.ImportTournamentViewHolder holder, int position) {
        TournamentImportInfo tournament = data.get(position);

        holder.name.setText(tournament.getName());
        String info;
        if (competitionInfo.getType().equals(CompetitionTypes.teams())) {
            info = tournament.getPlayersCnt() + " players, " + tournament.getTeamsCnt() + " teams, " + tournament.getMatchesCnt() + " matches";
        } else {
            info = tournament.getPlayersCnt() + " players, " + tournament.getMatchesCnt() + " matches";
        }
        holder.info.setText(info);
    }

    public class ImportTournamentViewHolder extends RecyclerView.ViewHolder {
        public TextView name, info;
        public View wholeView;
        public ImportTournamentViewHolder(View itemView) {
            super (itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            info = (TextView) itemView.findViewById(R.id.tv_info);
            wholeView = itemView;
        }
    }
}
