package br.com.original.opbk.repository;

import br.com.original.opbk.presenter.RealizFuturoPresenter;
import br.com.original.opbk.presenter.RefuseTransactionPresenter;
import br.com.original.opbk.presenter.TransactionOPBKPresenter;

import java.util.List;

public interface TransactionRepository {
	 void insertTransaction(RealizFuturoPresenter trans);
	 void insertRefuseTransaction(RefuseTransactionPresenter refuse);

}