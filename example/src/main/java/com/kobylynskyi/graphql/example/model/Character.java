package com.kobylynskyi.graphql.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Character {

    private String id;
    private String name;
    private List<Character> friends = new ArrayList<>();
    private List<Episode> appearsIn = new ArrayList<>();

    public void addFriends(Character... friends) {
        this.friends.addAll(Arrays.asList(friends));
    }
}
