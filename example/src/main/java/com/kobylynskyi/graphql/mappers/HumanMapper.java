package com.kobylynskyi.graphql.mappers;

import com.kobylynskyi.graphql.codegen.Human;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HumanMapper {

    Human map(com.kobylynskyi.graphql.model.Human id);

}
