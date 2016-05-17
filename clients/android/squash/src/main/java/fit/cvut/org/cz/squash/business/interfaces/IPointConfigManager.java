package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

import fit.cvut.org.cz.squash.business.entities.PointConfig;

/** Class that manages Point configuration for tournament
 * Created by Vaclav on 19. 4. 2016.
 */
public interface IPointConfigManager {
    /**
     * Should insert default configuration for given id
     * @param context
     * @param id of tournament
     */
    void insert(Context context, long id);

    /**
     * Updates given configuration
     * @param context
     * @param cfg
     */
    void update(Context context, PointConfig cfg);

    /**
     * Deletes configuration for given id of tournament
     * @param context
     * @param id of tournament
     */
    void delete(Context context, long id);

    /**
     *
     * @param context
     * @param id of tournament
     * @return Configuratioon with given id
     */
    PointConfig getById(Context context, long id);
}
