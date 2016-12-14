package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public class TeamAdapter extends AbstractListAdapter<Team, TeamAdapter.TeamViewHolder> {
    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeamViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_team, parent, false));
    }

    protected void setOnClickListeners(View v, long teamId, int position, String name) {}

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        Team team = data.get(position);
        setOnClickListeners(holder.wholeView, team.getId(), position, team.getName());

        holder.teamName.setText(team.getName());
        if (team.getPlayers().size() > 0) {
            holder.playerNames.setVisibility(View.VISIBLE);
            holder.playerNames.setText("");
            holder.playerNames.append(team.getPlayers().get(0).getName());
            for (int i = 1; i<team.getPlayers().size();i++)
                holder.playerNames.append(String.format(", %s", team.getPlayers().get(i).getName()));
        } else
            holder.playerNames.setVisibility(View.GONE);
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder{
        public TextView teamName, playerNames;
        public View wholeView;

        public TeamViewHolder(View itemView) {
            super(itemView);
            teamName = (TextView) itemView.findViewById(R.id.tv_name);
            playerNames = (TextView) itemView.findViewById(R.id.tv_players);
            wholeView = itemView;
        }
    }
}
