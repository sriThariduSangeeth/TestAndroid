package app.whatsdone.android;

import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import app.whatsdone.android.model.Contact;
import app.whatsdone.android.utils.ContactUtil;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("app.whatsdone.android", appContext.getPackageName());
    }

    @Test
    public void testContactCleaning(){
        String cleanNo = ContactUtil.getInstance().cleanNo("+94" + "0714411600");
        assertEquals("+94714411600", cleanNo);
    }

    @Test
    public void testContactCleaningLocal(){
        String cleanNo = ContactUtil.getInstance().cleanNo( "0714411600");
        assertEquals("+94714411600", cleanNo);
    }

    @Test
    public void testContactCleaningCleaned(){
        String cleanNo = ContactUtil.getInstance().cleanNo( "+94714411600");
        assertEquals("+94714411600", cleanNo);
    }
}
