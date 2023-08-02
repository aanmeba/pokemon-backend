package io.nology.pokemonbackend.pokemon;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PokemonService {
	
	@Autowired
	private PokemonRepository pokemonRepository;

	
	@Autowired
	private ModelMapper modelMapper;
	
	// type validation
	// hp & evolutionId validation
	private final List<String> validTypes = Arrays.asList("normal", 
			"fire", "water", "grass", "electric", "ice", "fighting", 
			"poison", "ground", "flying", "psychic", "bug", "rock", 
			"ghost", "dark", "dragon", "steel", "fairy");
	
	private boolean isValidType(String type) {
		return this.validTypes.contains(type.toLowerCase());
	}	
	
	public Pokemon create(CreatePokemonDTO data) {
		boolean validPokeType = this.isValidType(data.getType());
		
		if (!validPokeType) {
			throw new IllegalArgumentException("Please enter a valid type");
		}
		
		System.out.println("create method called");
		System.out.println(data.getName());
		String pokeName = data.getName().trim();
		String pokeType = data.getType().trim();
		String pokeImage = data.getImageLink().trim();
		Integer pokeHp = data.getHp();
		Integer pokeEvoId = data.getEvolutionId();
		
		Pokemon newPokemon = new Pokemon(
				pokeName, pokeType, pokeHp, pokeImage, pokeEvoId
				);
		
//		Pokemon newPokemon = modelMapper.map(data, Pokemon.class);
		Pokemon createdPokemon = this.pokemonRepository.save(newPokemon);
		return createdPokemon;
	}
	
	public List<Pokemon> findAll() {
		return this.pokemonRepository.findAll();
	}
	
	
	public Optional<Pokemon> findById(Long id) {
		Optional<Pokemon> maybePokemon = this.pokemonRepository.findById(id);
		return maybePokemon;
	}
	
	public Optional<List<Pokemon>> findByType(String type) {
		Optional<List<Pokemon>> maybePokemons = this.pokemonRepository.findByType(type);
		
		return maybePokemons;
	}
	
	public Optional<List<Pokemon>> findByHp(Integer hp) {
		Optional<List<Pokemon>> maybePokemons = this.pokemonRepository.findByHp(hp);
		
		return maybePokemons;
	}
	
	
	public boolean deleteById(Long id) {
		Optional<Pokemon> maybePokemon = this.findById(id);
		
		if (maybePokemon.isEmpty()) {
			return false;
		}
		this.pokemonRepository.delete(maybePokemon.get());
		return true;
	}
	
	public Optional<Pokemon> updateById(Long id, UpdatePokemonDTO data) {
		
		boolean validPokeType = (data.getType() != null) && this.isValidType(data.getType());
		
		
		if (!validPokeType) {
			throw new IllegalArgumentException("Please enter a valid type");
		}
				
		Optional<Pokemon> maybePokemon = this.findById(id);
		
		if (maybePokemon.isPresent()) {
			Pokemon existingPokemon = maybePokemon.get();
			String pokeName = data.getName().trim();
			String pokeType = data.getType().trim();
			String pokeImage = data.getImageLink().trim();
			Integer pokeHp = data.getHp();
			
			existingPokemon.setName(pokeName);
			existingPokemon.setType(pokeType);
			existingPokemon.setImageLink(pokeImage);
			existingPokemon.setHp(pokeHp);
			
			if (data.getEvolutionId() != null) {				
				Integer pokeEvoId = data.getEvolutionId();
				existingPokemon.setEvolutionId(pokeEvoId);
			}
			
//			modelMapper.map(data, existingPokemon);
			Pokemon optionalPokemon = this.pokemonRepository.save(existingPokemon);
			return Optional.ofNullable(optionalPokemon);
		}
		return maybePokemon;
	}
}
