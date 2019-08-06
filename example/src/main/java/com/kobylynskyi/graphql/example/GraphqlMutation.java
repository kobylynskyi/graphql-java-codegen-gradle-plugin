package com.kobylynskyi.graphql.example;

import com.kobylynskyi.graphql.example.mappers.HumanMapper;
import com.yepco.graphql.CreateHumanInput;
import com.yepco.graphql.CreateHumanMutation;
import com.yepco.graphql.Human;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphqlMutation implements CreateHumanMutation {

    @Autowired
    private CharacterRepository repository;
    @Autowired
    private HumanMapper mapper;

    @Override
    public Human createHuman(CreateHumanInput input) {
        return mapper.map(repository.createHuman(input.getName(), input.getHomePlanet()));
    }
}
