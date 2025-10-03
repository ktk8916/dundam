package org.study.dundam;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.microsoft.playwright.BrowserType.*;

@Slf4j
@Configuration
public class PlaywrightConfig {

    @Bean
    public Playwright playwright() {
        return Playwright.create();
    }

    @Bean
    public Browser browser(Playwright playwright) {
        return playwright.chromium().launch(new LaunchOptions()
                .setHeadless(true)
                .setArgs(java.util.List.of(
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--disable-gpu",
                        "--remote-debugging-port=9222"
                )));
    }

    @Bean
    public BrowserContext browserContext(Browser browser) {
        return browser.newContext();
    }

    @Bean
    public Page page(BrowserContext context) {
        return context.newPage();
    }
}