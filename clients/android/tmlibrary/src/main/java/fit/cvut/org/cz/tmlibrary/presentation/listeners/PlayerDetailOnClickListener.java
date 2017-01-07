package fit.cvut.org.cz.tmlibrary.presentation.listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;

/**
 * Class for Player Detail on click listener.
 */
public class PlayerDetailOnClickListener {
    /**
     * Get on click listener by given player.
     * @param context application context
     * @param playerId id of player
     * @return View.OnClickListener instance
     */
    public static View.OnClickListener getListener(final Context context, final Long playerId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(CrossPackageConstants.CORE, CrossPackageConstants.ACTIVITY_PLAYER_DETAIL);
                intent.putExtra(CrossPackageConstants.EXTRA_ID, playerId);
                context.startActivity(intent);
            }
        };
    }
}
