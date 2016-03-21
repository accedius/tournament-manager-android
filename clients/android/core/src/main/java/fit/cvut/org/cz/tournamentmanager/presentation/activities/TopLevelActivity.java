package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 18. 3. 2016.
 */
public class TopLevelActivity extends AbstractToolbarActivity {
    @Override
    protected View injectView(ViewGroup parent) {
        return LayoutInflater.from(this).inflate(R.layout.activity_top_level, parent, false);
    }

    @Override
    protected FloatingActionButton getFloatingActionButton() {
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

}
