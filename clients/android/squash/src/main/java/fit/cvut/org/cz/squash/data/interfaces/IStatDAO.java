package fit.cvut.org.cz.squash.data.interfaces;

import android.content.Context;

import fit.cvut.org.cz.squash.data.entities.DStat;

/**
 * Created by Vaclav on 21. 4. 2016.
 */
public interface IStatDAO {

    void insert(Context context, DStat stat);
}
