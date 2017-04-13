package br.com.treinamento.processor;

import org.springframework.batch.item.ItemProcessor;

import br.com.treinamento.entity.Animal;
import br.com.treinamento.entity.AnimalFileLine;

public class CatProcessor implements ItemProcessor<AnimalFileLine, Animal> {

	@Override
	public Animal process(AnimalFileLine arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
