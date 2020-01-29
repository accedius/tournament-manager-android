package fit.cvut.org.cz.bowling.business.serialization;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IFrameManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.business.serialization.strategies.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;


public class MatchSerializer extends BaseSerializer<Match> {
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

        Tournament tournament = ManagerFactory.getInstance(context).getEntityManager(Tournament.class).getById(entity.getTournamentId());
        /* Teams match */
        if (tournament.getTypeId() == TournamentTypes.teams().id) {
            /* Serialize Teams and their ParticipantStats with Players and PlayerStats*/
            for (Participant tPart :((IParticipantManager)ManagerFactory.getInstance(context).getEntityManager(Participant.class)).getByMatchIdWithAllContents(entity.getId())) {
                Team team = ManagerFactory.getInstance(context).getEntityManager(Team.class).getById(tPart.getParticipantId());
                ServerCommunicationItem teamSCI = TeamSerializer.getInstance(context).serializeToMinimal(team);

                List<ServerCommunicationItem> paStats = new ArrayList<>();
                for (ParticipantStat stat : ((IParticipantStatManager)ManagerFactory.getInstance(context).getEntityManager(ParticipantStat.class)).getByMatchId(entity.getId())) {
                    paStats.add(ParticipantStatSerializer.getInstance(context).serialize(stat));
                }

                List<ServerCommunicationItem> players = new ArrayList<>();
                for (Player pl : ((ITeamManager)ManagerFactory.getInstance(context).getEntityManager(Team.class)).getTeamPlayers(team)) {
                    ServerCommunicationItem playerSCI = PlayerSerializer.getInstance(context).serializeToMinimal(pl);
                    for (PlayerStat stat : ((IPlayerStatManager)ManagerFactory.getInstance(context).getEntityManager(PlayerStat.class)).getByParticipantId(tPart.getId())) {
                        if (stat.getPlayerId() == pl.getId())
                            playerSCI.getSubItems().add(PlayerStatSerializer.getInstance(context).serialize(stat));
                    }
                    for (Frame frame : ((IFrameManager)ManagerFactory.getInstance(context).getEntityManager(Frame.class)).getInMatchByPlayerId(entity.getId(), pl.getId())) {
                        playerSCI.getSubItems().add(FrameSerializer.getInstance(context).serialize(frame));
                    }
                    players.add(playerSCI);
                }

                teamSCI.getSubItems().addAll(paStats);
                teamSCI.getSubItems().addAll(players);
                item.getSubItems().add(teamSCI);
            }
        }
        else if (tournament.getTypeId() == TournamentTypes.individuals().id) {
            /* Serialize Players their ParticipantStats and PlayerStats */
            for (Participant participant :((IParticipantManager)ManagerFactory.getInstance(context).getEntityManager(Participant.class)).getByMatchId(entity.getId())) {
                Player player = ManagerFactory.getInstance(context).getEntityManager(Player.class).getById(participant.getParticipantId());
                ServerCommunicationItem playerSCI = PlayerSerializer.getInstance(context).serializeToMinimal(player);

                List<ServerCommunicationItem> paStats = new ArrayList<>();
                for (ParticipantStat stat : ((IParticipantStatManager)ManagerFactory.getInstance(context).getEntityManager(ParticipantStat.class)).getByParticipantId(participant.getId())) {
                    paStats.add(ParticipantStatSerializer.getInstance(context).serialize(stat));
                }

                List<ServerCommunicationItem> plStats = new ArrayList<>();
                for (PlayerStat stat : ((IPlayerStatManager)ManagerFactory.getInstance(context).getEntityManager(PlayerStat.class)).getByParticipantId(participant.getId())) {
                    plStats.add(PlayerStatSerializer.getInstance(context).serialize(stat));
                }

                List<ServerCommunicationItem> frames = new ArrayList<>();
                for (Frame frame : ((IFrameManager)ManagerFactory.getInstance(context).getEntityManager(Frame.class)).getInMatchByPlayerId(entity.getId(), player.getId())) {
                    frames.add(FrameSerializer.getInstance(context).serialize(frame));
                }

                playerSCI.getSubItems().addAll(paStats);
                playerSCI.getSubItems().addAll(plStats);
                playerSCI.getSubItems().addAll(frames);
                item.getSubItems().add(playerSCI);
            }
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
        hm.put(Constants.TRACK_ROLLS, entity.isTrackRolls());
        hm.put(Constants.VALID_FOR_STATS, entity.isValidForStats());

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
        entity.setTrackRolls((boolean)syncData.get(Constants.TRACK_ROLLS));
        entity.setValidForStats((boolean)syncData.get(Constants.VALID_FOR_STATS));
    }

    @Override
    public String getEntityType() {
        return Constants.MATCH;
    }
}
