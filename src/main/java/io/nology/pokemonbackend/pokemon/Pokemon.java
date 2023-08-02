package io.nology.pokemonbackend.pokemon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pokemons")
public class Pokemon {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	@Getter // without getter & setter, nothing returns
	@Setter
	private String name;
	
	@Column
	@Getter
	@Setter
	private String type;
	
	@Column
	@Getter
	@Setter
	private Integer hp;
	
	@Column
	@Getter
	@Setter
	private String imageLink;
	
	@Column(nullable = true)
	@Getter
	@Setter
	private Integer evolutionId;
	
	public Pokemon() {};
	public Pokemon(String name, String type, Integer hp, String imageLink, Integer evolutionId) {
		super(); // what it does here?
		this.name = name;
		this.type = type;
		this.hp = hp;
		this.imageLink = imageLink;
		this.evolutionId = evolutionId;
	}
	

}
