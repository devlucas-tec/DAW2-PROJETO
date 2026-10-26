package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    default Optional<Usuario> findByEmail(String email) {
        return findByEmailIgnoreCase(email);
    }
}
