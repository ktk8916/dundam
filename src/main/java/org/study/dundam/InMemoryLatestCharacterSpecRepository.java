package org.study.dundam;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryLatestCharacterSpecRepository implements LatestCharacterSpecRepository {
    
    private final Map<String, List<CharacterSpec>> storage = new ConcurrentHashMap<>();
    
    @Override
    public void save(String adventureGroupName, List<CharacterSpec> characterSpecs) {
        storage.put(adventureGroupName, characterSpecs);
    }
    
    @Override
    public List<CharacterSpec> findByAdventureGroupName(String adventureGroupName) {
        return storage.getOrDefault(adventureGroupName, List.of());
    }
}