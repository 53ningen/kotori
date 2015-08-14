package suites;

import models.contributions.HandleContributionTest;
import models.paginations.HandlePaginationTest;
import models.payloads.HandlePayloadTest;
import models.posts.deletes.DeleteContributionTest;
import models.posts.deletes.DeleteNGWordTest;
import models.posts.inserts.InsertContributionTest;
import models.posts.updates.UpdateContributionTest;
import models.requests.HandleRequestTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        InsertContributionTest.class,
        UpdateContributionTest.class,
        DeleteContributionTest.class,
        HandleContributionTest.class,
        DeleteNGWordTest.class,
        HandlePayloadTest.class,
        HandleRequestTest.class,
        HandlePaginationTest.class
})
public class ModelTests {

}
