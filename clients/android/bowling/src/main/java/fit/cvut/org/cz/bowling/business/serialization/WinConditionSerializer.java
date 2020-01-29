package fit.cvut.org.cz.bowling.business.serialization;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;

public class WinConditionSerializer {
    protected static Context context = null;
    protected static ISerializingStrategy strategy = null;
    protected static WinConditionSerializer instance = null;
    protected WinConditionSerializer(Context context) {
        this.context = context;
    }

    public static WinConditionSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            instance = new WinConditionSerializer(context);
        }
        return instance;
    }


    public ServerCommunicationItem serialize(WinCondition entity) {
        ServerCommunicationItem item = new ServerCommunicationItem(String.valueOf(entity.getId()), "", null, getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setSyncData(serializeSyncData(entity));

        return item;
    }

    public WinCondition deserialize(ServerCommunicationItem item) {
        WinCondition winCondition = new WinCondition();
        deserializeSyncData(item.syncData, winCondition);
        return winCondition;
    }

    public HashMap<String, Object> serializeSyncData(WinCondition entity) {
        HashMap<String, Object> hm = new HashMap<>();

        hm.put(Constants.WIN_CONDITION_ID, String.valueOf(entity.getWinCondition()));

        return hm;
    }

    public void deserializeSyncData(HashMap<String, Object> syncData, WinCondition entity) {
        entity.setWinCondition(Integer.parseInt(String.valueOf(syncData.get(Constants.WIN_CONDITION_ID))));
    }

    public String getEntityType() {
        return Constants.WIN_CONDITION;
    }
}
