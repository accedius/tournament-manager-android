package fit.cvut.org.cz.bowling.business.serialization;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.interfaces.ISerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;

public class ParticipantStatSerializer {
    protected static Context context = null;
    protected static ISerializingStrategy strategy = null;
    protected static ParticipantStatSerializer instance = null;
    protected ParticipantStatSerializer(Context context) {
        this.context = context;
    }

    public static ParticipantStatSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            instance = new ParticipantStatSerializer(context);
        }
        return instance;
    }


    public ServerCommunicationItem serialize(ParticipantStat entity) {
        ServerCommunicationItem item = new ServerCommunicationItem(null, "", null, getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setSyncData(serializeSyncData(entity));

        return item;
    }

    public ParticipantStat deserialize(ServerCommunicationItem item) {
        ParticipantStat pStat = new ParticipantStat();
        deserializeSyncData(item.syncData, pStat);
        return pStat;
    }

    public HashMap<String, Object> serializeSyncData(ParticipantStat entity) {
        HashMap<String, Object> hm = new HashMap<>();

        hm.put(Constants.SCORE, String.valueOf(entity.getScore()));
        hm.put(Constants.FRAMES_PLAYED, String.valueOf(entity.getFramesPlayedNumber()));

        return hm;
    }

    public void deserializeSyncData(HashMap<String, Object> syncData, ParticipantStat entity) {
        entity.setScore(Integer.parseInt(String.valueOf(syncData.get(Constants.SCORE))));
        entity.setFramesPlayedNumber(Byte.parseByte(String.valueOf(syncData.get(Constants.FRAME_NUMBER))));
    }

    public String getEntityType() {
        return Constants.PARTICIPANT_STAT;
    }
}