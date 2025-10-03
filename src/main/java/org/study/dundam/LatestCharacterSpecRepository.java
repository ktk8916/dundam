package org.study.dundam;

import java.util.List;

public interface LatestCharacterSpecRepository {

    void save(String adventureGroupName, List<CharacterSpec> characterSpecs);

    List<CharacterSpec> findByAdventureGroupName(String adventureGroupName);
}