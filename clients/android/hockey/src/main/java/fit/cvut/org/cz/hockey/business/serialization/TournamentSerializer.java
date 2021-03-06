package fit.cvut.org.cz.hockey.business.serialization;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.Constants;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

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
        List<Player> players = ((ITournamentManager)ManagerFactory.getInstance(context).getEntityManager(Tournament.class)).getTournamentPlayers(entity.getId());
        for (Player p : players) {
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(p));
        }

        /* Serialize Teams */
        List<Team> teams = ((ITeamManager)ManagerFactory.getInstance(context).getEntityManager(Team.class)).getByTournamentId(entity.getId());
        for (Team t : teams) {
            item.subItems.add(TeamSerializer.getInstance(context).serialize(t));
        }

        /* Serialize Matches */
        List<Match> matches = ((IMatchManager)ManagerFactory.getInstance(context).getEntityManager(Match.class)).getByTournamentId(entity.getId());
        for (Match sm : matches) {
            item.subItems.add(MatchSerializer.getInstance(context).serialize(sm));
        }
        return item;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Tournament entity) {
        HashMap<String, Object> hm = super.serializeSyncData(entity);

        /* Serialize Point Configuration */
        PointConfiguration pointConfiguration = ManagerFactory.getInstance(context).getEntityManager(PointConfiguration.class).getById(entity.getId());
        hm.put(Constants.POINT_CONFIGURATION, pointConfiguration);
        return hm;
    }

    @Override
    public Tournament deserialize(ServerCommunicationItem item) {
        Tournament t = new Tournament(item.getId(), item.getUid(), "", null, null, "");
        t.setEtag(item.getEtag());
        t.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, t);
        return t;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Tournament entity) {
        super.deserializeSyncData(syncData, entity);
        String pointConfigurationString = String.valueOf(syncData.get(Constants.POINT_CONFIGURATION));
        JsonReader pointConfigurationJsonReader = new JsonReader(new StringReader(pointConfigurationString.trim()));
        pointConfigurationJsonReader.setLenient(true);
        PointConfiguration pointConfiguration = new Gson().fromJson(pointConfigurationJsonReader, new TypeToken<PointConfiguration>(){}.getType());
        entity.setPointConfiguration(pointConfiguration);
    }
}
