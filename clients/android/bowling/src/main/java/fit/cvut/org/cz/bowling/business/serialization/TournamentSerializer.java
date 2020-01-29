package fit.cvut.org.cz.bowling.business.serialization;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IWinConditionManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.Constants;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

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

        /* Serialize Point Configurations */
        List<PointConfiguration> pointConfigurations = ((IPointConfigurationManager)ManagerFactory.getInstance(context).getEntityManager(PointConfiguration.class)).getByTournamentId(entity.getId());
        for (PointConfiguration pc : pointConfigurations) {
            item.getSubItems().add(PointConfigurationSerializer.getInstance(context).serialize(pc));
        }

        /* Serialize Win Condition */
        WinCondition wc = ((IWinConditionManager)ManagerFactory.getInstance(context).getEntityManager(WinCondition.class)).getByTournamentId(entity.getId());
        item.getSubItems().add(WinConditionSerializer.getInstance(context).serialize(wc));

        return item;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Tournament entity) {
        HashMap<String, Object> hm = super.serializeSyncData(entity);

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
        /*String pointConfigurationString = String.valueOf(syncData.get(Constants.POINT_CONFIGURATION));
        JsonReader pointConfigurationJsonReader = new JsonReader(new StringReader(pointConfigurationString.trim()));
        pointConfigurationJsonReader.setLenient(true);
        PointConfiguration pointConfiguration = new Gson().fromJson(pointConfigurationJsonReader, new TypeToken<PointConfiguration>(){}.getType());
        entity.setPointConfiguration(pointConfiguration);*/
    }
}
