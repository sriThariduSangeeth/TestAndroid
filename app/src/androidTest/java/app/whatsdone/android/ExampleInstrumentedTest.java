package app.whatsdone.android;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.SharedPreferencesUtil;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void persist(){
        HashMap data = new HashMap();
        HashMap map = new HashMap();
        map.put("key", "value");
        data.put("group",map);
        SharedPreferencesUtil.save("data", data);
        HashMap retrieved = SharedPreferencesUtil.get("data");
        HashMap retrievedMap = (HashMap) retrieved.get("group");
        assertEquals(retrievedMap.get("key"), "value");
    }
}
