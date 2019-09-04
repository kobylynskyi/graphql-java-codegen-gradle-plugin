package io.github.kobylynskyi.bikeshop.graphql.mappers;

import io.github.kobylynskyi.bikeshop.model.Bike;
import io.github.kobylynskyi.bikeshop.model.BikeType;
import com.kobylynskyi.graphql.bikeshop.BikeInputTO;
import com.kobylynskyi.graphql.bikeshop.BikeTO;
import com.kobylynskyi.graphql.bikeshop.BikeTypeTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BikeMapper {

    BikeTO map(Bike from);

    @Mapping(target = "id", ignore = true) // auto-generated
    @Mapping(target = "addedDateTime", ignore = true)
        // set in the service
    Bike mapInput(BikeInputTO from);

    BikeType mapInputType(BikeTypeTO type);

}
