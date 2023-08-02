package io.nology.pokemonbackend.pokemon;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	
	@NotNull
	@Getter
	@Setter
	private Integer hp;
	
	@NotBlank
	@Getter
	@Setter
	private String imageLink;
	
	@Getter
	@Setter
	private Integer evolutionId;
	
	public CreatePokemonDTO(String name, String type, Integer hp, String imageLink, Integer evolutionId) {
		this.name = name;
		this.type = type;
		this.hp = hp;
		this.imageLink = imageLink;
		this.evolutionId = evolutionId;
	}
}
