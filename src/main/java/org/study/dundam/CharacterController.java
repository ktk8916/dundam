package org.study.dundam;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/v1/characters")
    public List<CharacterSpec> getCharacterSpecs(@RequestParam String adventureGroupName) {
        return characterService.getCharacterSpecs(adventureGroupName);
    }
}
