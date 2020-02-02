package fit.cvut.org.cz.bowling.business.serialization;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;

public class PlayerStatSerializer {
    protected static Context context = null;
    protected static ISerializingStrategy strategy = null;
    protected static PlayerStatSerializer instance = null;
    protected PlayerStatSerializer(Context context) {
        this.context = context;
    }

    public static PlayerStatSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            instance = new PlayerStatSerializer(context);
        }
        return instance;
    }


    public ServerCommunicationItem serialize(PlayerStat entity) {
        ServerCommunicationItem item = new ServerCommunicationItem(String.valueOf(entity.getPlayerId()), "", null, getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setSyncData(serializeSyncData(entity));

        return item;
    }

    public PlayerStat deserialize(ServerCommunicationItem item) {
        PlayerStat pStat = new PlayerStat();
        deserializeSyncData(item.syncData, pStat);
        return pStat;
    }

    public HashMap<String, Object> serializeSyncData(PlayerStat entity) {
        HashMap<String, Object> hm = new HashMap<>();

        hm.put(Constants.STRIKES, String.valueOf(entity.getStrikes()));
        hm.put(Constants.SPARES, String.valueOf(entity.getSpares()));
        hm.put(Constants.POINTS, String.valueOf(entity.getPoints()));
        hm.put(Constants.FRAMES_PLAYED, String.valueOf(entity.getFramesPlayedNumber()));

        return hm;
    }

    public void deserializeSyncData(HashMap<String, Object> syncData, PlayerStat entity) {
        entity.setStrikes(Integer.parseInt(String.valueOf(syncData.get(Constants.STRIKES))));
        entity.setSpares(Integer.parseInt(String.valueOf(syncData.get(Constants.SPARES))));
        entity.setPoints(Integer.parseInt(String.valueOf(syncData.get(Constants.POINTS))));
        entity.setFramesPlayedNumber(Byte.parseByte(String.valueOf(syncData.get(Constants.FRAMES_PLAYED))));
    }

    public String getEntityType() {
        return Constants.PLAYER_STAT;
    }
}
