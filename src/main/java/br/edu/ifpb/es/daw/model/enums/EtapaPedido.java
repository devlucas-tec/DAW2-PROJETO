package br.edu.ifpb.es.daw.model.enums;

/**
 * Etapas do ciclo de vida de um pedido, usadas na timeline e nas transições feitas pelo vendedor.
 * Fluxo normal: PEDIDO_CONFIRMADO → PAGAMENTO_APROVADO → EMBALADO → EM_TRANSITO → SAIU_PARA_ENTREGA → ENTREGUE.
 */
public enum EtapaPedido {
    PEDIDO_CRIADO,
    PEDIDO_CONFIRMADO,
    PAGAMENTO_APROVADO,
    PAGAMENTO_RECUSADO,
    EMBALADO,
    EM_TRANSITO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO,
    DEVOLUCAO_SOLICITADA,
    DEVOLUCAO_RECUSADA,
    DEVOLVIDO;

    public static EtapaPedido de(StatusEntrega status) {
        return EtapaPedido.valueOf(status.name());
    }
}
