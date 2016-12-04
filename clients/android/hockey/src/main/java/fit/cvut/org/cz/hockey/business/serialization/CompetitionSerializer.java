package fit.cvut.org.cz.hockey.business.serialization;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.serialization.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;

/**
 * Created by kevin on 8.10.2016.
 */
public class CompetitionSerializer extends fit.cvut.org.cz.tmlibrary.business.serialization.CompetitionSerializer {
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
        List<Player> players = ManagerFactory.getInstance(context).competitionManager.getCompetitionPlayers(context, entity.getId());
        PlayerSerializer ps = PlayerSerializer.getInstance(context);
        for (Player p : players) {
            item.subItems.add(ps.serialize(p));
        }

        /* Serialize Tournaments */
        List<Tournament> tournaments = ManagerFactory.getInstance(context).tournamentManager.getByCompetitionId(context, entity.getId());
        TournamentSerializer ts = TournamentSerializer.getInstance(context);
        for (Tournament t : tournaments) {
            item.subItems.add(ts.serialize(t));
        }
        return item;
    }

    @Override
    public Competition deserialize(ServerCommunicationItem item) {
        Competition c = new Competition(item.getId(), item.getUid(), "", null, null, "", CompetitionTypes.teams());
        c.setEtag(item.getEtag());
        deserializeSyncData(item.syncData, c);
        return c;
    }
}
