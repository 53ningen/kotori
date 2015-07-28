package Suites;

import models.PostContentModelTest;
import models.PostDBTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({PostContentModelTest.class, PostDBTest.class})
public class ModelTests {

}
