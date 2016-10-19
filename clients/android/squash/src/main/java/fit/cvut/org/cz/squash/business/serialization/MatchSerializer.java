package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ISharedEntitySerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;

/**
 * Created by kevin on 8.10.2016.
 */
public class MatchSerializer extends BaseSerializer<ScoredMatch> {
    protected static Context context = null;
    protected static MatchSerializer instance = null;

    protected MatchSerializer(Context context) {
        this.context = context;
    }

    public static MatchSerializer getInstance(Context context) {
        if (instance == null) {
            instance = new MatchSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(ScoredMatch entity) {
        /* Serialize Match itself */
        ServerCommunicationItem item = new ServerCommunicationItem(entity.getUid(), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        // TODO fix serializing teams and rosters - if there's change in roster among team matches

        /* Serialize Participants */
        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, entity.getTournamentId());
        Competition c = ManagersFactory.getInstance().competitionManager.getById(context, t.getCompetitionId());
        if (c.getType().equals(CompetitionTypes.teams())) {
            Team home = ManagersFactory.getInstance().teamsManager.getById(context, entity.getHomeParticipantId());
            item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(home));
            Team away = ManagersFactory.getInstance().teamsManager.getById(context, entity.getAwayParticipantId());
            item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(away));
        } else {
            Player home = ManagersFactory.getInstance().playerManager.getPlayerById(context, entity.getHomeParticipantId());
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(home));
            Player away = ManagersFactory.getInstance().playerManager.getPlayerById(context, entity.getAwayParticipantId());
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(away));
        }

        return item;
    }

    @Override
    public ScoredMatch deserialize(ServerCommunicationItem item) {
        return null;
    }

    @Override
    public HashMap<String, String> serializeSyncData(ScoredMatch entity) {
        HashMap<String, String> hm = new HashMap<>();
        if (entity.getDate() == null) {
            hm.put("date", null);
        } else {
            hm.put("date", entity.getDate().toString());
        }
        hm.put("played", Boolean.toString(entity.isPlayed()));
        hm.put("note", entity.getNote());
        hm.put("period", Integer.toString(entity.getPeriod()));
        hm.put("round", Integer.toString(entity.getRound()));

        hm.put("score_home", Integer.toString(entity.getHomeScore()));
        hm.put("score_away", Integer.toString(entity.getAwayScore()));

        /* Serialize sets */
        ArrayList<SetRowItem> sets = ManagersFactory.getInstance().statsManager.getSetsForMatch(context, entity.getId());
        hm.put("sets", Integer.toString(sets.size()));
        int set_id = 1;
        for (SetRowItem set : sets) {
            hm.put("set_home_"+set_id, Integer.toString(set.getHomeScore()));
            hm.put("set_away_"+set_id, Integer.toString(set.getAwayScore()));
            set_id++;
        }

        /* Serialize players */
        ArrayList<Player> homePlayers = ManagersFactory.getInstance().statsManager.getPlayersForMatch(context, entity.getId(), "home");
        hm.put("players_home", Integer.toString(homePlayers.size()));

        int homePlayerId = 1;
        for (Player p : homePlayers) {
            hm.put("player_home_"+homePlayerId, p.getUid());
            homePlayerId++;
        }

        ArrayList<Player> awayPlayers = ManagersFactory.getInstance().statsManager.getPlayersForMatch(context, entity.getId(), "away");
        hm.put("players_away", Integer.toString(awayPlayers.size()));

        int awayPlayerId = 1;
        for (Player p : awayPlayers) {
            hm.put("player_away_"+awayPlayerId, p.getUid());
            awayPlayerId++;
        }
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, String> syncData, ScoredMatch entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        try {
            entity.setDate(dateFormat.parse(syncData.get("date")));
        } catch (ParseException e) {}
        entity.setPlayed(Boolean.parseBoolean(syncData.get("played")));
        entity.setNote(syncData.get("note"));
        entity.setPeriod(Integer.parseInt(syncData.get("period")));
        entity.setRound(Integer.parseInt(syncData.get("round")));
    }

    @Override
    public String getEntityType() {
        return "ScoredMatch";
    }
}
