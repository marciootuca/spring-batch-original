package br.com.original.opbk.writer;

import br.com.original.fwcl.annotations.OriginalLogger;
import br.com.original.fwcl.components.logger.OriginalLogLogger;
import br.com.original.opbk.presenter.RealizFuturoPresenter;
import br.com.original.opbk.presenter.RefuseTransactionPresenter;
import br.com.original.opbk.presenter.TransactionOPBKPresenter;
import br.com.original.opbk.repository.TransactionRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionWriter implements ItemWriter<TransactionOPBKPresenter> {

	@Autowired
	@OriginalLogger
	private OriginalLogLogger logger;

	@Autowired
	private TransactionRepository transactionRepository;

	@Value("${spring.application.name}")
	private String applicationName;

	private static final String TABLE_NAME_R_TXN_HIST = "R_TXN_HIST";

	@Override
	public void write(List<? extends TransactionOPBKPresenter> transactions) throws Exception {
		logger.info("[itemWriter] Iniciando processo de inserção das contas.");
		 transactions.parallelStream()
				 .map(RealizFuturoPresenter::fromRealizFuturoPresenter)
				 .forEachOrdered( trans -> {
					 try {
							transactionRepository.insertTransaction(trans);
						}catch (Exception e){
							logger.error(e.toString());
							transactionRepository.insertRefuseTransaction(
							RefuseTransactionPresenter.toRefuseTransactionPresenter(e.toString(),TABLE_NAME_R_TXN_HIST,applicationName, trans)
							);
						}
				 	});
		logger.info("[itemWriter] Fim do processo.");

	}


}
