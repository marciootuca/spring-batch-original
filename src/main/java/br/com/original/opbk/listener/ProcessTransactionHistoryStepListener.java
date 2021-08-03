package br.com.original.opbk.listener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.original.fwcl.annotations.OriginalLogger;
import br.com.original.fwcl.components.logger.OriginalLogLogger;
import br.com.original.opbk.repository.EnumTransactionQueries;

@SuppressWarnings({ "java:S3457", "java:S2629" })
@Component
public class ProcessTransactionHistoryStepListener implements StepExecutionListener {
	
	@Autowired
	@OriginalLogger
	private OriginalLogLogger logger;
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info("Inicio do Step ProcessExpiredConsentsStep: " + dateTimeToString(LocalDateTime.now()));
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info("== TOTALIZADOR == ");
		logger.info("Quantidade de lidos: " + stepExecution.getReadCount());
		logger.info("== QUERY EXECUTADA == ");
		logger.info(EnumTransactionQueries.GET_TRANSACTION_HISTORY.getQuery());
		logger.info("Fim do Step : " + dateTimeToString(LocalDateTime.now()));

		return null;
	}

	private String dateTimeToString(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss,SSSSSSSSS");
		return formatter.format(dateTime);
	}
}
