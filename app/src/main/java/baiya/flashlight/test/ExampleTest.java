package baiya.flashlight.test;

import android.test.InstrumentationTestCase;

/**
 * Created by BaiYa on 2014/6/28.
 */
public class ExampleTest extends InstrumentationTestCase {

    public void test() throws Exception{
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }

}
