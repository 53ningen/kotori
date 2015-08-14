package suites;

import databases.daos.ContributionDaoTest;
import databases.daos.NGUserDaoTest;
import databases.daos.NGWordDaoTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContributionDaoTest.class,
        NGWordDaoTest.class,
        NGUserDaoTest.class
})
public class DaoTests {
}
