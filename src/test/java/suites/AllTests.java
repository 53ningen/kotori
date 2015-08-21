package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ModelTests.class,
        DatabaseTests.class
})
public class AllTests {
}
