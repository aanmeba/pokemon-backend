package io.nology.pokemonbackend.pokemon;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

public class UpdatePokemonDTO {
	
	@Pattern(regexp = "^(?=\\S).*$", message="Name cannot be an empty string")
	@Getter
	@Setter
	String name;
	
	@Pattern(regexp = "^(?=\\S).*$", message="Type cannot be an empty string")
	@Getter
	@Setter
	String type;
	
//	@Pattern(regexp = "^[1-9]+[0-9]*$", message="HP cannot be an empty string")
	@Getter
	@Setter
	Integer hp;
	
	@Pattern(regexp = "^(?=\\S).*$", message="Image link cannot be an empty string")
	@Getter
	@Setter
	String imageLink;
	
//	@Pattern(regexp = "^[1-9]+[0-9]*$", message="Evolution ID cannot be an empty string")
	@Getter
	@Setter
	Integer evolutionId;

}
