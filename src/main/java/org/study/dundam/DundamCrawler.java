package org.study.dundam;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DundamCrawler {

    private final Page page;

    private final static String DUNAM_URL = "https://dundam.xyz/search?server=adven&name=%s";

    public List<CharacterSpec> crawlCharacterSpecs(String adventureGroupName) {
        String url = String.format(DUNAM_URL, adventureGroupName);
        log.info("크롤링 시작: {}", adventureGroupName);

        page.navigate(url);
        page.waitForSelector(".sr-result .scon");
        List<ElementHandle> charactersContainer = page.querySelectorAll(".sr-result .scon");

        return charactersContainer.stream()
                .map(this::exctractCharacterSpec)
                .collect(Collectors.toList());
    }

    private CharacterSpec exctractCharacterSpec(ElementHandle character) {
        ElementHandle characterNameElement = character.querySelector(".seh_name .name");
        String characterNameWithServer = characterNameElement.textContent();
        String serverText = characterNameElement.querySelector(".introd.server").textContent();
        String characterName = characterNameWithServer.replace(serverText, "").trim();

        String fame = character.querySelector(".seh_name .level .val").textContent();
        String jobName = character.querySelector(".seh_job .sev").textContent();

        // 딜러면 stat a, 버퍼면 stat b만 있음
        List<ElementHandle> damageElements = character.querySelectorAll("ul.stat_a .val");
        List<ElementHandle> buffElements = character.querySelectorAll("ul.stat_b .val");
        String power = !damageElements.isEmpty() ? damageElements.getFirst().textContent() :
                !buffElements.isEmpty() ? buffElements.getFirst().textContent() : "0";

        JopType jopType = damageElements.isEmpty() ? JopType.BUFFER : JopType.DEALER;

        return new CharacterSpec(characterName, jobName, fame, jopType, power);
    }
}
