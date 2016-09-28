package fit.cvut.org.cz.tmlibrary.presentation.listeners;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;

/**
 * Created by kevin on 28.9.2016.
 */
public class PlayerDetailOnClickListener {
    public static View.OnClickListener getListener(final Context context, final Long playerId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(CrossPackageComunicationConstants.CORE, CrossPackageComunicationConstants.ACTIVITY_PLAYER_DETAIL);
                Bundle b = new Bundle();
                b.putLong(CrossPackageComunicationConstants.EXTRA_ID, playerId);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        };
    }
}
