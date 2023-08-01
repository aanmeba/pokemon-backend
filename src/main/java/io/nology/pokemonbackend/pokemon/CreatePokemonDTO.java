package io.nology.pokemonbackend.pokemon;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class CreatePokemonDTO {

	@NotBlank
	@Getter
	@Setter
	private String name;
	
	@NotBlank
	@Getter
	@Setter
	private String type;
	
	@NotBlank
	@Getter
	@Setter
	private String hp;
	
	@NotBlank
	@Getter
	@Setter
	private String imageLink;
	
	@Getter
	@Setter
	private String evolutionId;
	
	public CreatePokemonDTO(String name, String type, String hp, String imageLink, String evolutionId) {
		this.name = name;
		this.type = type;
		this.hp = hp;
		this.imageLink = imageLink;
		this.evolutionId = evolutionId;
	}
}
