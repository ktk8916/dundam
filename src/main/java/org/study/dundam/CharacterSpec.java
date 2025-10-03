package org.study.dundam;

public record CharacterSpec(
    String characterName,
    String jobName,
    String fame,
    JopType jopType,
    String power
) {
}
