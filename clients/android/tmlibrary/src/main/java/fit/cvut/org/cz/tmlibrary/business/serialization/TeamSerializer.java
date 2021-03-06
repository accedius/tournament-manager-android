package fit.cvut.org.cz.tmlibrary.business.serialization;

import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;

/**
 * Team serializer.
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
        hm.put(Constants.NAME, entity.getName());
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Team entity) {
        entity.setName(String.valueOf(syncData.get(Constants.NAME)));
    }

    @Override
    public String getEntityType() {
        return Constants.TEAM;
    }
}
