package fit.cvut.org.cz.bowling.business.serialization;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;

public class RollSerializer extends fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer<Roll> {

    protected RollSerializer(Context ctx) {
        context = ctx;
    }

    protected static RollSerializer instance = null;

    public static RollSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            return new RollSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Roll entity) {
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        return item;
    }

    @Override
    public Roll deserialize(ServerCommunicationItem item) {
        Roll roll = new Roll();
        roll.setEtag(item.getEtag());
        roll.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, roll);

        return roll;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Roll entity) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(Constants.FRAME_ID, Long.toString(entity.getFrameId()));
        hm.put(Constants.ROLL_NUMBER, Byte.toString(entity.getRollNumber()));
        hm.put(Constants.POINTS, Byte.toString(entity.getPoints()));
        hm.put(Constants.PLAYER_ID, Long.toString(entity.getPlayerId()));

        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Roll entity) {
        entity.setFrameId(Long.parseLong(String.valueOf(syncData.get(Constants.FRAME_ID))));
        entity.setRollNumber(Byte.parseByte(String.valueOf(syncData.get(Constants.ROLL_NUMBER))));
        entity.setPoints(Byte.parseByte(String.valueOf(syncData.get(Constants.POINTS))));
        entity.setPlayerId(Long.parseLong(String.valueOf(syncData.get(Constants.PLAYER_ID))));
    }

    @Override
    public String getEntityType() {
        return Constants.ROLL;
    }
}
