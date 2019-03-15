package shep.mysolution;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ LruCacheIntTest.class, LruCacheStringTest.class })
public class AllTests {

}
