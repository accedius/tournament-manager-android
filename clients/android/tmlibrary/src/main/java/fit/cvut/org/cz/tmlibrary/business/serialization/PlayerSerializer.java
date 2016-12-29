package fit.cvut.org.cz.tmlibrary.business.serialization;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by kevin on 8.10.2016.
 */
public class PlayerSerializer extends BaseSerializer<Player> {
    protected static PlayerSerializer instance = null;
    protected PlayerSerializer(Context context) {
        this.context = context;
    }

    public static PlayerSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            return new PlayerSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Player entity) {
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));
        return item;
    }

    @Override
    public Player deserialize(ServerCommunicationItem item) {
        Player p = new Player(-1, "", "", "");
        p.setUid(item.getUid());
        p.setEtag(item.getEtag());
        p.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, p);
        return p;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Player entity) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(Constants.NAME, entity.getName());
        hm.put(Constants.EMAIL, entity.getEmail());
        hm.put(Constants.NOTE, entity.getNote());
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Player entity) {
        entity.setName(String.valueOf(syncData.get(Constants.NAME)));
        entity.setEmail(String.valueOf(syncData.get(Constants.EMAIL)));
        entity.setNote(String.valueOf(syncData.get(Constants.NOTE)));
    }

    @Override
    public String getEntityType() {
        return Constants.PLAYER;
    }
}
