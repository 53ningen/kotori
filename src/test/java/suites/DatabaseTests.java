package suites;

import databases.daos.*;

import databases.entities.ContributionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContributionDaoTest.class,
        NGWordDaoTest.class,
        NGUserDaoTest.class,
        UserDaoTest.class,
        AutoLoginDaoTest.class,
        ContributionTest.class
})
public class DatabaseTests {
}
