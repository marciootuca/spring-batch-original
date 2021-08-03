package br.com.original.opbk.repository;

import br.com.original.opbk.presenter.RealizFuturoPresenter;
import br.com.original.opbk.presenter.RefuseTransactionPresenter;
import br.com.original.opbk.presenter.TransactionOPBKPresenter;
import br.com.original.opbk.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<TransactionOPBKPresenter> getTransactionHistory(String date) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource()
				.addValue("DATE_BEGIN_TRANSACTION",  Timestamp.valueOf(DateUtil.convertStringToLocalDateTime(date)));
		return namedParameterJdbcTemplate.query(EnumTransactionQueries.GET_TRANSACTION_HISTORY.getQuery(), paramMap, new BeanPropertyRowMapper<>(TransactionOPBKPresenter.class));
	}

	@Override
	public void insertTransaction(RealizFuturoPresenter trans) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource()
			.addValue("NOM_MARCA_PARTC", trans.getNomMarcaPartc())
			.addValue("NUM_CAD_INSTC_FINCR", trans.getNumCadInstcFincr())
			.addValue("NOM_TPO_CTA", trans.getNomTpoCta())
			.addValue("AG_CTA", trans.getAgCta())
			.addValue("NUM_CTA", trans.getNumCta())
			//.addValue("NUM_TRANS_CTA", trans.getNumTransCta())
			.addValue("NOM_TPO_CONCL_TRANS",  trans.getNomTpoConclTrans())
			.addValue("NOM_TPO_LCTO", trans.getNomTpoLcto())
			.addValue("NOM_TRANS", trans.getNomTrans())
			.addValue("NOM_TPO_TRANS", trans.getNomTpoTrans())
			.addValue("VAL_TRANS", trans.getValTrans())
			.addValue("HOR_TRANS", Timestamp.valueOf(trans.getHorTrans()))
			.addValue("NUM_CAD_PSSOA_ENVOL", trans.getNumCadPssoaEnvol())
			.addValue("NOM_TPO_PSSOA_ENVOL", trans.getNomTpoPssoaEnvol())
			.addValue("COD_INSTC_ENVOL", trans.getCodInstcEnvol())
			.addValue("COD_AG_ENVOL", trans.getCodAgEnvol())
			.addValue("NUM_CTA_ENVOL", trans.getNumCtaEnvol())
			.addValue("NUM_DIGTO_CTA_ENVOL", trans.getNumDigtoCtaEnvol())
			.addValue("IND_REG_ATIVO", trans.getIndRegAtivo())
			.addValue("COD_MOEDA_TRANS", trans.getCodMoedaTrans())
			.addValue("NOM_TBELA_ORIGE_TRANS", trans.getNomTbelaOrigeTrans())
			.addValue("IDTFD_TRANS_TBELA_ORIGE", trans.getIdtfdTransTbelaOrige());

		namedParameterJdbcTemplate.update(EnumTransactionQueries.INSERT_TRANSACTION.getQuery(), paramMap);
	}

	@Override
	public void insertRefuseTransaction(RefuseTransactionPresenter refuse){
		MapSqlParameterSource paramMap = new MapSqlParameterSource()
				.addValue("HOR_INCL_RCUSA",  Timestamp.valueOf(refuse.getDataHoraInclusao()))
				.addValue("NOM_TBELA_ORIGE_RCUSA", refuse.getNomeTabela())
				.addValue("DES_ORIGE_RCUSA", refuse.getDescricaoRecusa())
				.addValue("DES_PROCS_RCUSA", refuse.getDescricaoProcessoRecusa())
				.addValue("DES_OBJET_RCUSA", refuse.getDescricaoObjetoRecusa())
				.addValue("COD_TPO_SIT", refuse.getCodTipoSituacao())
				.addValue("COD_LOGIN_USUAR_ATULZ", refuse.getCodLoginUsuario());
		namedParameterJdbcTemplate.update(EnumTransactionQueries.INSERT_RECUSA.getQuery(),  paramMap);
	}

}