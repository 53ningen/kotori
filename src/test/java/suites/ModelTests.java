package suites;

import models.paginations.HandlePaginationTest;
import models.payloads.HandlePayloadTest;
import models.requests.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContributionRequestTest.class,
        NGUserRequestTest.class,
        NGWordRequestTest.class,
        UserRequestTest.class,
        HandlePayloadTest.class,
        HandleRequestTest.class,
        HandlePaginationTest.class
})
public class ModelTests {

}
