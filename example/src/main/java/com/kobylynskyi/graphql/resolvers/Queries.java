package com.kobylynskyi.graphql.resolvers;

import com.kobylynskyi.graphql.CharacterRepository;
import com.kobylynskyi.graphql.codegen.Character;
import com.kobylynskyi.graphql.codegen.*;
import com.kobylynskyi.graphql.mappers.CharacterMapper;
import com.kobylynskyi.graphql.mappers.EpisodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Queries implements HeroQuery, HumanQuery, DroidQuery, CharacterQuery {

    @Autowired
    private CharacterRepository repository;
    @Autowired
    private EpisodeMapper episodeMapper;
    @Autowired
    private CharacterMapper characterMapper;

    @Override
    public Character hero(Episode episode) {
        return characterMapper.map(repository.getHero(episodeMapper.map(episode)));
    }

    @Override
    public Droid droid(String id) {
        return characterMapper.map(repository.getDroid(id));
    }

    @Override
    public Human human(String id) {
        return characterMapper.map(repository.getHuman(id));
    }

    @Override
    public Character character(String id) {
        return characterMapper.map(repository.getCharacter(id));
    }
}
