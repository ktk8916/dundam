package org.study.dundam;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Component
public class SeleniumManager {

    @Value("${SELENIUM_URL:http://localhost:4444/wd/hub}")
    private String seleniumUrl;

    public WebDriver getDriver() throws MalformedURLException {
        ChromeOptions options = buildChromeOption();
        RemoteWebDriver driver = new RemoteWebDriver(new URL(seleniumUrl), options);

        driver.executeScript(
                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );

        return driver;
    }

    private ChromeOptions buildChromeOption() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        // WebDriver 탐지 방지
        options.setExperimentalOption("excludeSwitches",
                new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        return options;
    }
}