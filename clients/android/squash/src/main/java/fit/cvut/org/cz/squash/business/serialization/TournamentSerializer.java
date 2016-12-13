package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.Match;
import fit.cvut.org.cz.squash.business.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.serializers.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;

/**
 * Created by kevin on 8.10.2016.
 */
public class TournamentSerializer extends fit.cvut.org.cz.tmlibrary.business.serialization.serializers.TournamentSerializer {
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
        List<Player> players = ManagerFactory.getInstance(context).tournamentManager.getTournamentPlayers(entity.getId());
        for (Player p : players) {
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(p));
        }

        /* Serialize Teams */
        List<Team> teams = ManagerFactory.getInstance(context).teamManager.getByTournamentId(entity.getId());
        for (Team t : teams) {
            item.subItems.add(TeamSerializer.getInstance(context).serialize(t));
        }

        /* Serialize Matches */
        List<Match> matches = ManagerFactory.getInstance(context).matchManager.getByTournamentId(entity.getId());
        for (Match sm : matches) {
            item.subItems.add(MatchSerializer.getInstance(context).serialize(sm));
        }
        return item;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Tournament entity) {
        HashMap<String, Object> hm = super.serializeSyncData(entity);

        /* Serialize Point Configuration */
        PointConfiguration pointConfiguration = ManagerFactory.getInstance(context).pointConfigManager.getById(entity.getId());
        hm.put("point_configuration", pointConfiguration);
        return hm;
    }

    @Override
    public Tournament deserialize(ServerCommunicationItem item) {
        Tournament tournament = new Tournament(item.getId(), item.getUid(), "", null, null, "");
        tournament.setEtag(item.getEtag());
        tournament.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, tournament);
        return tournament;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Tournament entity) {
        super.deserializeSyncData(syncData, entity);
        PointConfiguration pointConfiguration = new Gson().fromJson(String.valueOf(syncData.get("point_configuration")), new TypeToken<PointConfiguration>(){}.getType());
        entity.setPointConfiguration(pointConfiguration);
    }
}
