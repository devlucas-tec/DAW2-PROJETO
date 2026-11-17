package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Notificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    @Query("SELECT n FROM Notificacao n WHERE n.usuario.id = :idUsuario AND (:somenteNaoLidas = false OR n.lida = false)")
    Page<Notificacao> buscarDoUsuario(Long idUsuario, boolean somenteNaoLidas, Pageable pageable);

    long countByUsuarioIdAndLidaFalse(Long idUsuario);

    @Modifying
    @Query("UPDATE Notificacao n SET n.lida = true WHERE n.usuario.id = :idUsuario AND n.lida = false")
    int marcarTodasComoLidas(Long idUsuario);

    @Modifying
    @Query("DELETE FROM Notificacao n WHERE n.usuario.id = :idUsuario")
    int removerDoUsuario(Long idUsuario);
}
