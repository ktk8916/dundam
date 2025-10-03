package org.study.dundam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterService {

    private final DundamCrawler crawler;
    private final LatestCharacterSpecRepository repository;

    @Cacheable(value = "characterSpecs", key = "#adventureGroupName")
    public List<CharacterSpec> getCharacterSpecs(String adventureGroupName) {
        try {
            List<CharacterSpec> result = crawler.crawlCharacterSpecs(adventureGroupName);
            repository.save(adventureGroupName, result);
            return result;
        } catch (Exception e) {
            log.warn("모험단 '{}' 크롤링 실패, 가장 최근 저장된 데이터로 fallback: {}", adventureGroupName, e.getMessage());
            return repository.findByAdventureGroupName(adventureGroupName);
        }
    }
}