package br.edu.ifpb.es.daw.service;

import br.edu.ifpb.es.daw.model.Pedido;
import br.edu.ifpb.es.daw.model.PedidoHistorico;
import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.enums.EtapaPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PedidoHistoricoService {

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticado;

    /** Adiciona uma etapa à timeline do pedido, com o usuário logado como autor. É salvo junto com o pedido (cascade). */
    public PedidoHistorico registrar(Pedido pedido, EtapaPedido etapa, String descricao) {
        PedidoHistorico registro = new PedidoHistorico();
        registro.setPedido(pedido);
        registro.setEtapa(etapa);
        registro.setDescricao(descricao);
        registro.setData(LocalDateTime.now());
        usuarioAutenticado.getUsuarioOpcional().ifPresent(autor -> preencherAutor(registro, autor));
        pedido.getHistorico().add(registro);
        return registro;
    }

    public static void preencherAutor(PedidoHistorico registro, Usuario autor) {
        registro.setIdAutor(autor.getId());
        registro.setNomeAutor(autor.getNome());
        registro.setPapelAutor(autor.getRole().name());
    }
}
