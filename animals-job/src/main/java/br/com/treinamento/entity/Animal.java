package br.com.treinamento.entity;

import java.math.BigDecimal;

public class Animal {

	private String breed;
	private BigDecimal price;

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
