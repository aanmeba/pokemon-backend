package io.nology.pokemonbackend.exception;

import lombok.Getter;

public class NotFoundException extends RuntimeException {
	
	@Getter
	private String message;
	
	public NotFoundException(String message) {
		super();
		this.message = message;
	}
	
	

}
