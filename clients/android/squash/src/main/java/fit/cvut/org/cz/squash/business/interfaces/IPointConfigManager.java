package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import fit.cvut.org.cz.squash.business.entities.PointConfig;

/**
 * Created by Vaclav on 19. 4. 2016.
 */
public interface IPointConfigManager {

    void insert(Context context, long id);
    void update(Context context, PointConfig cfg);
    void delete(Context context, long id);
    PointConfig getById(Context context, long id);
}
