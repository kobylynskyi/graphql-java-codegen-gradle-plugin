package com.kobylynskyi.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.kobylynskyi.graphql.CharacterRepository;
import com.kobylynskyi.graphql.codegen.CreateHumanInput;
import com.kobylynskyi.graphql.codegen.CreateHumanMutation;
import com.kobylynskyi.graphql.codegen.Human;
import com.kobylynskyi.graphql.mappers.CharacterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements CreateHumanMutation, GraphQLMutationResolver {

    @Autowired
    private CharacterRepository repository;
    @Autowired
    private CharacterMapper mapper;

    @Override
    public Human createHuman(CreateHumanInput input) {
        return mapper.map(repository.createHuman(input.getName(), input.getHomePlanet()));
    }
}
