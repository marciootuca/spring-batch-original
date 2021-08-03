package br.com.original.opbk.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import br.com.original.fwcl.annotations.OriginalLogger;
import br.com.original.fwcl.components.logger.OriginalLogLogger;
import br.com.original.opbk.listener.ProcessTransactionHistoryStepListener;
import br.com.original.opbk.presenter.TransactionOPBKPresenter;
import br.com.original.opbk.reader.TransactionReader;
import br.com.original.opbk.writer.TransactionWriter;

@Configuration
public class SynchronizationTransactionJob {

	@Autowired
    @OriginalLogger
    private OriginalLogLogger originalLogger;
	
	@Value("${chunk}")
	private int chunk;

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor("opbk-transacao-batch");
		simpleAsyncTaskExecutor.setConcurrencyLimit(1);
		return simpleAsyncTaskExecutor;
	}

	@Bean
	public Job transactionJob(Step processTransactionStep, JobBuilderFactory jobBuilderFactory) {
		this.originalLogger.info("Iniciando JOB.");
		return jobBuilderFactory.get("transactionJob")
                .incrementer(new RunIdIncrementer())
				.start(processTransactionStep)
				.preventRestart()
				.build();
	}
	
	@Bean
    Step processTransactionStep(TransactionReader reader, TransactionWriter writer, StepBuilderFactory stepBuilderFactory, ProcessTransactionHistoryStepListener listener){
		this.originalLogger.info("Iniciando o passo para iniciar a leitura, o processamento e a escrita.");
		return stepBuilderFactory.get("processTransactionStep").<TransactionOPBKPresenter, TransactionOPBKPresenter>chunk(chunk)
				.reader(reader)
				.writer(writer)
				.taskExecutor(taskExecutor())
				.listener(listener)
				.build();
	}
}