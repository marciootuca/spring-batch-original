package br.com.original.opbk;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication(scanBasePackages = {"br.com.original"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new ResourcelessTransactionManager();
	}

	@Bean
	public JobExplorer jobExplorer() throws Exception {
		MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(mapJobRepositoryFactoryBean());
		jobExplorerFactory.afterPropertiesSet();
		return jobExplorerFactory.getObject();
	}

	@Bean
	public MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean() {
		MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean();
		mapJobRepositoryFactoryBean.setTransactionManager(transactionManager());
		return mapJobRepositoryFactoryBean;
	}

	@Bean
	public JobRepository jobRepository() throws Exception {
		return mapJobRepositoryFactoryBean().getObject();
	}

	@Bean
	public JobLauncher jobLauncher() throws Exception {
		SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
		simpleJobLauncher.setJobRepository(jobRepository());
		return simpleJobLauncher;
	}

}

