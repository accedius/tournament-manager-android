package fit.cvut.org.cz.tmlibrary.presentation.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Vaclav on 19. 3. 2016.
 * Simple Intent service with added methods to check if service is currently handling requests.
 */
public abstract class AbstractIntentServiceWProgress extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AbstractIntentServiceWProgress(String name) {
        super(name);
    }

    private static boolean isWorking;

    public static boolean isWorking() {
        return isWorking;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isWorking = false;
    }

    /**
     * If when you do not call super when overiding it wont work
     * @param intent start intent
     *
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        isWorking = true;
    }
}
