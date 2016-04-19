package fit.cvut.org.cz.squash.data.interfaces;

import android.content.Context;

import fit.cvut.org.cz.squash.data.entities.DPointConfig;

/**
 * Created by Vaclav on 19. 4. 2016.
 */
public interface IPointConfigDAO {

    void insert(Context context, long id);
    void update(Context context, DPointConfig cfg);
    void delete(Context context, long id);
    DPointConfig getById(Context context, long id);

}
