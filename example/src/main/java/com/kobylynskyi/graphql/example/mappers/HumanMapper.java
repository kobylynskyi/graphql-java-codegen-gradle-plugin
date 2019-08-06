package com.kobylynskyi.graphql.example.mappers;

import com.yepco.graphql.Human;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HumanMapper {

    Human map(com.kobylynskyi.graphql.example.model.Human id);

}
