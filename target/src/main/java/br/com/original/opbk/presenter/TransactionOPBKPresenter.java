package br.com.original.opbk.presenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionOPBKPresenter {

	@JsonProperty("NOM_MARCA_PARTC")
	private String nomMarcaPartc;

	@JsonProperty("NUM_CAD_INSTC_FINCR")
	private String numCadInstcFincr;

	@JsonProperty("ACCOUNT_TYPE")
	private Long accountType;

	@JsonProperty("PURE_ACTION")
	private Long pureAction;

	@JsonProperty("AG_CTA")
	private Long agCta;

	@JsonProperty("NUM_TRANS_CTA")
	private Long numTransCta;

	@JsonProperty("NOM_TPO_CONCL_TRANS")
	private String nomTpoConclTrans;

	@JsonProperty("NOM_TRANS")
	private String nomTrans;

	@JsonProperty("NOM_TPO_TRANS")
	private String nomTpoTrans;

	@JsonProperty("VAL_TRANS")
	private Double valTrans;

	@JsonProperty("HOR_TRANS")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private String horTrans;

	@JsonProperty("NUM_CTA")
	private String numCta;

	@JsonProperty("TARGET_IDENTIFICATION_NUMBER")
	private String targetIdentificationNumber;

	@JsonProperty("TARGET_ENTITY")
	private Long targetEntity;

	@JsonProperty("TARGET_BRANCH")
	private Long targetBranch;

	@JsonProperty("TARGET_ACCOUNT")
	private String targetAccount;

	@JsonProperty("ORIGIN_IDENTIFICATION_NUMBER")
	private String originIdentificationNumber;

	@JsonProperty("ORIGIN_ENTITY")
	private Long originEntity;

	@JsonProperty("ORIGIN_BRANCH")
	private Long originBranch;

	@JsonProperty("ORIGIN_ACCOUNT")
	private String originAccount;

	@JsonProperty("IND_REG_ATIVO")
	private String indRegAtivo;

	@JsonProperty("COD_MOEDA_TRANS")
	private String codMoedaTrans;

	@JsonProperty("TRANSACTION_REASON")
	private Long transactionReason;

	@JsonProperty("ID_HIST")
	private Long idHist;

}
