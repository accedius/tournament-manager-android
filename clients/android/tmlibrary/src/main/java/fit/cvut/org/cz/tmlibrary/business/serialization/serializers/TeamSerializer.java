package fit.cvut.org.cz.tmlibrary.business.serialization.serializers;

import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;

/**
 * Created by kevin on 13.12.2016.
 */

abstract public class TeamSerializer extends BaseSerializer<Team> {
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
