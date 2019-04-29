package fit.cvut.org.cz.bowling.business.serialization;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;


public class TeamSerializer extends fit.cvut.org.cz.tmlibrary.business.serialization.TeamSerializer {
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
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize players */
        List<Player> teamPlayers = ((ITeamManager)ManagerFactory.getInstance(context).getEntityManager(Team.class)).getTeamPlayers(entity);
        for (Player p : teamPlayers) {
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(p));
        }
        return item;
    }
}
