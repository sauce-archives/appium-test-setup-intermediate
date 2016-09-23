import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testobject.api.TestObjectClient;
import org.testobject.appium.junit.TestObjectTestResultWatcher;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntermediateTestSetup {

    private AppiumDriver driver;

    /* Sets the test name to the name of the test method. */
    @Rule
    public TestName testName = new TestName();

    /* Takes care of sending the result of the tests over to TestObject. */
    @Rule
    public TestObjectTestResultWatcher resultWatcher = new TestObjectTestResultWatcher();

    private final static String EXPECTED_RESULT_FOUR = "4";
    private final static String EXPECTED_RESULT_NAN = "NaN";

    /* This is the setup that will be run before the test. */
    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        /* These are the capabilities we must provide to run our test on TestObject. */
        capabilities.setCapability("testobject_api_key", System.getenv("TESTOBJECT_API_KEY")); // API key through env variable
//        capabilities.setCapability("testobject_api_key", "YOUR_API_KEY"); // API key hardcoded

        capabilities.setCapability("testobject_app_id", "1");

        capabilities.setCapability("testobject_device", System.getenv("TESTOBJECT_DEVICE_ID")); // device id through env variable
//        capabilities.setCapability("testobject_device", "Motorola_Moto_G_2nd_gen_real"); // device id hardcoded

		String cacheDevice = System.getenv("TESTOBJECT_CACHE_DEVICE");
		if (cacheDevice != null && cacheDevice.trim().isEmpty() == false) {
			capabilities.setCapability("testobject_cache_device", cacheDevice);
		}

        // We generate a random UUID for later lookup in logs for debugging
        String testUUID = UUID.randomUUID().toString();
        System.out.println("TestUUID: " + testUUID);
        capabilities.setCapability("testobject_testuuid", testUUID);

        String testobjectAppiumEndpoint = Optional.ofNullable(System.getenv("APPIUM_URL"))
                .orElse("https://app.testobject.com:443/api/appium/wd/hub");
        driver = new AndroidDriver(new URL(testobjectAppiumEndpoint), capabilities);

        resultWatcher.setAppiumDriver(driver);

    }

    /* A simple addition, it expects the correct result to appear in the result field. */
    @Test
    public void twoPlusTwoOperation() {

        /* Get the elements. */
        MobileElement buttonTwo = (MobileElement)(driver.findElement(By.id("net.ludeke.calculator:id/digit2")));
        MobileElement buttonPlus = (MobileElement)(driver.findElement(By.id("net.ludeke.calculator:id/plus")));
        MobileElement buttonEquals = (MobileElement)(driver.findElement(By.id("net.ludeke.calculator:id/equal")));
        MobileElement resultField = (MobileElement)(driver.findElement(By.xpath("//android.widget.EditText[1]")));

        /* Add two and two. */
        buttonTwo.click();
        buttonPlus.click();
        buttonTwo.click();
        buttonEquals.click();

        /* Check if within given time the correct result appears in the designated field. */
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, EXPECTED_RESULT_FOUR));

        saveVideo("two-plus-two.mp4");

    }



    /* A simple zero divided by zero operation. */
    @Test
    public void zerosDivisionOperation() {

        /* Get the elements. */
        MobileElement digitZero = (MobileElement)(driver.findElement(By.id("net.ludeke.calculator:id/digit0")));
        MobileElement buttonDivide = (MobileElement)(driver.findElement(By.id("net.ludeke.calculator:id/div")));
        MobileElement buttonEquals = (MobileElement)(driver.findElement(By.id("net.ludeke.calculator:id/equal")));
        MobileElement resultField = (MobileElement)(driver.findElement(By.xpath("//android.widget.EditText[1]")));

        /* Divide zero by zero. */
        digitZero.click();
        buttonDivide.click();
        digitZero.click();
        buttonEquals.click();

        /* Check if within given time the correct error message appears in the designated field. */
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, EXPECTED_RESULT_NAN));

        saveVideo("divide-by-zero.mp4");

    }

    private void saveVideo(String filename) {
        Capabilities capabilities = driver.getCapabilities();
        String team = (String)capabilities.getCapability("testobject_user_id");
        String project = (String)capabilities.getCapability("testobject_project_id");
        long reportId = (long)capabilities.getCapability("testobject_report_id");

        String username = System.getenv("TESTOBJECT_USERNAME");
        String password = System.getenv("TESTOBJECT_PASSWORD");
        if (username != null && password != null) {
            TestObjectClient client = TestObjectClient.Factory.create();
            client.login(username, password);
            AppiumTestReport testReport = client.getTestReport(team, project, reportId);

            try (InputStream video = client.getVideo(team, project, testReport.getVideoId());
                    OutputStream file = new FileOutputStream(filename)) {

                int read;
                byte[] bytes = new byte[1024];

                while ((read = video.read(bytes)) != -1) {
                    file.write(bytes, 0, read);
                }
            } catch (IOException e) {
                System.out.println("Failed to save " + filename + ": " + e);
            }
            System.out.println("Saved test recording to " + filename);
        } else {
            System.out.println("No username/password set; not saving " + filename + " test recording.");
        }
    }

}
