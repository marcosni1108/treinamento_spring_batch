package br.com.treinamento.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.treinamento.entity.Animal;
import br.com.treinamento.entity.AnimalFileLine;
import br.com.treinamento.processor.AnimalProcessor;
import br.com.treinamento.processor.CatProcessor;
import br.com.treinamento.processor.DogProcessor;

@Configuration
@EnableBatchProcessing
public class AnimalsJob {

	@Autowired
	private JobBuilderFactory jobBuilder;

	@Autowired
	private StepBuilderFactory stepBuilder;

	@Value("file:\\C:\\Users\\cielo\\Documents\\project\\animals.csv")
	private Resource inputFile;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Bean
	public Job myAnimalsJob() {
		return jobBuilder.get("animalsJob")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}

	public Step step1() {
		return stepBuilder.get("animalStep")
				.<AnimalFileLine, Animal>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	public Step step2() {
		return stepBuilder.get("animalStep2")
				.<AnimalFileLine, Animal>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}

	@Bean
	public ItemReader<? extends AnimalFileLine> reader() {
		
		FlatFileItemReader<AnimalFileLine> reader = new FlatFileItemReader<>();
		reader.setResource(inputFile);
		reader.setLinesToSkip(1);
		reader.setLineMapper(creatLineMapper());
		return reader;
	}
	
	@Bean
	public ItemProcessor<? super AnimalFileLine, ? extends Animal> processor() {
		return new AnimalProcessor();
	}
	
	@Bean
	public ItemWriter<? super Animal> writer() {
		JdbcBatchItemWriter<Animal> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Animal>());
		writer.setSql("INSERT INTO TB_CACHORRO(RACA, PRECO) VALUES (:breed, :price)");
		writer.setDataSource(jdbcTemplate.getDataSource());
		return writer;
	}
	
	private LineMapper<AnimalFileLine> creatLineMapper() {
		
		DelimitedLineTokenizer tokenizer =new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"breed", "minPrice", "maxPrice", "type"});
		
		BeanWrapperFieldSetMapper<AnimalFileLine> beanMapper = new BeanWrapperFieldSetMapper<>();
		beanMapper.setTargetType(AnimalFileLine.class);
		
		DefaultLineMapper<AnimalFileLine> mapper = new DefaultLineMapper<>();
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(beanMapper);
		
		return mapper;
	}
	
	public static class Step1Configuration{
		
		@Bean
		public ItemProcessor<? super AnimalFileLine, ? extends Animal> processor() {
			return new DogProcessor();
		}
		
	}
	
public static class Step2Configuration{
		
		@Bean
		public ItemProcessor<? super AnimalFileLine, ? extends Animal> processor() {
			return new CatProcessor();
		}
		
	}

}
