package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by atgot_000 on 4. 5. 2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class ShowMatchActivityTest {
    @Test
    public void IntentShouldBeCreated() throws Exception {
        Context context = Mockito.mock(Context.class);
        Intent intent = ShowMatchActivity.newStartIntent(context, 1);
        assertNotNull(intent);
        Bundle extras = intent.getExtras();
        assertNotNull(extras);
        assertEquals(1, extras.getLong("match_id"));
    }
}