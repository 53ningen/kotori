package suites;

import databases.daos.ContributionDaoTest;
import databases.daos.NGUserDaoTest;
import databases.daos.NGWordDaoTest;

import databases.entities.ContributionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContributionDaoTest.class,
        NGWordDaoTest.class,
        NGUserDaoTest.class,
        ContributionTest.class
})
public class DatabaseTests {
}
