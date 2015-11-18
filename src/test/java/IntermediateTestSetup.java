import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testobject.appium.junit.TestObjectTestResultWatcher;

import java.net.URL;

public class IntermediateTestSetup {

    private AppiumDriver driver;

    /* Sets the test name to the name of the test method. */
    @Rule
    public TestName testName = new TestName();

    /* Takes care of sending the result of the tests over to TestObject. */
    @Rule
    public TestObjectTestResultWatcher resultWatcher = new TestObjectTestResultWatcher();

    private final static String EXPECTED_RESULT_FOUR = "4";
    private final static String EXPECTED_RESULT_ERROR = "Error";

    /* This is the setup that will be run before the test. */
    @Before
    public void setUp() throws Exception {

//        String env = System.getenv().toString();
//        String polishedEnv = env.substring(1, env.length() - 1);
//        String[] varList = polishedEnv.split("\\, ");

        String[] varList = new String[1];
        varList[0] = "TEST=123";

        EnvironmentVariablesExporter.writeFile(varList, "environment.txt");

        DesiredCapabilities capabilities = new DesiredCapabilities();

        /* These are the capabilities we must provide to run our test on TestObject. */
        capabilities.setCapability("testobject_api_key", System.getenv("TESTOBJECT_API_KEY")); // API key through env variable
        //capabilities.setCapability("testobject_api_key", "YOUR_API_KEY")); // API key hardcoded

        capabilities.setCapability("testobject_app_id", "1");

//        capabilities.setCapability("testobject_device", System.getenv("TESTOBJECT_DEVICE_ID")); // device id through env variable
        capabilities.setCapability("testobject_device", "Motorola_Moto_G_2nd_gen_real"); // device id hardcoded

        driver = new AndroidDriver(new URL("https://app.testobject.com:443/api/appium/wd/hub"), capabilities);

        resultWatcher.setAppiumDriver(driver);

    }

    /* A simple addition, it expects the correct result to appear in the result field. */
    @Test
    public void twoPlusTwoOperation() throws Exception {

        throw new Exception("derp");

    }

}
