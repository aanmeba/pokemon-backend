package io.nology.pokemonbackend.pokemon;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nology.pokemonbackend.exception.NotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {
	
	@Autowired
	private PokemonService pokemonService;
	
	@PostMapping
	public ResponseEntity<Pokemon> createPokemon(@Valid @RequestBody CreatePokemonDTO data) {
		Pokemon createdPokemon = this.pokemonService.create(data);
		
		return new ResponseEntity<>(createdPokemon, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Pokemon>> getAllPokemons() {
		List<Pokemon> allPokemons = this.pokemonService.findAll();
		
		return new ResponseEntity<>(allPokemons, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Pokemon> getById(@PathVariable Long id) {
		Optional<Pokemon> foundPokemon = this.pokemonService.findById(id);
		if (foundPokemon.isEmpty()) {
			throw new NotFoundException("not found the pokemon");
		}
		return new ResponseEntity<>(foundPokemon.get(), HttpStatus.OK);
	}
	
	
	@GetMapping(params = "type")
	public ResponseEntity<List<Pokemon>> getByType(@RequestParam String type) {
		Optional<List<Pokemon>> pokemonsByType = this.pokemonService.findByType(type);
		if (pokemonsByType.isEmpty()) {
			throw new NotFoundException(String.format("not found any pokemon in %s type", type));
		}
		return new ResponseEntity<>(pokemonsByType.get(), HttpStatus.OK);
	}
	
	@GetMapping(params = "hp")
	public ResponseEntity<List<Pokemon>> getByHp(@RequestParam String hp) {
		Optional<List<Pokemon>> pokemonsByHp = this.pokemonService.findByHp(hp);
		if (pokemonsByHp.isEmpty()) {
			throw new NotFoundException(String.format("not found any pokemon in %s HP", hp));
		}
		return new ResponseEntity<>(pokemonsByHp.get(), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public String updatedById(
			@PathVariable Long id,
			@Valid @RequestBody UpdatePokemonDTO data
			) {
		Optional<Pokemon> maybeUpdated = this.pokemonService.updateById(id, data);
		if (maybeUpdated.isEmpty()) {
			return "Oops! something went wrong to update";
		}
		
		return "updated!";
	}
	
	@DeleteMapping("/{id}")
	public String deleteById(@PathVariable Long id) {
		boolean deleted = this.pokemonService.deleteById(id);
		
		if (!deleted) {
			return "Oops!, not found";
		}
		return "deleted!";
	}

}
