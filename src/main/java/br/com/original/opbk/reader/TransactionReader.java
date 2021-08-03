package br.com.original.opbk.reader;

import br.com.original.opbk.presenter.TransactionOPBKPresenter;
import br.com.original.opbk.repository.TransactionRepository;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class TransactionReader implements ItemReader<TransactionOPBKPresenter> {

	@Autowired
	private TransactionRepository transactionRepository;

	private Iterator<TransactionOPBKPresenter> transactionIterator;

	@Value("${DATE_BEGIN_TRANSACTION}")
	private String dateBegin;

	@BeforeStep
	public void before(StepExecution stepExecution) {
		var result = transactionRepository.getTransactionHistory(dateBegin);
		transactionIterator = result.iterator();
	}

	@Override
	public TransactionOPBKPresenter read() {
		if(transactionIterator != null && transactionIterator.hasNext()) {
			return transactionIterator.next();
		}
		return null;
	}

}
