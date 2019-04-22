package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.loaders.CompetitionLoader;
import fit.cvut.org.cz.bowling.business.serialization.CompetitionSerializer;
import fit.cvut.org.cz.bowling.presentation.BowlingPackage;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;
import fit.cvut.org.cz.bowling.R;

public class BowlingService extends AbstractIntentServiceWProgress {
    public BowlingService() {
        super("Bowling Service");
    }

    @Override
    protected String getActionKey() {
        return CrossPackageConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) throws SQLException {
        String action = intent.getStringExtra(CrossPackageConstants.EXTRA_ACTION);
        String package_name = intent.getStringExtra(CrossPackageConstants.EXTRA_PACKAGE);
        String sportContext = intent.getStringExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        ((BowlingPackage) getApplicationContext()).setSportContext(sportContext);

        switch (action) {
            case CrossPackageConstants.ACTION_GET_STATS: {
                long id = intent.getLongExtra(CrossPackageConstants.EXTRA_ID, -1);
                //AggregatedStatistics ags = ((IStatisticManager) ManagerFactory.getInstance(this).getEntityManager(AggregatedStatistics.class)).getByPlayerId(id);

                ArrayList<PlayerAggregatedStats> statsToSend = new ArrayList<>();
                PlayerAggregatedStats as = new PlayerAggregatedStats();
                /*as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.gp), Long.toString(ags.getMatches()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.g), Long.toString(ags.getGoals()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.a), Long.toString(ags.getAssists()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.p), Long.toString(ags.getPoints()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.pmp), Long.toString(ags.getPlusMinusPoints()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.s), Long.toString(ags.getSaves()), true));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.tp), Long.toString(ags.getTeamPoints()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.w), Long.toString(ags.getWins()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.d), Long.toString(ags.getDraws()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.l), Long.toString(ags.getLosses()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.ag), String.format("%.2f", ags.getAvgGoals()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.ap), String.format("%.2f", ags.getAvgPoints()), false));
                as.addRecord(new PlayerAggregatedStatsRecord(getString(R.string.atp), String.format("%.2f", ags.getAvgTeamPoints()), false));*/
                statsToSend.add(as);

                Intent res = new Intent(sportContext + package_name + action);
                res.putParcelableArrayListExtra(CrossPackageConstants.EXTRA_STATS, statsToSend);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_GET_ALL_COMPETITIONS: {
                Intent res = new Intent(package_name + action);
                List<Competition> competitions = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getAll();
                for (Competition competition : competitions) {
                    competition.setType(CompetitionTypes.competitionTypes()[competition.getTypeId()]);
                }

                res.putExtra(CrossPackageConstants.EXTRA_TYPE, CrossPackageConstants.TYPE_COMPETITION);
                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                res.putParcelableArrayListExtra(CrossPackageConstants.EXTRA_COMPETITION, new ArrayList<>(competitions));
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_GET_COMPETITIONS_BY_PLAYER: {
                Intent res = new Intent(package_name + action);
                long playerId = intent.getLongExtra(CrossPackageConstants.EXTRA_ID, -1);
                ArrayList<Competition> competitions = new ArrayList<>();
                List<Competition> allCompetitions = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getAll();

                boolean containsPlayer;
                for (Competition competition : allCompetitions) {
                    containsPlayer = false;
                    List<Player> players = ((ICompetitionManager) ManagerFactory.getInstance(this).getEntityManager(Competition.class)).getCompetitionPlayers(competition.getId());
                    for (Player player : players) {
                        if (player.getId() == playerId) {
                            containsPlayer = true;
                            break;
                        }
                    }

                    if (containsPlayer)
                        competitions.add(competition);
                }

                for (Competition competition : competitions) {
                    competition.setType(CompetitionTypes.competitionTypes()[competition.getTypeId()]);
                }

                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                res.putParcelableArrayListExtra(CrossPackageConstants.EXTRA_COMPETITION, new ArrayList<>(competitions));
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_DELETE_COMPETITION: {
                Intent res = new Intent(package_name + action);
                long compId = intent.getLongExtra(CrossPackageConstants.EXTRA_ID, -1);
                res.putExtra(CrossPackageConstants.EXTRA_RESULT, ManagerFactory.getInstance(this).getEntityManager(Competition.class).delete(compId));

                res.putExtra(CrossPackageConstants.EXTRA_TYPE, CrossPackageConstants.TYPE_DELETE);
                res.putExtra(CrossPackageConstants.EXTRA_POSITION, intent.getIntExtra(CrossPackageConstants.EXTRA_POSITION, -1));
                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_GET_COMPETITION_SERIALIZED: {
                Intent res = new Intent(package_name + action);
                long compId = intent.getLongExtra(CrossPackageConstants.EXTRA_ID, -1);
                Competition competition = ManagerFactory.getInstance(this).getEntityManager(Competition.class).getById(compId);
                competition.setSportContext(sportContext);
                String json = CompetitionSerializer.getInstance(this).serialize(competition).toJson();

                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                res.putExtra(CrossPackageConstants.EXTRA_NAME, competition.getFilename());
                res.putExtra(CrossPackageConstants.EXTRA_TYPE, CrossPackageConstants.EXTRA_EXPORT);
                res.putExtra(CrossPackageConstants.EXTRA_JSON, json);
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_GET_COMPETITION_IMPORT_INFO: {
                String json = intent.getStringExtra(CrossPackageConstants.EXTRA_JSON);
                Gson gson = new GsonBuilder().serializeNulls().create();
                JsonReader jr = new JsonReader(new StringReader(json.trim()));
                jr.setLenient(true);
                ServerCommunicationItem competition = gson.fromJson(jr, ServerCommunicationItem.class);

                ArrayList<TournamentImportInfo> tournamentsInfo = new ArrayList<>();
                ArrayList<PlayerImportInfo> playersInfo = new ArrayList<>();
                ArrayList<Conflict> playersModified = new ArrayList<>();
                CompetitionImportInfo competitionInfo = CompetitionLoader.getImportInfo(this, getResources(), competition, tournamentsInfo, playersInfo, playersModified);

                Intent res = new Intent(package_name + action);
                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                res.putExtra(CrossPackageConstants.EXTRA_TYPE, CrossPackageConstants.EXTRA_IMPORT_INFO);
                res.putExtra(CrossPackageConstants.EXTRA_JSON, json);
                res.putExtra(ExtraConstants.EXTRA_COMPETITION, competitionInfo);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_TOURNAMENTS, tournamentsInfo);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, playersInfo);
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_CONFLICTS, playersModified);
                sendBroadcast(res);
                break;
            }
            case CrossPackageConstants.ACTION_IMPORT_FILE_COMPETITION: {
                // TODO Begin transaction ?
                /*DatabaseFactory.getInstance().getDatabase(this).beginTransaction();
                try {
                */
                Intent res = new Intent(package_name + action);
                String json = intent.getStringExtra(CrossPackageConstants.EXTRA_JSON);
                Map<String, String> conflictSolutions = (HashMap<String, String>) intent.getExtras().getSerializable(CrossPackageConstants.EXTRA_CONFLICTS);

                Gson gson = new GsonBuilder().serializeNulls().create();
                JsonReader jr = new JsonReader(new StringReader(json.trim()));
                jr.setLenient(true);
                ServerCommunicationItem competition = gson.fromJson(jr, ServerCommunicationItem.class);
                Competition importedCompetition = CompetitionLoader.importCompetition(this, competition, conflictSolutions);
                /*
                } catch(Exception e) {} finally {
                    DatabaseFactory.getInstance().getDatabase(this).setTransactionSuccessful();
                }
                DatabaseFactory.getInstance().getDatabase(this).endTransaction();
                */
                break;
            }
        }
    }
}
