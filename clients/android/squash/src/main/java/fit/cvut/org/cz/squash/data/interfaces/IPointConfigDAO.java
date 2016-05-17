package fit.cvut.org.cz.squash.data.interfaces;

import android.content.Context;

import fit.cvut.org.cz.squash.data.entities.DPointConfig;

/**
 * Created by Vaclav on 19. 4. 2016.
 */
public interface IPointConfigDAO {
    /**
     * insert point config for tournamet with specified id
     * @param context
     * @param id
     */
    void insert(Context context, long id);

    /**
     * Update specified cfg to match its values in data source
     * @param context
     * @param cfg
     */
    void update(Context context, DPointConfig cfg);

    /**
     * Delete cfg with specified id from data source
     * @param context
     * @param id
     */
    void delete(Context context, long id);

    /**
     *
     * @param context
     * @param id
     * @return Config with specified id
     */
    DPointConfig getById(Context context, long id);

}
