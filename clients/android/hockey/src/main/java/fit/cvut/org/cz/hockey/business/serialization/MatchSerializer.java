package fit.cvut.org.cz.hockey.business.serialization;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.Constants;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;

/**
 * Created by kevin on 8.10.2016.
 */
public class MatchSerializer extends BaseSerializer<Match> {
    private static final String OVERTIME = "overtime";
    private static final String SHOOTOUTS = "shootouts";
    private static final String SCORE_HOME = "score_home";
    private static final String SCORE_AWAY = "score_away";

    protected static MatchSerializer instance = null;
    protected MatchSerializer(Context context) {
        this.context = context;
    }

    public static MatchSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            instance = new MatchSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(Match entity) {
        /* Serialize Match itself */
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize Teams */
        Team home = ManagerFactory.getInstance(context).getEntityManager(Team.class).getById(entity.getHomeParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(home));
        Team away = ManagerFactory.getInstance(context).getEntityManager(Team.class).getById(entity.getAwayParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(away));
        return item;
    }

    @Override
    public Match deserialize(ServerCommunicationItem item) {
        Match sm = new Match();
        sm.setEtag(item.getEtag());
        sm.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, sm);
        return sm;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Match entity) {
        HashMap<String, Object> hm = new HashMap<>();
        if (entity.getDate() == null) {
            hm.put(Constants.DATE, null);
        } else {
            hm.put(Constants.DATE, DateFormatter.getInstance().getDBDateFormat().format(entity.getDate()));
        }
        hm.put(Constants.PLAYED, entity.isPlayed());
        hm.put(Constants.NOTE, entity.getNote());
        hm.put(Constants.PERIOD, String.valueOf(entity.getPeriod()));
        hm.put(Constants.ROUND, String.valueOf(entity.getRound()));

        hm.put(SCORE_HOME, String.valueOf(entity.getHomeScore()));
        hm.put(SCORE_AWAY, String.valueOf(entity.getAwayScore()));

        hm.put(OVERTIME, entity.isOvertime());
        hm.put(SHOOTOUTS, entity.isShootouts());

        /* Serialize rosters and stats */
        Map<Long, Player> playerMap = ((IPackagePlayerManager)ManagerFactory.getInstance(context).getEntityManager(Player.class)).getMapAll();
        for (Participant participant : entity.getParticipants()) {
            if (ParticipantType.home.toString().equals(participant.getRole())) {
                List<PlayerStat> homePlayers = ((IPlayerStatManager)ManagerFactory.getInstance(context).getEntityManager(PlayerStat.class)).getByParticipantId(participant.getId());
                for (fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat stat : homePlayers) {
                    stat.setUid(playerMap.get(stat.getPlayerId()).getUid());
                }
                hm.put(Constants.PLAYERS_HOME, homePlayers);
            } else if (ParticipantType.away.toString().equals(participant.getRole())) {
                List<PlayerStat> awayPlayers = ((IPlayerStatManager)ManagerFactory.getInstance(context).getEntityManager(PlayerStat.class)).getByParticipantId(participant.getId());
                for (fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat stat : awayPlayers) {
                    stat.setUid(playerMap.get(stat.getPlayerId()).getUid());
                }
                hm.put(Constants.PLAYERS_AWAY, awayPlayers);
            }
        }
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Match entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        try {
            entity.setDate(dateFormat.parse(String.valueOf(syncData.get(Constants.DATE))));
        } catch (ParseException e) {} catch (NullPointerException e) {}
        entity.setPlayed((boolean)syncData.get(Constants.PLAYED));
        entity.setNote(String.valueOf(syncData.get(Constants.NOTE)));
        entity.setPeriod(Integer.valueOf(String.valueOf(syncData.get(Constants.PERIOD))));
        entity.setRound(Integer.valueOf(String.valueOf(syncData.get(Constants.ROUND))));

        entity.setHomeScore(Integer.valueOf(String.valueOf(syncData.get(SCORE_HOME))));
        entity.setAwayScore(Integer.valueOf(String.valueOf(syncData.get(SCORE_AWAY))));

        entity.setOvertime((boolean)syncData.get(OVERTIME));
        entity.setShootouts((boolean)syncData.get(SHOOTOUTS));

        List<PlayerStat> homePlayers = new Gson().fromJson(String.valueOf(syncData.get(Constants.PLAYERS_HOME)), new TypeToken<List<PlayerStat>>(){}.getType());
        entity.setHomePlayers(homePlayers);

        List<PlayerStat> awayPlayers = new Gson().fromJson(String.valueOf(syncData.get(Constants.PLAYERS_AWAY)), new TypeToken<List<PlayerStat>>(){}.getType());
        entity.setAwayPlayers(awayPlayers);
    }

    @Override
    public String getEntityType() {
        return Constants.MATCH;
    }
}
