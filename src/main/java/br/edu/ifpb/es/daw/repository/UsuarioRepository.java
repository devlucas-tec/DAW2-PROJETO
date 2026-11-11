package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Usuario;
import br.edu.ifpb.es.daw.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    default Optional<Usuario> findByEmail(String email) {
        return findByEmailIgnoreCase(email);
    }

    @Query("""
            SELECT u FROM Usuario u
            WHERE (:nome  IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%'))
                                  OR LOWER(u.email) LIKE LOWER(CONCAT('%', CAST(:nome AS string), '%')))
            AND   (:role  IS NULL OR u.role = :role)
            AND   (:ativo IS NULL OR u.ativo = :ativo)
            """)
    Page<Usuario> filtrar(String nome, Role role, Boolean ativo, Pageable pageable);

    List<Usuario> findByDataCadastroGreaterThanEqual(LocalDateTime inicio);
}
