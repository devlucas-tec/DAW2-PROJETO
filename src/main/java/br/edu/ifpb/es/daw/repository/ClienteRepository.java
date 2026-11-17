package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE c.email = :email")
    Optional<Cliente> buscarPorEmail(String email);

    @Query("""
            SELECT c FROM Cliente c
            WHERE (:nome  IS NULL OR LOWER(c.nome)  LIKE LOWER(CONCAT('%', CAST(:nome  AS string), '%')))
            AND   (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', CAST(:email AS string), '%')))
            """)
    Page<Cliente> filtrar(String nome, String email, Pageable pageable);

    /**
     * Trava a linha do usuário (SELECT ... FOR UPDATE) até o fim da transação. Serve para serializar as
     * operações no carrinho de um mesmo cliente: sem isso, duas requisições simultâneas adicionando o mesmo
     * produto leem o carrinho sem ele e as duas tentam inserir a mesma linha em item_carrinho.
     * É uma consulta nativa porque o @Lock do JPA não gera o FOR UPDATE na herança JOINED (cliente + usuario).
     */
    @Query(value = "SELECT id FROM usuario WHERE id = :id FOR UPDATE", nativeQuery = true)
    Long travarParaAtualizacao(Long id);
}
