package com.kobylynskyi.graphql.mappers;

import com.kobylynskyi.graphql.codegen.Character;
import com.kobylynskyi.graphql.codegen.Droid;
import com.kobylynskyi.graphql.codegen.Human;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CharacterMapper {

    Human map(com.kobylynskyi.graphql.model.Human from);

    Droid map(com.kobylynskyi.graphql.model.Droid from);

    default Character map(com.kobylynskyi.graphql.model.Character from) {
        if (from == null) {
            return null;
        }
        if (from instanceof com.kobylynskyi.graphql.model.Human) {
            return map((com.kobylynskyi.graphql.model.Human) from);
        } else if (from instanceof com.kobylynskyi.graphql.model.Droid) {
            return map((com.kobylynskyi.graphql.model.Droid) from);
        }
        throw new IllegalArgumentException("Unknown character type: " + from.getClass());
    }

}
