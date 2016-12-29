package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.SAggregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.managers.interfaces.IStatisticManager;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.Constants;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;

/**
 * Created by kevin on 8.10.2016.
 */
public class MatchSerializer extends BaseSerializer<Match> {
    private static final String SETS = "sets";
    private static final String SETS_NUMBER= "sets_number";

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

        /* Serialize Participants */
        Tournament t = ManagerFactory.getInstance(context).getEntityManager(Tournament.class).getById(entity.getTournamentId());
        Competition c = ManagerFactory.getInstance(context).getEntityManager(Competition.class).getById(t.getCompetitionId());
        if (c.getType().equals(CompetitionTypes.teams())) {
            Team home = ManagerFactory.getInstance(context).getEntityManager(Team.class).getById(entity.getHomeParticipantId());
            item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(home));
            Team away = ManagerFactory.getInstance(context).getEntityManager(Team.class).getById(entity.getAwayParticipantId());
            item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(away));
        } else {
            Player home = ManagerFactory.getInstance(context).getEntityManager(Player.class).getById(entity.getHomeParticipantId());
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(home));
            Player away = ManagerFactory.getInstance(context).getEntityManager(Player.class).getById(entity.getAwayParticipantId());
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(away));
        }

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

        /* Serialize sets */
        List<SetRowItem> sets = ((IStatisticManager)ManagerFactory.getInstance(context).getEntityManager(SAggregatedStats.class)).getMatchSets(entity.getId());
        hm.put(SETS, sets);
        hm.put(SETS_NUMBER, String.valueOf(entity.getSetsNumber()));

        /* Serialize players */
        Participant home = null, away = null;
        for (Participant participant : entity.getParticipants()) {
            if (ParticipantType.home.toString().equals(participant.getRole()))
                home = participant;
            else if (ParticipantType.away.toString().equals(participant.getRole()))
                away = participant;
        }

        Map<Long, Player> playerMap = ((IPackagePlayerManager)ManagerFactory.getInstance(context).getEntityManager(Player.class)).getMapAll();
        List<PlayerStat> homePlayers = ((IPlayerStatManager)ManagerFactory.getInstance(context).getEntityManager(PlayerStat.class)).getByParticipantId(home.getId());
        for (PlayerStat stat : homePlayers) {
            stat.setUid(playerMap.get(stat.getPlayerId()).getUid());
        }
        hm.put(Constants.PLAYERS_HOME, homePlayers);

        List<PlayerStat> awayPlayers = ((IPlayerStatManager)ManagerFactory.getInstance(context).getEntityManager(PlayerStat.class)).getByParticipantId(away.getId());
        for (PlayerStat stat : awayPlayers) {
            stat.setUid(playerMap.get(stat.getPlayerId()).getUid());
        }
        hm.put(Constants.PLAYERS_AWAY, awayPlayers);
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Match entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        try {
            entity.setDate(dateFormat.parse((String)syncData.get(Constants.DATE)));
        } catch (ParseException e) {} catch (NullPointerException e) {}
        entity.setPlayed((boolean)syncData.get(Constants.PLAYED));
        entity.setSetsNumber(Integer.valueOf(String.valueOf(syncData.get(SETS_NUMBER))));

        entity.setNote((String)syncData.get(Constants.NOTE));
        entity.setPeriod(Integer.valueOf(String.valueOf(syncData.get(Constants.PERIOD))));
        entity.setRound(Integer.valueOf(String.valueOf(syncData.get(Constants.ROUND))));

        Gson gson = new Gson();
        List<SetRowItem> sets = gson.fromJson(String.valueOf(syncData.get(SETS)), new TypeToken<List<SetRowItem>>(){}.getType());
        entity.setSets(sets);

        List<PlayerStat> homePlayers = gson.fromJson(String.valueOf(syncData.get(Constants.PLAYERS_HOME)), new TypeToken<List<PlayerStat>>(){}.getType());
        entity.setHomePlayers(homePlayers);

        List<PlayerStat> awayPlayers = gson.fromJson(String.valueOf(syncData.get(Constants.PLAYERS_AWAY)), new TypeToken<List<PlayerStat>>(){}.getType());
        entity.setAwayPlayers(awayPlayers);
    }

    @Override
    public String getEntityType() {
        return Constants.MATCH;
    }
}
