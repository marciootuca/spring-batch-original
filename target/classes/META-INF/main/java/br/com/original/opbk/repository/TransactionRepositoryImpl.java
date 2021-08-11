package br.com.original.opbk.repository;

import br.com.original.fwcl.annotations.OriginalLogger;
import br.com.original.fwcl.components.logger.OriginalLogLogger;
import br.com.original.opbk.presenter.RealizFuturoPresenter;
import br.com.original.opbk.presenter.RefuseTransactionPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Map;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Value("${spring.application.name}")
	private String applicationName;
	
	@Autowired
	@OriginalLogger
	private OriginalLogLogger logger;

	private static final String TABLE_NAME_R_TXN_HIST = "R_TXN_HIST";

	@Override
	public void insertTransaction(RealizFuturoPresenter trans) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource()
			.addValue("NOM_MARCA_PARTC", trans.getNomMarcaPartc())
			.addValue("NUM_CAD_INSTC_FINCR", trans.getNumCadInstcFincr())
			.addValue("NOM_TPO_CTA", trans.getNomTpoCta())
			.addValue("AG_CTA", "0001")
			.addValue("NUM_CTA", trans.getNumCta())
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
			.addValue("ENVOL_NUM_CTA", trans.getNumCtaEnvol())
			.addValue("NUM_DIGTO_CTA_ENVOL", trans.getNumDigtoCtaEnvol())
			.addValue("IND_REG_ATIVO", trans.getIndRegAtivo())
			.addValue("COD_MOEDA_TRANS", trans.getCodMoedaTrans())
			.addValue("NOM_TBELA_ORIGE_TRANS", trans.getNomTbelaOrigeTrans())
			.addValue("IDTFD_TRANS_TBELA_ORIGE", trans.getIdtfdTransTbelaOrige());

		try {
			namedParameterJdbcTemplate.update(EnumTransactionQueries.INSERT_TRANSACTION.getQuery(), paramMap);
		} catch (Exception e) {
			String sql = getSQL(EnumTransactionQueries.INSERT_TRANSACTION.getQuery(), paramMap);
			String message = getMessage(e.getMessage(), sql);
			logger.error(message);
			insertRefuseTransaction(RefuseTransactionPresenter.toRefuseTransactionPresenter(
					message, TABLE_NAME_R_TXN_HIST,applicationName, trans));
		}
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
		
		namedParameterJdbcTemplate.update(EnumTransactionQueries.INSERT_RECUSA.getQuery(), paramMap);
	}
	
	private String getSQL(String sql, MapSqlParameterSource paramMap) {
		String constructSQL = sql;
		
		for (Map.Entry<String, Object> entry : paramMap.getValues().entrySet()) {
			if (entry.getValue() != null) {
				String value = entry.getValue().toString();
				if (entry.getValue() instanceof Boolean)
					value = Boolean.TRUE.equals((entry.getValue())) ? "1" : "0";
				constructSQL = constructSQL.replaceAll(":" + entry.getKey(), "'" + value + "'");
			} else {
				constructSQL = constructSQL.replaceAll(":" + entry.getKey(), "NULL");
			}
		}
		
		return constructSQL;
    }
	
	private String getMessage(String error, String sql) {
		return "Nao foi possivel processar o registro; error=" + error + "; sql=" + sql;
	}



}