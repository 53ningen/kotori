package suites;

import databases.daos.ContributionDaoTest;
import models.posts.PostContentModelTest;
import models.posts.PostDBTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PostContentModelTest.class,
        PostDBTest.class,
        ContributionDaoTest.class
})
public class ModelTests {

}
