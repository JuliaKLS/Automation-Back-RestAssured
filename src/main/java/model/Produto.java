package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    private String nome;
    private Integer preco;
    private String descricao;
    private Integer quantidade;
}
