package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;

/**
 * Created by kevin on 8.10.2016.
 */
public class TeamSerializer extends BaseSerializer<Team> {
    protected static TeamSerializer instance = null;
    protected TeamSerializer(Context context) {
        this.context = context;
    }

    public static TeamSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            instance = new TeamSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Team entity) {
        /* Serialize Team itself */
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize players */
        List<Player> teamPlayers = ManagerFactory.getInstance(context).teamManager.getTeamPlayers(entity);
        for (Player player : teamPlayers) {
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(player));
        }
        return item;
    }

    @Override
    public Team deserialize(ServerCommunicationItem item) {
        Team team = new Team(-1, "");
        team.setEtag(item.getEtag());
        team.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, team);
        return team;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Team entity) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("name", entity.getName());
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Team entity) {
        entity.setName(String.valueOf(syncData.get("name")));
    }

    @Override
    public String getEntityType() {
        return "Team";
    }
}
