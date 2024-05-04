package ru.job4j.tracker.model;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @Getter
    @EqualsAndHashCode.Include
    private int id;
    @Getter
    @Setter
    private String name;
}
