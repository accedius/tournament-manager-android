package fit.cvut.org.cz.tmlibrary.presentation.behaviors;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.View;

/**
 * Created by Vaclav on 27. 3. 2016.
 */
public class FABFloatUpBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof TextInputLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
