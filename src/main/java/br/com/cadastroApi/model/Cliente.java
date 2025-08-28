package br.com.cadastroApi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @NotNull
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;


    @NotNull
    @Column(name = "cpf", nullable = false, length = 12)
    private String cpf;

    @NotNull
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;


    @NotNull
    @Column(name = "email", nullable = false, length = 120)
    private String email;

    @NotNull
    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "idade")
    Integer idade;

    @Column(name = "cep", length = 9)
    String cep;

    @Size(max = 255)
    @NotNull
    @Column(name = "endereco", nullable = false)
    private String endereco;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ColumnDefault("now()")
    @Column(name = "updated_at")
    private LocalDateTime  updatedAt;

}