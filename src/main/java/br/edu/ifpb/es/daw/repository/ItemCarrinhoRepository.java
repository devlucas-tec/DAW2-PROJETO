package br.edu.ifpb.es.daw.repository;

import br.edu.ifpb.es.daw.model.ItemCarrinho;
import br.edu.ifpb.es.daw.model.ItemCarrinhoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;

@Repository
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, ItemCarrinhoId> {

    /** Tira produtos de todos os carrinhos (produto removido ou vendedor que excluiu a conta). */
    @Modifying
    @Query("DELETE FROM ItemCarrinho i WHERE i.produto.id IN :idsProdutos")
    int removerProdutosDosCarrinhos(Collection<Long> idsProdutos);
}
