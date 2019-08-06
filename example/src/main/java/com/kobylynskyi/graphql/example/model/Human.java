package com.kobylynskyi.graphql.example.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Human extends Character {

    private String homePlanet;

    public Human(String id, String name, List<Episode> appearsIn, String homePlanet) {
        super(id, name, new ArrayList<>(), appearsIn);
        this.homePlanet = homePlanet;
    }
}
