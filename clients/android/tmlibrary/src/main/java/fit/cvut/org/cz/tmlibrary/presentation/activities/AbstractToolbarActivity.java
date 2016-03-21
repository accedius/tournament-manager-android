package fit.cvut.org.cz.tmlibrary.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * Created by Vaclav on 17. 3. 2016.
 * This activity sets up Toolbar for you. It is meant to be a top-level container
 * You should inject your view an then treat it as normal Appcompat Activity if you overide OnCreate don't
 * forget to call super.OnCreate or it won't work
 *
 */
public abstract class AbstractToolbarActivity extends AppCompatActivity {

    /**
     * This activity uses coordinator layout as top level layout in order for Toolbar
     * and floating action button to work properly.
     * Use this method only for inflating your layout. All the findViews do in OnCreate but call super.OnCreate first!!!
     * @param parent viewGroup for inflater
     * @return Layout that will be injected into parent activty Layout
     */
    protected abstract View injectView(ViewGroup parent);

    /**
     * If you want to have FAB in this Activity return it here. Do not forget to give it id.
     * return null if you don't want one.
     * @return
     */
    protected abstract FloatingActionButton getFloatingActionButton();

    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_abstract_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout content = (LinearLayout) findViewById(R.id.content_layout);
        View v = injectView(content);
        if (v != null && content != null)
            content.addView(v);
    }
}
