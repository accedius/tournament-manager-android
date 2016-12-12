package fit.cvut.org.cz.squash.business.managers;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class PlayerManager {/*implements IPackagePlayerManager {
    @Override
    public void addPlayerToCompetition(long playerId, long competitionId) {
        DAOFactory.getInstance().playerDAO.addPlayerToCompetition(context, playerId, competitionId);
    }

    @Override
    public void addPlayerToTournament(long playerId, long tournamentId) {
        DAOFactory.getInstance().playerDAO.addPlayerToTournament(context, playerId, tournamentId);
    }

    @Override
    public boolean deletePlayerFromCompetition(long playerId, long competitionId) {
        ArrayList<Tournament> tournaments = ManagersFactory.getInstance().tournamentManager.getByCompetitionId(context,competitionId);
        for (Tournament t : tournaments) {
            ArrayList<Long> playerIds = DAOFactory.getInstance().playerDAO.getPlayerIdsByTournament(context, t.getId());
            if (playerIds.contains(playerId)) return false;
        }
        DAOFactory.getInstance().playerDAO.deletePlayerFromCompetition(context, playerId, competitionId);
        return true;
    }

    @Override
    public boolean deletePlayerFromTournament(Context context, long playerId, long tournamentId) {
        Player p = new Player(playerId, null, null, null);
        if (DAOFactory.getInstance().statDAO.getByPlayerAndTournament(context, playerId, tournamentId, StatsEnum.MATCH_PARTICIPATION).size() != 0) return false;
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersNotInTeams(context, tournamentId);
        if (!players.contains(p)) return false;

        DAOFactory.getInstance().playerDAO.deletePlayerFromTournament(context, playerId, tournamentId);
        return true;
    }

    @Override
    public ArrayList<Player> getPlayersByCompetition(Context context, long competitionId) {
        Map<Long, Player> players = DAOFactory.getInstance().playerDAO.getAllPlayers();
        ArrayList<Long> ids = DAOFactory.getInstance().playerDAO.getPlayerIdsByCompetition(context, competitionId);
        ArrayList<Player> filteredPlayers = new ArrayList<>();

        for (Long id : ids) filteredPlayers.add(players.get(id));

        return filteredPlayers;
    }

    @Override
    public ArrayList<Player> getPlayersByTournament(Context context, long tournamentId) {
        Map<Long, Player> players = DAOFactory.getInstance().playerDAO.getAllPlayers();
        ArrayList<Long> ids = DAOFactory.getInstance().playerDAO.getPlayerIdsByTournament(context, tournamentId);
        ArrayList<Player> filteredPlayers = new ArrayList<>();

        for (Long id : ids) filteredPlayers.add(players.get(id));

        return filteredPlayers;
    }

    @Override
    public ArrayList<Player> getPlayersByParticipant(Context context, long participantId) {
        Map<Long, Player> players = DAOFactory.getInstance().playerDAO.getAllPlayers();
        ArrayList<Long> ids = DAOFactory.getInstance().statDAO.getPlayerIdsForParticipant(context, participantId);
        ArrayList<Player> filtered = new ArrayList<>();

        for (long id : ids) filtered.add(players.get(id));

        return filtered;
    }

    @Override
    public ArrayList<Player> getPlayersByTeam(Context context, long teamId) {
        Map<Long, Player> players = DAOFactory.getInstance().playerDAO.getAllPlayers();
        ArrayList<Long> ids = DAOFactory.getInstance().playerDAO.getPlayerIdsByTeam(context, teamId);
        ArrayList<Player> filteredPlayers = new ArrayList<>();

        for (Long id : ids) filteredPlayers.add(players.get(id));

        return filteredPlayers;
    }

    @Override
    public Player getPlayerById(Context context, long playerId) {
        Map<Long, Player> players = DAOFactory.getInstance().playerDAO.getAllPlayers();
        return players.get(playerId);
    }

    @Override
    public ArrayList<Player> getAllPlayers(Context context) {
        ArrayList<Player> players = new ArrayList<>();
        Map<Long, Player> dPlayers = DAOFactory.getInstance().playerDAO.getAllPlayers();

        for (Long key : dPlayers.keySet()) players.add(dPlayers.get(key));

        return players;
    }

    @Override
    public long insertPlayer(Context context, Player player) {
        ContentValues values = new ContentValues();
        values.put("email", player.getEmail());
        values.put("name", player.getName());
        values.put("note", player.getNote());
        return DAOFactory.getInstance().playerDAO.insertPlayer(context, values);
    }

    @Override
    public void updatePlayer(Context context, Player player) {
        ContentValues values = new ContentValues();
        values.put("email", player.getEmail());
        values.put("name", player.getName());
        values.put("note", player.getNote());
        DAOFactory.getInstance().playerDAO.updatePlayer(context, values);
    }

    @Override
    public ArrayList<Player> getPlayersNotInCompetition(Context context, long competitionId) {
        Map<Long, Player> players = DAOFactory.getInstance().playerDAO.getAllPlayers();
        ArrayList<Long> ids = DAOFactory.getInstance().playerDAO.getPlayerIdsByCompetition(context, competitionId);
        ArrayList<Player> filteredPlayers = new ArrayList<>();

        for (Long id : ids) players.remove(id);
        for (Long id : players.keySet()) filteredPlayers.add(players.get(id));

        return filteredPlayers;
    }

    @Override
    public ArrayList<Player> getPlayersNotInTournament(Context context, long tournamentId) {
        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, tournamentId);
        ArrayList<Player> playersInCompetition = getPlayersByCompetition(context, t.getCompetitionId());
        ArrayList<Player> playersInTournament = getPlayersByTournament(context, tournamentId);

        for (Player p : playersInTournament) playersInCompetition.remove(p);

        return playersInCompetition;
    }

    @Override
    public void updatePlayersInTeam(Context context, long teamId, ArrayList<Player> players) {
        DAOFactory.getInstance().playerDAO.deleteAllPlayersFromTeam(context, teamId);
        for (Player p : players) DAOFactory.getInstance().playerDAO.addPlayerToTeam(context, p.getId(), teamId);
    }

    @Override
    public ArrayList<Player> getPlayersNotInTeams(Context context, long tournamentId) {
        ArrayList<Team> teams = ManagersFactory.getInstance().teamsManager.getByTournamentId(context, tournamentId);
        ArrayList<Player> players = getPlayersByTournament(context, tournamentId);

        for (Team t : teams)
            for (Player p : t.getPlayers())
                players.remove(p);

        return players;
    }

    @Override
    public void updatePlayersInParticipant(Context context, long participantId, long competitionId, long tournamentId, ArrayList<Player> players) {
        DAOFactory.getInstance().statDAO.delete(context, participantId, StatsEnum.MATCH_PARTICIPATION);
        for (Player p : players) {
            DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, competitionId, tournamentId, p.getId(), participantId, 1, -1, 1, StatsEnum.MATCH_PARTICIPATION));
        }
    }

    @Override
    public ArrayList<Player> getPlayersNotInParticipant(Context context, long participantId) {
        return null;
    }*/
}
