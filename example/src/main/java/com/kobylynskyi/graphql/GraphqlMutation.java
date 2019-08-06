package com.kobylynskyi.graphql;

import com.kobylynskyi.graphql.codegen.CreateHumanInput;
import com.kobylynskyi.graphql.codegen.CreateHumanMutation;
import com.kobylynskyi.graphql.codegen.Human;
import com.kobylynskyi.graphql.mappers.HumanMapper;

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
