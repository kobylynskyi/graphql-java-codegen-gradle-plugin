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
public class Droid extends Character {

    private String primaryFunction;

    public Droid(String id, String name, List<Episode> appearsIn, String primaryFunction) {
        super(id, name, new ArrayList<>(), appearsIn);
        this.primaryFunction = primaryFunction;
    }

}
