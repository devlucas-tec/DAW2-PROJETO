package br.edu.ifpb.es.daw.mapper;

import br.edu.ifpb.es.daw.model.EnderecoEntrega;
import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.PedidoHistorico;
import br.edu.ifpb.es.daw.rest.dto.response.EnderecoEntregaResponseDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoHistoricoResponseDTO;
import br.edu.ifpb.es.daw.rest.dto.response.PedidoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ItemPedidoMapper.class)
public interface PedidoMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    @Mapping(target = "codigoCompra", source = "compra.codigo")
    @Mapping(target = "idCupom", source = "cupom.id")
    @Mapping(target = "codigoCupom", source = "cupom.codigo")
    @Mapping(target = "idPagamento", source = "pagamento.id")
    @Mapping(target = "metodoPagamento", source = "pagamento.metodo")
    @Mapping(target = "statusPagamento", source = "pagamento.status")
    @Mapping(target = "idEntrega", source = "entrega.id")
    @Mapping(target = "statusEntrega", source = "entrega.statusEntrega")
    @Mapping(target = "transportadora", source = "entrega.transportadora")
    @Mapping(target = "codigoRastreamento", source = "entrega.codigoRastreamento")
    @Mapping(target = "dataEnvio", source = "entrega.dataEnvio")
    @Mapping(target = "dataEntregaPrevista", source = "entrega.dataEntregaPrevista")
    @Mapping(target = "dataEntrega", source = "entrega.dataEntrega")
    PedidoResponseDTO from(Pedido obj);

    EnderecoEntregaResponseDTO from(EnderecoEntrega obj);

    @Mapping(target = "idPedido", source = "pedido.id")
    PedidoHistoricoResponseDTO from(PedidoHistorico obj);

    List<PedidoHistoricoResponseDTO> historico(List<PedidoHistorico> historico);
}
