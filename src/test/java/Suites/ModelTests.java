package suites;

import databases.daos.ContributionDaoTest;
import models.posts.PostContributionTest;
import models.posts.OperateDBTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PostContributionTest.class,
        OperateDBTest.class,
        ContributionDaoTest.class
})
public class ModelTests {

}
