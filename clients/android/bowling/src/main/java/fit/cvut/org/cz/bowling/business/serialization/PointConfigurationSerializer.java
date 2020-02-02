package fit.cvut.org.cz.bowling.business.serialization;

import android.content.Context;

import java.util.HashMap;

import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;

public class PointConfigurationSerializer extends fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer<PointConfiguration> {

    protected PointConfigurationSerializer(Context ctx) {
        context = ctx;
    }

    protected static PointConfigurationSerializer instance = null;

    public static PointConfigurationSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            return new PointConfigurationSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(PointConfiguration entity) {
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        return item;
    }

    @Override
    public PointConfiguration deserialize(ServerCommunicationItem item) {
        PointConfiguration pc = new PointConfiguration(item.getId(), item.getUid(), 0, 0, null);
        pc.setEtag(item.getEtag());
        pc.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, pc);

        return pc;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(PointConfiguration entity) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(Constants.SIDES_NUMBER, Long.toString(entity.getSidesNumber()));
        hm.put(Constants.PLACE_POINTS, entity.placePoints);
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, PointConfiguration entity) {
        entity.setSidesNumber(Long.parseLong(String.valueOf(syncData.get(Constants.SIDES_NUMBER))));
        entity.placePoints = String.valueOf(syncData.get(Constants.PLACE_POINTS));
    }

    @Override
    public String getEntityType() {
        return Constants.POINT_CONFIGURATION;
    }
}
