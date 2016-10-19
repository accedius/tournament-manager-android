package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
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
        if (instance == null) {
            instance = new CompetitionSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Competition entity) {
        /* Serialize Competition itself */
        ServerCommunicationItem item = new ServerCommunicationItem(entity.getUid(), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize Players */
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByCompetition(context, entity.getId());
        PlayerSerializer ps = PlayerSerializer.getInstance(context);
        for (Player p : players) {
            item.subItems.add(ps.serialize(p));
        }

        /* Serialize Tournaments */
        ArrayList<Tournament> tournaments = ManagersFactory.getInstance().tournamentManager.getByCompetitionId(context, entity.getId());
        TournamentSerializer ts = TournamentSerializer.getInstance(context);
        for (Tournament t : tournaments) {
            item.subItems.add(ts.serialize(t));
        }
        return item;
    }

    @Override
    public Competition deserialize(ServerCommunicationItem item) {
        Competition c = new Competition(item.getId(), item.getUid(), "", null, null, "", null);
        c.setEtag(item.getEtag());
        deserializeSyncData(item.syncData, c);
        return c;
    }
}
