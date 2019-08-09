package com.kobylynskyi.graphql;

import com.kobylynskyi.graphql.model.Character;
import com.kobylynskyi.graphql.model.Droid;
import com.kobylynskyi.graphql.model.Episode;
import com.kobylynskyi.graphql.model.Human;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Component
public class CharacterRepository {

    private Map<String, Character> characters;
    private Map<Episode, Character> heroes;

    public CharacterRepository() {
        Human lukeSkywalker = new Human(UUID.randomUUID().toString(), "Luke Skywalker", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Tatooine");
        Human darthVader = new Human(UUID.randomUUID().toString(), "Darth Vader", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Tatooine");
        Human hanSolo = new Human(UUID.randomUUID().toString(), "Han Solo", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), null);
        Human leiaOrgana = new Human(UUID.randomUUID().toString(), "Leia Organa", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Alderaan");
        Human wilhuffTarkin = new Human(UUID.randomUUID().toString(), "Wilhuff Tarkin", Collections.singletonList(Episode.NEWHOPE), null);

        Droid c3po = new Droid(UUID.randomUUID().toString(), "C-3PO", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Protocol");
        Droid aretoo = new Droid(UUID.randomUUID().toString(), "R2-D2", Arrays.asList(Episode.NEWHOPE, Episode.JEDI, Episode.EMPIRE), "Astromech");

        lukeSkywalker.addFriends(hanSolo, leiaOrgana, c3po, aretoo);
        darthVader.addFriends(wilhuffTarkin);
        hanSolo.addFriends(lukeSkywalker, leiaOrgana, aretoo);
        leiaOrgana.addFriends(lukeSkywalker, hanSolo, c3po, aretoo);
        wilhuffTarkin.addFriends(darthVader);

        c3po.addFriends(lukeSkywalker, hanSolo, leiaOrgana, aretoo);
        aretoo.addFriends(lukeSkywalker, hanSolo, leiaOrgana);

        characters = new HashMap<>(Stream.of(lukeSkywalker, darthVader, hanSolo, leiaOrgana, wilhuffTarkin, c3po, aretoo)
                .collect(Collectors.toMap(Character::getId, Function.identity())));

        heroes = new HashMap<>();
        heroes.put(Episode.NEWHOPE, lukeSkywalker);
        heroes.put(Episode.EMPIRE, aretoo);
        heroes.put(Episode.JEDI, darthVader);
    }

    public Human createHuman(String name, String homePlanet) {
        Human human = new Human(UUID.randomUUID().toString(), name, Collections.emptyList(), homePlanet);
        characters.put(human.getId(), human);
        return human;
    }

    public Character getCharacter(String id) {
        return characters.get(id);
    }

    public Droid getDroid(String id) {
        Character character = getCharacter(id);
        if (character instanceof Droid) {
            return (Droid) character;
        }
        return null;
    }

    public Human getHuman(String id) {
        Character character = getCharacter(id);
        if (character instanceof Human) {
            return (Human) character;
        }
        return null;
    }

    public Character getHero(@Nullable Episode episode) {
        if (episode == null) {
            return heroes.get(Episode.NEWHOPE);
        } else {
            return heroes.get(episode);
        }
    }
}
