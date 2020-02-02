package fit.cvut.org.cz.bowling.business.serialization;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IRollManager;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;

public class FrameSerializer extends fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer<Frame> {

    protected FrameSerializer(Context ctx) {
        context = ctx;
    }

    protected static FrameSerializer instance = null;

    public static FrameSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            return new FrameSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Frame entity) {
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize rolls */
        for (Roll rl : ((IRollManager) ManagerFactory.getInstance(context).getEntityManager(Roll.class)).getByFrameId(entity.getId())) {
            item.getSubItems().add(RollSerializer.getInstance(context).serialize(rl));
        }

        return item;
    }

    @Override
    public Frame deserialize(ServerCommunicationItem item) {
        Frame frame = new Frame();
        frame.setEtag(item.getEtag());
        frame.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, frame);

        return frame;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Frame entity) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(Constants.MATCH_ID, Long.toString(entity.getMatchId()));
        hm.put(Constants.PARTICIPANT_ID, Long.toString(entity.getParticipantId()));
        hm.put(Constants.FRAME_NUMBER, Byte.toString(entity.getFrameNumber()));
        hm.put(Constants.PLAYER_ID, Long.toString(entity.getPlayerId()));

        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Frame entity) {
        entity.setMatchId(Long.parseLong(String.valueOf(syncData.get(Constants.MATCH_ID))));
        entity.setParticipantId(Long.parseLong(String.valueOf(syncData.get(Constants.PARTICIPANT_ID))));
        entity.setFrameNumber(Byte.parseByte(String.valueOf(syncData.get(Constants.FRAME_NUMBER))));
        entity.setPlayerId(Long.parseLong(String.valueOf(syncData.get(Constants.PLAYER_ID))));
    }

    @Override
    public String getEntityType() {
        return Constants.FRAME;
    }
}
