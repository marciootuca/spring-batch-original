package br.com.original.opbk.repository;

public enum EnumTransactionQueries {

	GET_TRANSACTION_HISTORY("SELECT \n" +
			"'BANCO ORIGINAL S.A' NOM_MARCA_PARTC,\n" +
			" '92894922000108' NUM_CAD_INSTC_FINCR,\n" +
			" SA.ACCOUNT_TYPE ,\n" +
			" C.BRANCH  AG_CTA, \n" +
			" TH.REFERENCE_NUMBER NUM_TRANS_CTA,\n" +
			" 'TRANSACAO_EFETIVADA' NOM_TPO_CONCL_TRANS,\n" +
			" TH.PURE_ACTION , \n" + //NOM_TPO_LCTO
			" CDPO.DES_CONTD_PRODT NOM_TRANS,\n" +
			" CDPO.DES_CONTD_OPBK NOM_TPO_TRANS,\n" +
			" TH.AMOUNT VAL_TRANS,\n" +
			" TH.IMPUTATION_TIME  HOR_TRANS,\n" +
			" C.OPERATION_ID NUM_CTA,\n" +
			" TD.TARGET_IDENTIFICATION_NUMBER,\n" +
			" TD.TARGET_ENTITY,\n" +
			" TD.TARGET_BRANCH,\n" +
			" TD.TARGET_ACCOUNT,\n" +
			" TD1.ORIGIN_IDENTIFICATION_NUMBER,\n" +
			" TD1.ORIGIN_ENTITY,\n" +
			" TD1.ORIGIN_BRANCH,\n" +
			" TD1.ORIGIN_ACCOUNT,\n" +
			" NULL IND_REG_ATIVO,  \n" +
			" NULL COD_MOEDA_TRANS, \n" +
			" TH.TRANSACTION_REASON, \n"+
			" TH.ID ID_HIST \n"+
			"FROM OPBK.R_TXN_HIST TH\n" +
			"INNER JOIN OPBK.R_ACCOUNT C ON (C.ID = TH.ACCOUNT AND C.STATUS = 7 )\n" +
			"INNER JOIN OPBK.R_SUBPRODUCT_ACCOUNT SA ON sa.subproduct = C.SUBPRODUCT AND SA.ACCOUNT_TYPE IN (46,51,109,149)\n" +
			"LEFT JOIN OPBK.R_TRANSACTION_DETAIL TD ON TD.TARGET_REFERENCE_NUMBER  =  TH.REFERENCE_NUMBER \n" +
			"LEFT JOIN OPBK.R_TRANSACTION_DETAIL TD1 ON TD1.ORIGIN_REFERENCE_NUMBER  =  TH.REFERENCE_NUMBER\n" +
			"LEFT JOIN OPBK.CONVS_DOMNO_PRODT_OPBK CDPO ON\n" +
			" (TH.TRANSACTION_REASON = CDPO.IDTFD_FIS_PRODT AND CDPO.NOM_TBELA_PRODT  = 'cyberbank_core.transaction_reason' " +
			" AND  CDPO.NOM_ATRIB_PRODT = 'short_desc') \n" +
			"WHERE TH.IMPUTATION_TIME >= :DATE_BEGIN_TRANSACTION  \n" +
			//" and rownum < 100 " +
			"ORDER BY TH.IMPUTATION_TIME"),
	
	INSERT_TRANSACTION("INSERT INTO OPBK.CTA_TRANS_REALZ_FUTUR " +
			" (NOM_MARCA_PARTC,\n" +
			"  NUM_CAD_INSTC_FINCR,\n" +
			"  NOM_TPO_CTA,\n" +
			"  COD_AG,  \n" +
			"  NUM_TRANS_CTA, \n" +
			"  NOM_TPO_CONCL_TRANS,\n" +
			"  NOM_TPO_LCTO, \n" +
			"  NOM_TRANS, \n" +
			"  NOM_TPO_TRANS, \n" +
			"  VAL_TRANS, \n" +
			"  HOR_TRANS,\n" +
			"  NUM_CTA, \n" +
			"  NUM_CAD_PSSOA_ENVOL,\n" +
			"  NOM_TPO_PSSOA_ENVOL, \n" +
			"  COD_INSTC_ENVOL, \n" +
			"  COD_AG_ENVOL, \n" +
			"  NUM_CTA_ENVOL, \n" +
			"  NUM_DIGTO_CTA_ENVOL, \n" +
			"  IND_REG_ATIVO, \n" +
			"  COD_MOEDA_TRANS," +
			"  NOM_TBELA_ORIGE_TRANS," +
			"  IDTFD_TRANS_TBELA_ORIGE" +
			")" +
			"VALUES              " +
			" (:NOM_MARCA_PARTC,\n" +
			"  :NUM_CAD_INSTC_FINCR,\n" +
			"  :NOM_TPO_CTA,\n" +
			"  :AG_CTA,  \n" +
			"  OPBK.CTA_TRANS_REALZ_FUTUR_SEQ.NEXTVAL, \n" +
			"  :NOM_TPO_CONCL_TRANS,\n" +
			"  :NOM_TPO_LCTO, \n" +
			"  :NOM_TRANS, \n" +
			"  :NOM_TPO_TRANS, \n" +
			"  :VAL_TRANS, \n" +
			"  :HOR_TRANS,\n" +
			"  :NUM_CTA, \n" +
			"  :NUM_CAD_PSSOA_ENVOL,\n" +
			"  :NOM_TPO_PSSOA_ENVOL, \n" +
			"  :COD_INSTC_ENVOL, \n" +
			"  :COD_AG_ENVOL, \n" +
			"  :NUM_CTA_ENVOL, \n" +
			"  :NUM_DIGTO_CTA_ENVOL, \n" +
			"  :IND_REG_ATIVO, \n" +
			"  :COD_MOEDA_TRANS," +
			"  :NOM_TBELA_ORIGE_TRANS," +
			"  :IDTFD_TRANS_TBELA_ORIGE" +
			")"

	),

	INSERT_RECUSA(
			"INSERT INTO\n" +
					" OPBK.RCUSA_MOTOR_DADOS (" +
					" HOR_INCL_RCUSA,\n" +
					" NOM_TBELA_ORIGE_RCUSA,\n" +
					" DES_ORIGE_RCUSA,\n" +
					" DES_PROCS_RCUSA,\n" +
					" DES_OBJET_RCUSA,\n" +
					" NUM_RCUSA,\n" +
					" COD_TPO_SIT,\n" +
					" COD_LOGIN_USUAR_ATULZ)\n" +
					" VALUES\n" +
					" (:HOR_INCL_RCUSA,\n" +
					" :NOM_TBELA_ORIGE_RCUSA,\n" +
					" :DES_ORIGE_RCUSA,\n" +
					" :DES_PROCS_RCUSA,\n" +
					" :DES_OBJET_RCUSA,\n" +
					" OPBK.RCUSA_MOTOR_DADOS_SEQ.NEXTVAL,\n" +
					" :COD_TPO_SIT,\n" +
					" :COD_LOGIN_USUAR_ATULZ)");
	
	private String query;
	
	private EnumTransactionQueries(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return this.query;
	}
}
