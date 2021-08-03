package br.com.original.opbk.presenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefuseTransactionPresenter {

    @JsonProperty("HOR_INCL_RCUSA")
    private LocalDateTime dataHoraInclusao;

    @JsonProperty("NOM_TBELA_ORIGE_RCUSA")
    private String nomeTabela;

    @JsonProperty("DES_ORIGE_RCUSA")
    private String descricaoRecusa;

    @JsonProperty("DES_PROCS_RCUSA")
    private String descricaoProcessoRecusa;

    @JsonProperty("DES_OBJET_RCUSA")
    private String descricaoObjetoRecusa;

    @JsonProperty("NUM_RCUSA")
    private Long numeroRecusa;

    @JsonProperty("COD_TPO_SIT")
    private String codTipoSituacao;

    @JsonProperty("COD_LOGIN_USUAR_ATULZ")
    private String codLoginUsuario;

    public static RefuseTransactionPresenter toRefuseTransactionPresenter(String messageError,String tableName, String applicationName, RealizFuturoPresenter realizFuturoPresenter ){
            return  RefuseTransactionPresenter
                    .builder()
                    .dataHoraInclusao(LocalDateTime.now())
                    .nomeTabela(tableName)
                    .descricaoRecusa(messageError)
                    .descricaoProcessoRecusa(applicationName)
                    .descricaoObjetoRecusa(new Gson().toJson(realizFuturoPresenter))
                    .codLoginUsuario(applicationName)
                    .build();

    }
}
