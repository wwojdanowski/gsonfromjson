package gsonfromjson;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		format = "pretty",
		glue = "src/test/groovy"
)
public class RunCukeTest {

}
