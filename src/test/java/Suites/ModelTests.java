package suites;

import databases.daos.ContributionDaoTest;
import models.contributions.HandleContributionTest;
import models.paginations.HandlePaginationTest;
import models.posts.PostContributionTest;
import models.requests.HandleRequestTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PostContributionTest.class,
        HandleContributionTest.class,
        ContributionDaoTest.class,
        HandleRequestTest.class,
        HandlePaginationTest.class
})
public class ModelTests {

}
