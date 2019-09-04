package io.github.kobylynskyi.bikeshop.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import io.github.kobylynskyi.bikeshop.graphql.mappers.BikeMapper;
import io.github.kobylynskyi.bikeshop.model.Bike;
import io.github.kobylynskyi.bikeshop.service.BikeService;
import com.kobylynskyi.graphql.bikeshop.BikeInputTO;
import com.kobylynskyi.graphql.bikeshop.BikeTO;
import com.kobylynskyi.graphql.bikeshop.Mutation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MutationsResolver implements Mutation, GraphQLMutationResolver {

    @Autowired
    private BikeService service;
    @Autowired
    private BikeMapper mapper;

    @Override
    public BikeTO newBike(BikeInputTO bikeInputTO) {
        Bike savedBike = service.create(mapper.mapInput(bikeInputTO));
        return mapper.map(savedBike);
    }
}
