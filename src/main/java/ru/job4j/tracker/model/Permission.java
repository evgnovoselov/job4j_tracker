package ru.job4j.tracker.model;

import lombok.*;

import java.util.List;

@Builder(builderMethodName = "of")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Permission {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    @Singular
    private List<String> rules;
}
