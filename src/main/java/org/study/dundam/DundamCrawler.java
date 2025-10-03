package org.study.dundam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DundamCrawler {

    private final WebDriver webDriver;

    private final static String DUNAM_URL = "https://dundam.xyz/search?server=adven&name=%s";

    public List<CharacterSpec> crawlCharacterSpecs(String adventureGroupName) {
        String url = String.format(DUNAM_URL, adventureGroupName);
        log.info("크롤링 시작: {}", adventureGroupName);

        webDriver.get(url);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sr-result .scon")));
        
        List<WebElement> charactersContainer = webDriver.findElements(By.cssSelector(".sr-result .scon"));

        return charactersContainer.stream()
                .map(this::extractCharacterSpec)
                .collect(Collectors.toList());
    }

    private CharacterSpec extractCharacterSpec(WebElement character) {
        WebElement characterNameElement = character.findElement(By.cssSelector(".seh_name .name"));
        String characterNameWithServer = characterNameElement.getText();
        String serverText = characterNameElement.findElement(By.cssSelector(".introd.server")).getText();
        String characterName = characterNameWithServer.replace(serverText, "").trim();

        String fame = character.findElement(By.cssSelector(".seh_name .level .val")).getText();
        String jobName = character.findElement(By.cssSelector(".seh_job .sev")).getText();

        // 딜러면 stat a, 버퍼면 stat b만 있음
        List<WebElement> damageElements = character.findElements(By.cssSelector("ul.stat_a .val"));
        List<WebElement> buffElements = character.findElements(By.cssSelector("ul.stat_b .val"));
        String power = !damageElements.isEmpty() ? damageElements.getFirst().getText() :
                !buffElements.isEmpty() ? buffElements.getFirst().getText() : "0";

        JopType jopType = damageElements.isEmpty() ? JopType.BUFFER : JopType.DEALER;

        return new CharacterSpec(characterName, jobName, fame, jopType, power);
    }
}
