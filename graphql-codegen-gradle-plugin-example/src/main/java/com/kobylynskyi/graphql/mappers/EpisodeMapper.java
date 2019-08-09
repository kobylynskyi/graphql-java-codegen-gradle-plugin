package com.kobylynskyi.graphql.mappers;

import com.kobylynskyi.graphql.model.Episode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EpisodeMapper {

    Episode map(com.kobylynskyi.graphql.codegen.Episode from);

}
