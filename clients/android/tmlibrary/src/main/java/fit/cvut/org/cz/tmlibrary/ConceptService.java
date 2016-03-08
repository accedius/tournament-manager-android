package fit.cvut.org.cz.tmlibrary;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Vaclav on 8. 3. 2016.
 */
public class ConceptService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ConceptService(String name) {
        super(name);
    }

    public static final String ARG_DATA = "data";

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Random r = new Random();

        List<ConceptEntity> data = new ArrayList<>();
        for (int i = 0; i< 10; i++){
            ConceptEntity entity = new ConceptEntity("name " + Integer.toString(r.nextInt(100)), i, new InnerEntity("name " + Integer.toString(r.nextInt(100))));
            data.add(entity);
        }

        Intent resultIntnet = new Intent("action");
        intent.putExtra(ARG_DATA, data.toArray());

        sendBroadcast(intent);
    }
}
