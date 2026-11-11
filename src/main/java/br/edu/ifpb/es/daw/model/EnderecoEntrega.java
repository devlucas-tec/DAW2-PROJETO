package br.edu.ifpb.es.daw.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Cópia do endereço no momento da compra. O pedido mantém o endereço original
 * mesmo que o cliente altere ou exclua o endereço depois.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Embeddable
public class EnderecoEntrega {

    @Column(name = "entrega_rua", length = 100)
    private String rua;

    @Column(name = "entrega_numero", length = 10)
    private String numero;

    @Column(name = "entrega_complemento", length = 50)
    private String complemento;

    @Column(name = "entrega_cep", length = 10)
    private String cep;

    @Column(name = "entrega_cidade", length = 50)
    private String cidade;

    @Column(name = "entrega_estado", length = 2)
    private String estado;

    public static EnderecoEntrega de(Endereco endereco) {
        EnderecoEntrega copia = new EnderecoEntrega();
        copia.setRua(endereco.getRua());
        copia.setNumero(endereco.getNumero());
        copia.setComplemento(endereco.getComplemento());
        copia.setCep(endereco.getCep());
        copia.setCidade(endereco.getCidade());
        copia.setEstado(endereco.getEstado());
        return copia;
    }
}
