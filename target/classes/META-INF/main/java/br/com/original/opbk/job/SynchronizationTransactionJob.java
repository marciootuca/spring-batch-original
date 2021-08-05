package br.com.original.opbk.job;

import br.com.original.opbk.repository.EnumTransactionQueries;
import br.com.original.opbk.utils.DateUtil;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
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
import br.com.original.opbk.writer.TransactionWriter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SynchronizationTransactionJob {

	@Autowired
    @OriginalLogger
    private OriginalLogLogger originalLogger;
	
	@Value("${chunk}")
	private int chunk;

	@Value("${DATE_BEGIN_TRANSACTION}")
	private String dateBegin;

	@Value("${READER_PAGE_SIZE}")
	private Integer pageSize;


	@Autowired
	private DataSource dataSource;


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
    Step processTransactionStep(TransactionWriter writer, StepBuilderFactory stepBuilderFactory, ProcessTransactionHistoryStepListener listener){
		this.originalLogger.info("Iniciando o passo para iniciar a leitura, o processamento e a escrita.");
		return stepBuilderFactory.get("processTransactionStep").<TransactionOPBKPresenter, TransactionOPBKPresenter>chunk(chunk)
				.reader(readerPaging())
				.writer(writer)
				.taskExecutor(taskExecutor())
				.listener(listener)
				.build();
	}

	@Bean
	public JdbcPagingItemReader<TransactionOPBKPresenter> readerPaging() {
		JdbcPagingItemReader<TransactionOPBKPresenter> pagingItemReader = new JdbcPagingItemReader<>();
		Map<String, Object> parametersInput = new HashMap<>();
		parametersInput.put("DATE_BEGIN_TRANSACTION",  Timestamp.valueOf(DateUtil.convertStringToLocalDateTime(dateBegin)));
		pagingItemReader.setDataSource(dataSource);
		pagingItemReader.setFetchSize(pageSize);
		pagingItemReader.setPageSize(pageSize);
		pagingItemReader.setRowMapper(new BeanPropertyRowMapper<>(TransactionOPBKPresenter.class));
		pagingItemReader.setParameterValues(parametersInput);
		pagingItemReader.setQueryProvider(providerQuery());
		return pagingItemReader;
	}
    private OraclePagingQueryProvider providerQuery(){
		OraclePagingQueryProvider oraclePagingQueryProvider = new OraclePagingQueryProvider();
		oraclePagingQueryProvider.setSelectClause(EnumTransactionQueries.GET_TRANSACTION_HISTORY_SELECT.getQuery());
		oraclePagingQueryProvider.setFromClause(EnumTransactionQueries.GET_TRANSACTION_HISTORY_FROM.getQuery());
		oraclePagingQueryProvider.setWhereClause(EnumTransactionQueries.GET_TRANSACTION_HISTORY_WHERE.getQuery());
		Map<String, Order> orderByKeys = new HashMap<>();
		orderByKeys.put("IMPUTATION_TIME", Order.ASCENDING);
		oraclePagingQueryProvider.setSortKeys(orderByKeys);
		return oraclePagingQueryProvider;

	}

}