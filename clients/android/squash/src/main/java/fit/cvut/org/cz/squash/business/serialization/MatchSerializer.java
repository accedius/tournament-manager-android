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
import fit.cvut.org.cz.squash.business.entities.Match;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
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

        /* Serialize Participants */
        Tournament t = ManagerFactory.getInstance(context).tournamentManager.getById(entity.getTournamentId());
        Competition c = ManagerFactory.getInstance(context).competitionManager.getById(t.getCompetitionId());
        if (c.getType().equals(CompetitionTypes.teams())) {
            Team home = ManagerFactory.getInstance(context).teamManager.getById(entity.getHomeParticipantId());
            item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(home));
            Team away = ManagerFactory.getInstance(context).teamManager.getById(entity.getAwayParticipantId());
            item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(away));
        } else {
            Player home = ManagerFactory.getInstance(context).corePlayerManager.getPlayerById(entity.getHomeParticipantId());
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(home));
            Player away = ManagerFactory.getInstance(context).corePlayerManager.getPlayerById(entity.getAwayParticipantId());
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
            hm.put("date", null);
        } else {
            hm.put("date", DateFormatter.getInstance().getDBDateFormat().format(entity.getDate()));
        }
        hm.put("played", entity.isPlayed());
        hm.put("note", entity.getNote());
        hm.put("period", String.valueOf(entity.getPeriod()));
        hm.put("round", String.valueOf(entity.getRound()));

        hm.put("score_home", String.valueOf(entity.getHomeWonSets()));
        hm.put("score_away", String.valueOf(entity.getAwayWonSets()));

        /* Serialize sets */
        List<SetRowItem> sets = ManagerFactory.getInstance(context).statisticManager.getMatchSets(entity.getId());
        hm.put("sets", sets);
        hm.put("sets_number", String.valueOf(entity.getSetsNumber()));

        /* Serialize players */
        Participant home = null, away = null;
        for (Participant participant : entity.getParticipants()) {
            if (ParticipantType.home.toString().equals(participant.getRole()))
                home = participant;
            else if (ParticipantType.away.toString().equals(participant.getRole()))
                away = participant;
        }

        Map<Long, Player> playerMap = ManagerFactory.getInstance(context).corePlayerManager.getAllPlayers();
        List<PlayerStat> homePlayers = ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(home.getId());
        for (PlayerStat stat : homePlayers) {
            stat.setUid(playerMap.get(stat.getPlayerId()).getUid());
        }
        hm.put("players_home", homePlayers);

        List<PlayerStat> awayPlayers = ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(away.getId());
        for (PlayerStat stat : awayPlayers) {
            stat.setUid(playerMap.get(stat.getPlayerId()).getUid());
        }
        hm.put("players_away", awayPlayers);
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Match entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        try {
            entity.setDate(dateFormat.parse((String)syncData.get("date")));
        } catch (ParseException e) {} catch (NullPointerException e) {}
        entity.setPlayed((boolean)syncData.get("played"));
        entity.setSetsNumber(Integer.valueOf(String.valueOf(syncData.get("sets_number"))));

        entity.setNote((String)syncData.get("note"));
        entity.setPeriod(Integer.valueOf(String.valueOf(syncData.get("period"))));
        entity.setRound(Integer.valueOf(String.valueOf(syncData.get("round"))));

        List<SetRowItem> sets = new Gson().fromJson(String.valueOf(syncData.get("sets")), new TypeToken<List<SetRowItem>>(){}.getType());
        entity.setSets(sets);

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
