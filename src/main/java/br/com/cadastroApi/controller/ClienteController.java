package br.com.cadastroApi.controller;


import br.com.cadastroApi.dto.ClienteAtualizarDto;
import br.com.cadastroApi.dto.ClienteCadastrarDto;
import br.com.cadastroApi.dto.ClienteDto;
import org.hibernate.validator.constraints.br.CPF;
import br.com.cadastroApi.services.ClienteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;



    @PostMapping()
    public ResponseEntity<ClienteDto> cadastrarPessoaFisica(@Valid @RequestBody ClienteCadastrarDto clienteCadastrarDto) {
        return ResponseEntity.ok().body(clienteService.cadastrarCliente(clienteCadastrarDto));
    }

    @PutMapping()
    public ResponseEntity<ClienteDto> atualizarPessoaFisica(@Valid @RequestBody ClienteAtualizarDto clienteDto) {
        return ResponseEntity.ok().body(clienteService.atualizarCliente(clienteDto));
    }

    @DeleteMapping()
    public void deletarCliente(@RequestParam Long id) {
        clienteService.deletarCliente(id);
    }

    @GetMapping("paginacao")
    public ResponseEntity<List<ClienteDto>> buscarClientesComPaginacao(
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok().body(clienteService.buscarClientesComPaginacao(page, size));
    }

    @GetMapping()
    public ResponseEntity<List<ClienteDto>> buscarPessoasFisica() {
        return ResponseEntity.ok().body(clienteService.buscarClientes());
    }

    @GetMapping("id")
    public ResponseEntity<ClienteDto> buscarClientePorId(@RequestParam Long id) {
        return ResponseEntity.ok().body(clienteService.buscarClientePorId(id));
    }


    @GetMapping("nome")
    public ResponseEntity<List<ClienteDto>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok().body(clienteService.buscarPorNome(nome));
    }

    @GetMapping("cpf")
    public ResponseEntity<ClienteDto> buscarPorCpf(
            @RequestParam
            @CPF
            String cpf) {
        return ResponseEntity.ok().body(clienteService.buscarPorCpf(cpf));
    }

    @GetMapping("email")
    public ResponseEntity<ClienteDto> buscarPorEmail( @Email(message = "E-mail inválido. Formato esperado: teste@gmail.com") @RequestParam String email) {
        return ResponseEntity.ok().body(clienteService.buscarPorEmail(email));
    }

    @GetMapping("endereco")
    public ResponseEntity<List<ClienteDto>> buscarPorEndereco(@RequestParam String endereco) {
        return ResponseEntity.ok().body(clienteService.buscarPorEndereco(endereco));
    }

    @GetMapping("idade")
    public ResponseEntity<List<ClienteDto>> buscarPorIdade(@RequestParam Integer idade) {
        return ResponseEntity.ok().body(clienteService.buscarPorIdade(idade));
    }

    @GetMapping("telefone")
    public ResponseEntity<List<ClienteDto>> buscarPorTelefone(
            @RequestParam
            @Pattern(
                    regexp = "^\\+?\\d{2,3}?[- .]?\\(?\\d{2,3}\\)?[- .]?\\d{4,5}[- .]?\\d{4}$",
                    message = "Telefone inválido. Formato esperado: +55 (11) 91234-5678"
            )
            String telefone) {
        return ResponseEntity.ok().body(clienteService.buscarPorTelefone(telefone));
    }

    @GetMapping("cep")
    public ResponseEntity<List<ClienteDto>> buscarPorCep(
            @RequestParam
            @Pattern(
                    regexp = "^\\d{5}-\\d{3}$",
                    message = "CEP inválido. Formato esperado: 12345-678"
            )
            String cep) {


        return ResponseEntity.ok().body(clienteService.buscarPorCep(cep));
    }




}
