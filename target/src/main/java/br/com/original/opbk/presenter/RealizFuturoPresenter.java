package br.com.original.opbk.presenter;

import br.com.original.opbk.enums.AccountPostingType;
import br.com.original.opbk.enums.AccountType;
import br.com.original.opbk.enums.PersonType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RealizFuturoPresenter {
    private static final String VALUE_NA = "NA";
    private static final String REG_ATIVO_S = "S";
    private static final String COD_MOEDA_BRL = "BRL";
    private static final String TABELA_ORIGEM = "opbk.r_txn_hist";


    @JsonProperty("NOM_MARCA_PARTC")
    private String nomMarcaPartc;

    @JsonProperty("NUM_CAD_INSTC_FINCR")
    private String numCadInstcFincr;

    @JsonProperty("NOM_TPO_CTA")
    private String nomTpoCta;

    @JsonProperty("NUM_TRANS_CTA")
    private Long numTransCta;

    @JsonProperty("NOM_TPO_CONCL_TRANS")
    private String nomTpoConclTrans;

    @JsonProperty("NOM_TPO_LCTO")
    private String nomTpoLcto;

    @JsonProperty("NOM_TRANS")
    private String nomTrans;

    @JsonProperty("NOM_TPO_TRANS")
    private String nomTpoTrans;

    @JsonProperty("VAL_TRANS")
    private Double valTrans;

    @JsonProperty("HOR_TRANS")
    private String horTrans;

    @JsonProperty("AG_CTA")
    private String agCta;

    @JsonProperty("NUM_CTA")
    private String numCta;

    @JsonProperty("NUM_CAD_PSSOA_ENVOL")
    private String numCadPssoaEnvol;

    @JsonProperty("NOM_TPO_PSSOA_ENVOL")
    private String nomTpoPssoaEnvol;

    @JsonProperty("COD_INSTC_ENVOL")
    private Long codInstcEnvol;

    @JsonProperty("COD_AG_ENVOL")
    private Long codAgEnvol;

    @JsonProperty("NUM_CTA_ENVOL")
    private String numCtaEnvol;

    @JsonProperty("NUM_DIGTO_CTA_ENVOL")
    private String numDigtoCtaEnvol;

    @JsonProperty("IND_REG_ATIVO")
    private String indRegAtivo;

    @JsonProperty("COD_MOEDA_TRANS")
    private String codMoedaTrans;

    @JsonProperty("NOM_TBELA_ORIGE_TRANS")
    private String nomTbelaOrigeTrans;

    @JsonProperty("IDTFD_TRANS_TBELA_ORIGE")
    private Long idtfdTransTbelaOrige;

    public  static RealizFuturoPresenter fromRealizFuturoPresenter(TransactionOPBKPresenter transPresenter){
        boolean isCredit = AccountPostingType.CREDITO.getCode().longValue() == transPresenter.getPureAction();
        RealizFuturoPresenter realizPresenter =  RealizFuturoPresenter.builder()
                .nomMarcaPartc(transPresenter.getNomMarcaPartc())
                .numCadInstcFincr(transPresenter.getNumCadInstcFincr())
                .nomTpoCta( transPresenter.getAccountType() == 149
                        ? AccountType.CONTA_PAGAMENTO_PRE_PAGA.name()
                        : AccountType.CONTA_DEPOSITO_A_VISTA.name())
                .agCta(Objects.isNull(transPresenter.getAgCta())
                        ? "0"
                        : StringUtils.leftPad(transPresenter.getAgCta().toString(),4,"0"))
                .numCta(StringUtils.leftPad(transPresenter.getNumCta().substring(0,transPresenter.getNumCta().length() -1),8,"0"))
                .numTransCta(transPresenter.getNumTransCta())
                .nomTpoConclTrans(transPresenter.getNomTpoConclTrans())
                .nomTpoLcto(isCredit
                        ? AccountPostingType.CREDITO.getDescription()
                        : AccountPostingType.DEBITO.getDescription())
                .nomTrans(Objects.isNull(transPresenter.getNomTrans())
                        ? VALUE_NA
                        : transPresenter.getNomTrans())
                .nomTpoTrans(Objects.isNull(transPresenter.getNomTpoTrans())
                        ? VALUE_NA
                        : transPresenter.getNomTpoTrans())
                .valTrans(Objects.isNull(transPresenter.getValTrans())
                        ? 0
                        : transPresenter.getValTrans())
                .horTrans(transPresenter.getHorTrans().toString())
                .indRegAtivo(REG_ATIVO_S)
                .codMoedaTrans(COD_MOEDA_BRL)
                .nomTbelaOrigeTrans(TABELA_ORIGEM)
                .idtfdTransTbelaOrige(transPresenter.getIdHist())
                .build();

          return toPessoalEnvol(isCredit,realizPresenter,transPresenter);
    }

    private static RealizFuturoPresenter toPessoalEnvol(boolean isCredit, RealizFuturoPresenter envol, TransactionOPBKPresenter trans){
        return Optional.ofNullable(trans)
                .filter(f ->  (!isCredit && f.getTargetIdentificationNumber() != null) || (isCredit && f.getOriginIdentificationNumber() != null))
                .map(transPresenter -> {
                    if(Objects.nonNull(transPresenter.getTargetIdentificationNumber())){
                        envol.setNumCadPssoaEnvol(transPresenter.getTargetIdentificationNumber());
                        envol.setNomTpoPssoaEnvol(transPresenter.getTargetIdentificationNumber().length() <= 11
                                ? PersonType.PF.getDescription()
                                : PersonType.PJ.getDescription());
                        envol.setCodInstcEnvol(transPresenter.getTargetEntity());
                        envol.setCodAgEnvol(transPresenter.getTargetBranch());
                        envol.setNumCtaEnvol(transPresenter.getTargetAccount());
                        envol.setNumDigtoCtaEnvol(transPresenter.getTargetAccount().substring(transPresenter.getTargetAccount().length() -1));
                    }else{
                        envol.setNumCadPssoaEnvol(transPresenter.getOriginIdentificationNumber());
                        envol.setNomTpoPssoaEnvol(transPresenter.getOriginIdentificationNumber().length() <= 11
                                ? PersonType.PF.getDescription()
                                : PersonType.PJ.getDescription());
                        envol.setCodInstcEnvol(transPresenter.getOriginEntity());
                        envol.setCodAgEnvol(transPresenter.getOriginBranch());
                        envol.setNumCtaEnvol(transPresenter.getOriginAccount());
                        envol.setNumDigtoCtaEnvol(transPresenter.getOriginAccount().substring(transPresenter.getOriginAccount().length() -1));
                    }
                    return envol;
                   }
                ).orElse(envol);

    }

}
