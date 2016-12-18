package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.serializers.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;

/**
 * Created by kevin on 8.10.2016.
 */
public class TeamSerializer extends fit.cvut.org.cz.tmlibrary.business.serialization.serializers.TeamSerializer {
    protected static TeamSerializer instance = null;
    protected TeamSerializer(Context context) {
        this.context = context;
    }

    public static TeamSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            instance = new TeamSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Team entity) {
        /* Serialize Team itself */
        // TODO entity.getUid() instead of strategy
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize players */
        List<Player> teamPlayers = ((ITeamManager)ManagerFactory.getInstance(context).getEntityManager(Team.class)).getTeamPlayers(entity);
        for (Player player : teamPlayers) {
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(player));
        }
        return item;
    }
}
