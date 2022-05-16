package step_definitions;

import command_providers.ActOn;
import command_providers.AssertThat;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import utilities.ReadConfigFiles;

import java.util.List;
import java.util.Map;

public class LoginSteps {
    private static final By FullName = By.id("name");
    private static final By Password = By.id("password");
    private static final By Login = By.id("login");
    private static final By Logout = By.id("logout");
    private static final By InvalidPassword = By.xpath("//*[@id='pageLogin']/form//div[text()='Password is invalid']");
    WebDriver driver = Hooks.driver;

    private static final Logger LOGGER = LogManager.getLogger(LoginSteps.class);

    //Pre Condition
    @Given("^a user is on the login page$")
    public void navigateToLoginPage() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        ActOn.browser(driver).openBrowser(ReadConfigFiles.getPropertyValues("TestAppURL"));
        LOGGER.info("User is on the Login Page");
    }

    //Main Action
    @When("^user enters username \"(.+?)\" and password \"(.+?)\"$")
    public void enterUserCredentials(String username, String password) {
        ActOn.element(driver, FullName).setValue(username);
        ActOn.element(driver, Password).setValue(password);
        LOGGER.info("User has entered successfully");
    }

    @When("^user click on login button entering credentials$")
    public void userClickOnLogin(DataTable table) {
        List<Map<String, String>> data = table.asMaps(String.class, String.class);
        for (Map<String, String> cells: data) {
            ActOn.element(driver, FullName).setValue(cells.get("username"));
            ActOn.element(driver, Password).setValue(cells.get("password"));
            LOGGER.info("User has entered credentials");

            ActOn.element(driver, Login).click();
            LOGGER.info("User clicked on login button");
        }
    }

    //Additional Action
    @And("^click on login button$")
    public void clickOnLogin() {
        ActOn.element(driver, Login).click();
        LOGGER.info("User clicked on Login button");
    }

    //Assertion
    @Then("^user is navigated to home page$")
    public void validateUserIsLoggedInSuccessfully() {
        AssertThat.elementAssertions(driver, Logout).elementIsDisplayed();
        LOGGER.info("User is in Home Page");
        ActOn.browser(driver).closeBrowser();
    }

    @Then("^user is failed to login$")
    public void validateUserIsFailedToLogin() {
        AssertThat.elementAssertions(driver, InvalidPassword).elementIsDisplayed();
        LOGGER.info("User is still on the login page");
        ActOn.browser(driver).closeBrowser();
    }
}
