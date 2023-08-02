package io.nology.pokemonbackend.pokemon;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PokemonRepository extends JpaRepository<Pokemon, Long>{

	@Query(nativeQuery = true, value = "SELECT * FROM pokemons WHERE type = :type")
	Optional<List<Pokemon>> findByType(@Param("type") String type);
	
	@Query(nativeQuery = true, value = "SELECT * FROM pokemons WHERE hp >= :hp")
	Optional<List<Pokemon>> findByHp(@Param("hp") Integer hp);
	
//	@Query(nativeQuery = true, value = "SELECT * FROM pokemons WHERE hp >= :minHp")
//	Optional<List<Pokemon>> findByHp(@Param("minHp") String minHp);
	
	
}
