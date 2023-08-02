package io.nology.pokemonbackend.pokemon;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.transaction.Transactional;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTest {
	
	@Mock
	private PokemonRepository pokemonRepository;
	
	@InjectMocks
	private PokemonService underTest;
	
	@Test
	void findAllShouldCallFindAll() {
		underTest.findAll();
		
		verify(pokemonRepository).findAll();
	}
	
		
	@Test
	@Transactional
	void itShouldNotCreateNewPokemon() {
		CreatePokemonDTO pokemon = new CreatePokemonDTO("Pikachu", "something", 150, "http://pikachu2.jpg", 1);
		
		assertThatThrownBy(() -> underTest.create(pokemon))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Please enter a valid type");
		
		assertThat(pokemonRepository.count()).isEqualTo(0);
	}
	
	@Test
	void itShouldCreateNewPokemon() {
		CreatePokemonDTO pokemon = new CreatePokemonDTO("Pikachu", "Electric", 100, "http://pikachu.jpg", 1);
		
		underTest.create(pokemon);
		
		// .save in the repository layer
		ArgumentCaptor<Pokemon> pokemonArgument =
				ArgumentCaptor.forClass(Pokemon.class);
		verify(pokemonRepository).save(pokemonArgument.capture());
		
		assertThat(pokemonArgument.getValue().getName()).isEqualTo(pokemon.getName());		
	}
	
	@Test
	void itShouldNotCreateNewPokemonWithExistingName() {
		CreatePokemonDTO pokemon1 = new CreatePokemonDTO("Pikachu", "Electric", 100, "http://pikachu.jpg", 1);
		CreatePokemonDTO pokemon2 = new CreatePokemonDTO("Pikachu", "Electric", 150, "http://pikachu2.jpg", 1);
		
		underTest.create(pokemon1);
		
		ArgumentCaptor<Pokemon> pokemonArgument =
				ArgumentCaptor.forClass(Pokemon.class);
		verify(pokemonRepository).save(pokemonArgument.capture());

		// confirm pokemon1 is created
		assertThat(pokemonArgument.getValue().getName()).isEqualTo(pokemon1.getName());
						
		when(pokemonRepository.save(any(Pokemon.class)))
        .thenThrow(new DataIntegrityViolationException("name should be unique"));

		assertThatThrownBy(() -> underTest.create(pokemon2))
			.isInstanceOf(DataIntegrityViolationException.class)
			.hasMessage("name should be unique");	
	}
	
	@Test
	void itShouldReturnAPokemonIfIdExists() {
		Pokemon pokemon = new Pokemon("Pikachu", "Electric", 100, "http://pikachu.jpg", 3);
		
		Long id = 1l;
		
		BDDMockito
			.given(pokemonRepository.findById(ArgumentMatchers.anyLong()))
			.willReturn(Optional.of(pokemon));
		
		Optional<Pokemon> received = underTest.findById(id);
		assertThat(received.get()).isEqualTo(pokemon);
	}
	
	@Test
	void itShouldNotReturnAPokemonIfIdDoesntExist() {
		Long id = 123l;
		
		Optional<Pokemon> received = underTest.findById(id);
		assertThat(received.isEmpty()).isTrue();
	}
	
	// findByType & findByHp are tested in the RepositoryTest
	
	@Test
	void itShouldReturnAnEmptyValueIfNoPokemonExistsWithTheType() {
		String type = "something";
		
		Optional<List<Pokemon>> received = underTest.findByType(type);
		assertThat(received.isPresent()).isFalse();
	}
	
	@Test
	void itShouldNotDeletePokemonIfIdDoesntExist() {
		Long id = 123l;
		
		BDDMockito.given(pokemonRepository.findById(id))
			.willReturn(Optional.empty());
		
		boolean received = underTest.deleteById(id);
		assertThat(received).isFalse();
	}
	
	@Test
	void itShouldDeletePokemonIfIdExists() {
		Pokemon pokemon = new Pokemon("Pikachu", "Electric", 100, "http://pikachu.jpg", 3);
		Long id = 123l;
		
		BDDMockito
			.given(pokemonRepository.findById(ArgumentMatchers.anyLong()))
			.willReturn(Optional.of(pokemon));
		
		boolean received = underTest.deleteById(id);
		
		ArgumentCaptor<Pokemon> pokemonArgument =
				ArgumentCaptor.forClass(Pokemon.class);
		
		verify(pokemonRepository).delete(pokemonArgument.capture());
		
		assertThat(received).isTrue();		
	}
	
	@Test
	void itShouldUpdatePokemonWithValidData() {
		Pokemon existingPokemon = new Pokemon("Pikachu", "Electric", 100, "http://pikachu.jpg", 3);
		UpdatePokemonDTO updatePokemonDTO = new UpdatePokemonDTO();
		
        updatePokemonDTO.setName("Raichu");
        updatePokemonDTO.setType("water");
        updatePokemonDTO.setHp(200);
        updatePokemonDTO.setImageLink("http://raichu.jpg");
        
        Long id = 123l;
        
        BDDMockito
			.given(pokemonRepository.findById(ArgumentMatchers.anyLong()))
			.willReturn(Optional.of(existingPokemon));
        
        Optional<Pokemon> received = underTest.updateById(id, updatePokemonDTO);
		
		ArgumentCaptor<Pokemon> pokemonArgument =
				ArgumentCaptor.forClass(Pokemon.class);
		
		verify(pokemonRepository).save(pokemonArgument.capture());
             
        
        assertThat(received.get().getName()).isEqualTo(updatePokemonDTO.getName());
        
        
//        assertThat(received.get().getName()).isEqualTo(updatePokemonDTO.getName());
//        assertThat(received.get().getHp()).isEqualTo(updatePokemonDTO.getHp());
        
	}
	
	@Test
	void itShouldNotUpdateAndReturnEmptyValueIfIdDoesntNotExist() {
		UpdatePokemonDTO updatePokemonDTO = new UpdatePokemonDTO();
		updatePokemonDTO.setName("Raichu");
		updatePokemonDTO.setType("water");
		updatePokemonDTO.setHp(200);
		updatePokemonDTO.setImageLink("http://raichu.jpg");
		
		Long id = 123l;
		
		BDDMockito.given(pokemonRepository.findById(id))
		.willReturn(Optional.empty());
	
		Optional<Pokemon> received = underTest.updateById(id, updatePokemonDTO);
		assertThat(received).isEmpty();
		
	}
	
	@Test
	@Transactional
	void itShouldNotUpdatePokemonWithInvalidType() {
		UpdatePokemonDTO updatePokemonDTO = new UpdatePokemonDTO();
		
        updatePokemonDTO.setType("something");
        
		Long id = 123l;
				
		assertThatThrownBy(() -> underTest.updateById(id, updatePokemonDTO))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Please enter a valid type");		
	}
	
	
	

}
