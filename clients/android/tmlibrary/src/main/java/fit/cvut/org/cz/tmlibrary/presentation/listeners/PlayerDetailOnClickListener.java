package fit.cvut.org.cz.tmlibrary.presentation.listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;

/**
 * Created by kevin on 28.9.2016.
 */
public class PlayerDetailOnClickListener {
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
