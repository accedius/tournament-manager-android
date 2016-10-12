package fit.cvut.org.cz.tmlibrary.presentation.listeners;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;

/**
 * Created by kevin on 28.9.2016.
 */
public class PlayerDetailOnClickListener {
    public static View.OnClickListener getListener(final Context context, final Long playerId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(CrossPackageCommunicationConstants.CORE, CrossPackageCommunicationConstants.ACTIVITY_PLAYER_DETAIL);
                Bundle b = new Bundle();
                b.putLong(CrossPackageCommunicationConstants.EXTRA_ID, playerId);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        };
    }
}
