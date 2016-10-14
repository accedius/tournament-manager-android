package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ISharedEntitySerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;

/**
 * Created by kevin on 8.10.2016.
 */
public class TeamSerializer extends BaseSerializer<Team> {
    protected static Context context = null;
    protected static TeamSerializer instance = null;

    protected TeamSerializer(Context context) {
        this.context = context;
    }

    public static TeamSerializer getInstance(Context context) {
        if (instance == null) {
            instance = new TeamSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Team entity) {
        /* Serialize Team itself */
        ServerCommunicationItem item = new ServerCommunicationItem(entity.getUid(), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize players */
        ArrayList<Player> teamPlayers = ManagersFactory.getInstance().playerManager.getPlayersByTeam(context, entity.getId());
        for (Player p : teamPlayers) {
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(p));
        }
        return item;
    }

    @Override
    public Team deserialize(ServerCommunicationItem item) {
        return null;
    }

    @Override
    public HashMap<String, String> serializeSyncData(Team entity) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("name", entity.getName());
        return hm;
    }

    @Override
    public void deserializeSyncData(String syncData, Team entity) {
        String[] data = new Gson().fromJson(syncData, new TypeToken<ArrayList<String>>(){}.getType());
        entity.setName(data[0]);
    }

    @Override
    public String getEntityType() {
        return "Team";
    }
}
