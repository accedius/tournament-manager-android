package fit.cvut.org.cz.tournamentmanager.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.CPConstants;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;
import fit.cvut.org.cz.tournamentmanager.business.ManagerFactory;
import fit.cvut.org.cz.tournamentmanager.presentation.PackagesInfo;

/**
 * Created by kevin on 4.4.2016.
 */
public class PlayerService extends AbstractIntentServiceWProgress {
    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_PLAYER = "extra_player";
    public static final String EXTRA_PLAYERS = "extra_players";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_RESULT = "extra_result";

    public static final String ACTION_DELETE = "fit.cvut.org.cz.tournamentmanager.presentation.services.delete_player";
    public static final String ACTION_CREATE = "fit.cvut.org.cz.tournamentmanager.presentation.services.new_player";
    public static final String ACTION_GET_BY_ID = "fit.cvut.org.cz.tournamentmanager.presentation.services.get_player_by_id";
    public static final String ACTION_GET_ALL = "fit.cvut.org.cz.tournamentmanager.presentation.services.get_all_players";
    public static final String ACTION_UPDATE = "fit.cvut.org.cz.tournamentmanager.presentation.services.update_player";

    public PlayerService() {
        super("Player Service");
    }

    private boolean existsCompetitionsForPlayer(String packageName, String sportName, String content) {
        String uri = "content://"+packageName+".data/"+sportName+content;
        Uri myUri = Uri.parse(uri);
        Cursor cur = getContentResolver().query(myUri, null, null, null, null);
        if (cur == null)
            return false;

        if (cur.getCount() > 0)
            return true;

        return false;
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, PlayerService.class);
        intent.putExtra(EXTRA_ACTION, action);
        return intent;
    }

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) throws SQLException {
        String action = intent.getStringExtra(EXTRA_ACTION);

        switch (action){
            case ACTION_CREATE:{
                Player c = intent.getParcelableExtra(EXTRA_PLAYER);
                ManagerFactory.getInstance(this).getEntityManager(Player.class).insert(c);
                break;
            }
            case ACTION_GET_ALL:{
                Intent result = new Intent();
                result.setAction(ACTION_GET_ALL);
                ArrayList<Player> players = new ArrayList<>();
                players.addAll(ManagerFactory.getInstance(this).getEntityManager(Player.class).getAll());
                result.putExtra(EXTRA_PLAYERS, players);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_BY_ID:{
                Intent result = new Intent();
                result.setAction(ACTION_GET_BY_ID);
                Player p = ManagerFactory.getInstance(this).getEntityManager(Player.class).getById(intent.getLongExtra(EXTRA_ID, -1));
                result.putExtra(EXTRA_PLAYER, p);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE:{
                Player p = intent.getParcelableExtra(EXTRA_PLAYER);
                ManagerFactory.getInstance(this).getEntityManager(Player.class).update(p);
                break;
            }
            case ACTION_DELETE: {
                Intent result = new Intent(action);
                Long playerId = intent.getLongExtra(EXTRA_ID, -1);
                int position = intent.getIntExtra(EXTRA_POSITION, -1);
                boolean deleted = true;
                Map<String, ApplicationInfo> sportContexts = PackagesInfo.getSportContexts(this);
                for (Map.Entry<String, ApplicationInfo>  sport : sportContexts.entrySet()) {
                    String package_name = sport.getValue().metaData.getString("package_name");
                    if (existsCompetitionsForPlayer(package_name, sport.getKey(), CPConstants.uCompetitionsByPlayer + playerId)) {
                        deleted = false;
                        break;
                    }
                }

                if (deleted)
                    ManagerFactory.getInstance(this).getEntityManager(Player.class).delete(playerId);

                result.putExtra(EXTRA_POSITION, position);
                result.putExtra(EXTRA_RESULT, deleted);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
            }
        }
    }
}
