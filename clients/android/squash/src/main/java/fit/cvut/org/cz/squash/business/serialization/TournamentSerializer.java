package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.serialization.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;

/**
 * Created by kevin on 8.10.2016.
 */
public class TournamentSerializer extends fit.cvut.org.cz.tmlibrary.business.serialization.TournamentSerializer {
    protected static TournamentSerializer instance = null;
    protected TournamentSerializer(Context context) {
        super(context);
    }

    public static TournamentSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            instance = new TournamentSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Tournament entity) {
        /* Serialize Tournament itself */
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize Players */
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, entity.getId());
        for (Player p : players) {
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(p));
        }

        /* Serialize Teams */
        ArrayList<Team> teams = ManagersFactory.getInstance().teamsManager.getByTournamentId(context, entity.getId());
        for (Team t : teams) {
            item.subItems.add(TeamSerializer.getInstance(context).serialize(t));
        }

        /* Serialize Matches */
        ArrayList<ScoredMatch> matches = ManagersFactory.getInstance().matchManager.getByTournamentId(context, entity.getId());
        for (ScoredMatch sm : matches) {
            item.subItems.add(MatchSerializer.getInstance(context).serialize(sm));
        }
        return item;
    }

    @Override
    public Tournament deserialize(ServerCommunicationItem item) {
        Tournament t = new Tournament(item.getId(), item.getUid(), "", null, null, "");
        t.setEtag(item.getEtag());
        t.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, t);
        return t;
    }
}
