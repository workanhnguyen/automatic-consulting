package com.nva.server.it;

import com.vaadin.flow.component.login.testbench.LoginFormElement;
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.BrowserTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;


// @RunLocally(Browser.FIREFOX)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
public class LoginE2ETest extends BrowserTestBase { 

    @Autowired
    Environment env;

    static {
        // Prevent Vaadin Development mode to launch browser window
        System.setProperty("vaadin.launch-browser", "false");
    }

    @BeforeEach
    void openBrowser() {
        getDriver().get("http://localhost:" +
            env.getProperty("local.server.port") + "/");
    }

    @BrowserTest
    public void loginAsValidUserSucceeds() {
        // Find the LoginForm used on the page, using a
        // typed selector API provided by TestBench
        LoginFormElement form = $(LoginFormElement.class).first();
        // Enter the credentials and log in
        form.getUsernameField().setValue("admin@gmail.com");
        form.getPasswordField().setValue("admin");
        form.getSubmitButton().click();

        // Behind the scenes TestBench uses lower level WebDriver API
        // Here we can configure it on the fly
        getDriver().manage().timeouts().implicitlyWait(Duration.of(1, ChronoUnit.SECONDS));
        // Here finding an element on the actual main layout (after login),
        // using pure WebDriver API, BTW. There is also AppLayoutElement for TB
        getDriver().findElement(By.tagName("vaadin-app-layout"));

        // Ensure the login form is no longer visible
        assertFalse($(LoginFormElement.class).exists());
    }

}