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

import com.google.gson.Gson;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TransactionWriter implements ItemWriter<TransactionOPBKPresenter> {

	@Autowired
	@OriginalLogger
	private OriginalLogLogger logger;

	@Autowired
	private TransactionRepository transactionRepository;

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${THREAD_NUMBER}")
	private Integer threadNumber;

	private static final String TABLE_NAME_R_TXN_HIST = "R_TXN_HIST";

	@Override
	public void write(List<? extends TransactionOPBKPresenter> transactions) throws Exception {
		logger.info("[itemWriter] Iniciando processo de inserção das contas. " + OffsetDateTime.now());

		ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
		CompletableFuture[] futures = transactions.stream()
				.map(RealizFuturoPresenter::fromRealizFuturoPresenter)
				.map(transaction -> CompletableFuture.supplyAsync(() -> transaction))
				.map(future -> future.thenAcceptAsync(trans -> {
							try {
								transactionRepository.insertTransaction(trans);
							} catch (Exception e) {
								logger.error(e.toString());
								transactionRepository.insertRefuseTransaction(
										RefuseTransactionPresenter.toRefuseTransactionPresenter(e.toString(),TABLE_NAME_R_TXN_HIST,applicationName, trans));
							}
						}
						, executor))
				.toArray(CompletableFuture[]::new);
		CompletableFuture.allOf(futures).join();
		executor.shutdown();
		logger.info("[itemWriter] Fim do processo. "+ OffsetDateTime.now());

	}


}
