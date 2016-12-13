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
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;

/**
 * Created by kevin on 8.10.2016.
 */
public class MatchSerializer extends BaseSerializer<Match> {
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
        Team home = ManagerFactory.getInstance(context).teamManager.getById(entity.getHomeParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(home));
        Team away = ManagerFactory.getInstance(context).teamManager.getById(entity.getAwayParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(away));
        return item;
    }

    @Override
    public Match deserialize(ServerCommunicationItem item) {
        Match sm = new Match();
        sm.setEtag(item.getEtag());
        sm.setLastModified(item.getModified());
        sm.setType(CompetitionTypes.teams());
        deserializeSyncData(item.syncData, sm);
        return sm;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Match entity) {
        HashMap<String, Object> hm = new HashMap<>();
        if (entity.getDate() == null) {
            hm.put("date", null);
        } else {
            hm.put("date", DateFormatter.getInstance().getDBDateFormat().format(entity.getDate()));
        }
        hm.put("played", entity.isPlayed());
        hm.put("note", entity.getNote());
        hm.put("period", String.valueOf(entity.getPeriod()));
        hm.put("round", String.valueOf(entity.getRound()));

        hm.put("score_home", String.valueOf(entity.getHomeScore()));
        hm.put("score_away", String.valueOf(entity.getAwayScore()));

        hm.put("overtime", entity.isOvertime());
        hm.put("shootouts", entity.isShootouts());

        /* Serialize rosters and stats */
        Map<Long, Player> playerMap = ManagerFactory.getInstance(context).corePlayerManager.getAllPlayers();
        for (Participant participant : entity.getParticipants()) {
            if (ParticipantType.home.toString().equals(participant.getRole())) {
                List<PlayerStat> homePlayers = ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(participant.getId());
                for (fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat stat : homePlayers) {
                    stat.setUid(playerMap.get(stat.getPlayerId()).getUid());
                }
                hm.put("players_home", homePlayers);
            } else if (ParticipantType.away.toString().equals(participant.getRole())) {
                List<PlayerStat> awayPlayers = ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(participant.getId());
                for (fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat stat : awayPlayers) {
                    stat.setUid(playerMap.get(stat.getPlayerId()).getUid());
                }
                hm.put("players_away", awayPlayers);
            }
        }
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Match entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        try {
            entity.setDate(dateFormat.parse(String.valueOf(syncData.get("date"))));
        } catch (ParseException e) {} catch (NullPointerException e) {}
        entity.setPlayed((boolean)syncData.get("played"));
        entity.setNote(String.valueOf(syncData.get("note")));
        entity.setPeriod(Integer.valueOf(String.valueOf(syncData.get("period"))));
        entity.setRound(Integer.valueOf(String.valueOf(syncData.get("round"))));

        entity.setHomeScore(Integer.valueOf(String.valueOf(syncData.get("score_home"))));
        entity.setAwayScore(Integer.valueOf(String.valueOf(syncData.get("score_away"))));

        entity.setOvertime((boolean)syncData.get("overtime"));
        entity.setShootouts((boolean)syncData.get("shootouts"));

        List<PlayerStat> homePlayers = new Gson().fromJson(String.valueOf(syncData.get("players_home")), new TypeToken<List<PlayerStat>>(){}.getType());
        entity.setHomePlayers(homePlayers);

        List<PlayerStat> awayPlayers = new Gson().fromJson(String.valueOf(syncData.get("players_away")), new TypeToken<List<PlayerStat>>(){}.getType());
        entity.setAwayPlayers(awayPlayers);
    }

    @Override
    public String getEntityType() {
        return "Match";
    }
}
