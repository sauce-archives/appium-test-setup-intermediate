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

        DesiredCapabilities capabilities = new DesiredCapabilities();

        /* These are the capabilities we must provide to run our test on TestObject. */
        capabilities.setCapability("testobject_api_key", System.getenv("testobject-api-key")); // API key through env variable
        //capabilities.setCapability("testobject_api_key", "YOUR_API_KEY")); // API key hardcoded

        capabilities.setCapability("testobject_app_id", "1");

        capabilities.setCapability("testobject_device", System.getenv("testobject-device-id")); // device id through env variable
        //capabilities.setCapability("testobject_device", "Motorola_Moto_G_2nd_gen_real"); // device id hardcoded

        driver = new AndroidDriver(new URL("https://app.testobject.com:443/api/appium/wd/hub"), capabilities);

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

    }

    /* An invalid operation, it navigates to the advanced panel, selects factorial, then minus,
     * then the equal button. The expected result is an error message in the result field. */
    @Test
    public void factorialMinusOperation() {

        /* In the main panel... */
        MobileElement menuButton = (MobileElement)(driver.findElement(By.id("net.ludeke.calculator:id/overflow_menu")));
        menuButton.click();

        MobileElement advancedPanelButton = (MobileElement)(new WebDriverWait(driver, 60))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("Advanced panel")));
        advancedPanelButton.click();

        /* In the advanced panel... */
        MobileElement factorialButton = (MobileElement)(new WebDriverWait(driver, 60))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("net.ludeke.calculator:id/factorial")));
        factorialButton.click();

        /* In the main panel again. */
        MobileElement minusButton = (MobileElement)(new WebDriverWait(driver, 60))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("net.ludeke.calculator:id/minus")));
        minusButton.click();

        MobileElement equalsButton = (MobileElement)(driver.findElement(By.id("net.ludeke.calculator:id/equal")));
        equalsButton.click();

        MobileElement resultField = (MobileElement)(driver.findElement(By.xpath("//android.widget.EditText[1]")));

        (new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, EXPECTED_RESULT_ERROR));

    }


}
