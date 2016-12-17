package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;

import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.serializers.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 8.10.2016.
 */
public class CompetitionSerializer extends fit.cvut.org.cz.tmlibrary.business.serialization.serializers.CompetitionSerializer {
    protected static CompetitionSerializer instance = null;
    protected CompetitionSerializer(Context context) {
        super(context);
    }

    public static CompetitionSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            instance = new CompetitionSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Competition entity) {
        /* Serialize Competition itself */
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize Players */
        List<Player> players = ((ICompetitionManager)ManagerFactory.getInstance((context)).getEntityManager(Competition.class)).getCompetitionPlayers(entity.getId());
        PlayerSerializer ps = PlayerSerializer.getInstance(context);
        for (Player p : players) {
            item.subItems.add(ps.serialize(p));
        }

        /* Serialize Tournaments */
        List<Tournament> tournaments = ((ITournamentManager)ManagerFactory.getInstance((context)).getEntityManager(Tournament.class)).getByCompetitionId(entity.getId());
        TournamentSerializer ts = TournamentSerializer.getInstance(context);
        for (Tournament t : tournaments) {
            item.subItems.add(ts.serialize(t));
        }
        return item;
    }

    @Override
    public Competition deserialize(ServerCommunicationItem item) {
        // default type is necessary for constructor, but is overridden in deserializeSyncData
        CompetitionType defaultType = CompetitionTypes.teams();
        Competition c = new Competition(item.getId(), item.getUid(), "", null, null, "", defaultType);
        c.setEtag(item.getEtag());
        deserializeSyncData(item.syncData, c);
        return c;
    }
}
