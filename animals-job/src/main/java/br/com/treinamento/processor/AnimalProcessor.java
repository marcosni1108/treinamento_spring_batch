package br.com.treinamento.processor;

import java.math.BigDecimal;

import org.springframework.batch.item.ItemProcessor;

import br.com.treinamento.entity.Animal;
import br.com.treinamento.entity.AnimalFileLine;

public class AnimalProcessor implements ItemProcessor<AnimalFileLine, Animal> {

	@Override
	public Animal process(AnimalFileLine fileLine) throws Exception {
		
		Animal animal = new Animal();
		animal.setBreed(fileLine.getBreed());
		animal.setPrice(calculatePrice(fileLine));
		
		return animal;
	}

	private BigDecimal calculatePrice(AnimalFileLine fileLine) {
		// TODO Auto-generated method stub
		return null;
	}

}
