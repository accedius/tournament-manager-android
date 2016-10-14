package fit.cvut.org.cz.tmlibrary.business.serialization;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by kevin on 8.10.2016.
 */
public class PlayerSerializer extends BaseSerializer<Player> {
    protected static Context context = null;
    protected static PlayerSerializer instance = null;

    protected PlayerSerializer(Context context) {
        this.context = context;
    }

    public static PlayerSerializer getInstance(Context context) {
        if (instance == null)
            return new PlayerSerializer(context);
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Player entity) {
        ServerCommunicationItem item = new ServerCommunicationItem(entity.getUid(), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setSyncData(serializeSyncData(entity));
        return item;
    }

    @Override
    public Player deserialize(ServerCommunicationItem item) {
        return null;
    }

    @Override
    public HashMap<String, String> serializeSyncData(Player entity) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("name", entity.getName());
        hm.put("email", entity.getEmail());
        hm.put("note", entity.getNote());
        return hm;
    }

    @Override
    public void deserializeSyncData(String syncData, Player entity) {
        String[] data = new Gson().fromJson(syncData, String[].class);
        entity.setName(data[0]);
        entity.setEmail(data[1]);
        entity.setNote(data[2]);
    }

    @Override
    public String getEntityType() {
        return "Player";
    }
}
