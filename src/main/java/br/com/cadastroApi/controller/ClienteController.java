package br.com.cadastroApi.controller;


import br.com.cadastroApi.dto.ClienteAtualizarDto;
import br.com.cadastroApi.dto.ClienteCadastrarDto;
import br.com.cadastroApi.dto.ClienteDto;
import br.com.cadastroApi.model.Cliente;
import br.com.cadastroApi.services.ClienteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("cliente")
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
    public ResponseEntity<String> deletarCliente(@RequestParam Long id) {
        return ResponseEntity.ok().body(clienteService.deletarCliente(id));
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
    public ResponseEntity<ClienteDto> buscarPorCpf(@RequestParam String cpf) {
        return ResponseEntity.ok().body(clienteService.buscarPorCpf(cpf));
    }

    @GetMapping("email")
    public ResponseEntity<ClienteDto> buscarPorEmail(@RequestParam String email) {
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


}
