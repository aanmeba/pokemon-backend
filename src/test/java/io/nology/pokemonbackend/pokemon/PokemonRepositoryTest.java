package io.nology.pokemonbackend.pokemon;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class PokemonRepositoryTest {
	
	@Autowired
	private PokemonRepository underTest;
	
	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}
	
	@Test
	void itShouldReturnAnOptionalListOfPokemonWithTheInputType() {
		Pokemon pokemon1 = new Pokemon("Pikachu", "Electric", 100, "http://pikachu.jpg", 1);
		underTest.save(pokemon1);
		String type = "Electric";
		
		Optional<List<Pokemon>> received = underTest.findByType(type);
		assertThat(received.isPresent()).isTrue();
		assertThat(received.get()).extracting("name").contains(pokemon1.getName());
		assertThat(received.get()).extracting("type").contains(pokemon1.getType());
		assertThat(received.get()).hasSize(1);
	}
	
	@Test
	void itShouldReturnAnEmptyValueWhenThereIsNoPokemonWithTheType() {
		Pokemon pokemon1 = new Pokemon("Pikachu", "Electric", 100, "http://pikachu.jpg", 1);
		underTest.save(pokemon1);
		String type = "water";
		
		Optional<List<Pokemon>> received = underTest.findByType(type);
		assertThat(received.get()).isEmpty();
	}
	
	@Test
	void itShouldReturnAnOptionalListOfPokemonEqualToAndGreaterThanTheInputHp() {
		Pokemon pokemon1 = new Pokemon("Pikachu", "Electric", 100, "http://pikachu.jpg", 1);
		Pokemon pokemon2 = new Pokemon("Eevee", "Normal", 80, "http://eevee.jpg", 2);
		underTest.save(pokemon1);
		underTest.save(pokemon2);
		Integer inputHp = 100;
		
		Optional<List<Pokemon>> received = underTest.findByHp(inputHp);
		assertThat(received.get()).hasSize(1);
		assertThat(received.get()).extracting("name").contains(pokemon1.getName());
		assertThat(received.get()).extracting("hp").allMatch(hp -> (Integer) hp >= inputHp);
	}
	
	@Test
	void itShouldReturnAnEmptyValueWhenThereIsNoPokemonWithTheRange() {
		Pokemon pokemon1 = new Pokemon("Pikachu", "Electric", 100, "http://pikachu.jpg", 1);
		underTest.save(pokemon1);
		Integer inputHp = 150;
		
		Optional<List<Pokemon>> received = underTest.findByHp(inputHp);
		assertThat(received.get()).hasSize(0);
		assertThat(received.get()).isEmpty();
	}
	
}