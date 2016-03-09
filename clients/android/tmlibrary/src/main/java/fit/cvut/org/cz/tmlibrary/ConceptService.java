package fit.cvut.org.cz.tmlibrary;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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
     */
    public ConceptService() {
        super("Concept service");
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

        ArrayList<ConceptEntity> data = new ArrayList<>();
        for (int i = 0; i< 10; i++){
            ConceptEntity entity = new ConceptEntity("name " + Integer.toString(r.nextInt(100)), i, new InnerEntity("name " + Integer.toString(r.nextInt(100))));
            data.add(entity);
        }

        Intent resultIntnet = new Intent("akce");
        resultIntnet.putParcelableArrayListExtra(ARG_DATA, data);
        Log.d("SRVC", "DONE WORK");

        sendBroadcast(resultIntnet);
    }
}
