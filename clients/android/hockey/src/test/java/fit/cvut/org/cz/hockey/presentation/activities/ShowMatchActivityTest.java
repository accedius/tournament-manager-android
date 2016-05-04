package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by atgot_000 on 4. 5. 2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ShowMatchActivityTest {

    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void testIntentShouldBeCreated() throws Exception {
        Context context = Mockito.mock( Context.class );
        Intent intent = ShowMatchActivity.newStartIntent( context, 1 );
        assertNotNull( intent );
        Bundle extras = intent.getExtras();
        assertNotNull( extras );
        assertEquals( 1, extras.getLong("match_id") );
    }
}