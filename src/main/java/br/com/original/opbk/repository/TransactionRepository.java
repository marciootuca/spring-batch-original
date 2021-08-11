package br.com.original.opbk.repository;

import br.com.original.opbk.presenter.RealizFuturoPresenter;
import br.com.original.opbk.presenter.RefuseTransactionPresenter;

public interface TransactionRepository {
	 void insertTransaction(RealizFuturoPresenter trans);
	 void insertRefuseTransaction(RefuseTransactionPresenter refuse);

}