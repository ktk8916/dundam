package org.study.dundam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DundamCrawler {

    private final static String DUNAM_URL = "https://dundam.xyz/search?server=adven&name=%s";

    private final SeleniumManager seleniumManager;

    public List<CharacterSpec> crawlCharacterSpecs(String adventureGroupName) {
        try {
            WebDriver webDriver = seleniumManager.getDriver();

            String url = String.format(DUNAM_URL, adventureGroupName);
            log.info("크롤링 시작: {}", adventureGroupName);

            webDriver.get(url);
            log.info("페이지 요청 완료");

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".sr-result .scon")));

            log.info("캐릭터 로딩 완료");
            List<WebElement> charactersContainer = webDriver.findElements(By.cssSelector(".sr-result .scon"));

            webDriver.close();
            return charactersContainer.stream()
                    .map(this::extractCharacterSpec)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

        }
    }

    private CharacterSpec extractCharacterSpec(WebElement character) {
        WebElement characterNameElement = character.findElement(By.cssSelector(".seh_name .name"));
        String characterNameWithServer = characterNameElement.getText();
        String serverText = characterNameElement.findElement(By.cssSelector(".introd.server")).getText();

        String characterName;
        int lastIndex = characterNameWithServer.lastIndexOf(serverText);
        if (lastIndex >= 0) {
            characterName = characterNameWithServer.substring(0, lastIndex).trim();
        } else {
            characterName = characterNameWithServer;
        }

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
