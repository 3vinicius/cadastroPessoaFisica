package br.com.cadastroApi.controller;

import br.com.cadastroApi.dto.ClienteAtualizarDto;
import br.com.cadastroApi.dto.ClienteCadastrarDto;
import br.com.cadastroApi.dto.ClienteDto;
import br.com.cadastroApi.exceptions.ClienteNaoEncontradoException;
import br.com.cadastroApi.model.Cliente;
import br.com.cadastroApi.repositorys.ClienteRepository;
import br.com.cadastroApi.services.ClienteService;
import br.com.cadastroApi.utils.ClienteTesteUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "token.secret=teste123"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClienteControllerTest {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ClienteController clienteController;
    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;
    private ClienteDto clienteDto = ClienteTesteUtils.gerarClienteDto();
    private ClienteAtualizarDto clienteAtualizarDto = ClienteTesteUtils.gerarClienteAtualizarDto();
    private ClienteCadastrarDto clienteCadastrarDto = ClienteTesteUtils.gerarClienteCadastrarDto();


    @Test
    @Order(1)
    void deveCadastrarCliente() {

       ClienteDto clienteCadastrado = clienteController.cadastrarCliente(this.clienteCadastrarDto).getBody();

       assertNotNull(clienteCadastrado);

    }

    @Test
    @Order(2)
    void deveAtualizarCliente() {

        this.cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        clienteAtualizarDto = new ClienteAtualizarDto(
                cliente.getId(),
                "Nome Atualizado",
                null,null,null,null,null,null);


        ClienteDto clienteAtualizado = clienteController.atualizarCliente(this.clienteAtualizarDto).getBody();

        assertTrue(clienteAtualizado.nome() == "Nome Atualizado");
    }



    @Test
    @Order(3)
    void buscarClientesComPaginacao() {

        List<ClienteDto> listaClientes = clienteController.buscarClientesComPaginacao(0,5).getBody();

        assertNotNull(listaClientes);
    }

    @Test
    @Order(4)
    void buscarCliente() {

        List<ClienteDto> listaClientes = clienteController.buscarCliente().getBody();

        assertNotNull(listaClientes);
    }


    @Test
    @Order(5)
    void buscarClientePorId() {
        this.cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        ClienteDto clienteBuscado = clienteController.buscarClientePorId(this.cliente.getId()).getBody();

        assertNotNull(clienteBuscado);
    }

    @Test
    @Order(6)
    void buscarPorNome() {
        this.cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        List<ClienteDto> listaCliente = clienteController.buscarPorNome(this.cliente.getNome()).getBody();

        assertFalse(listaCliente.isEmpty());
    }

    @Test
    @Order(7)
    void buscarPorCpf() {
        this.cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        ClienteDto clienteBuscado = clienteController.buscarPorCpf(this.cliente.getCpf()).getBody();

        assertNotNull(clienteBuscado);
    }

    @Test
    @Order(8)
    void buscarPorEmail() {
        this.cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        ClienteDto clienteBuscado = clienteController.buscarPorEmail(this.cliente.getEmail()).getBody();

        assertNotNull(clienteBuscado);
    }

    @Test
    @Order(9)
    void buscarPorEndereco() {
        this.cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        List<ClienteDto> listaCliente = clienteController.buscarPorEndereco(this.cliente.getEndereco()).getBody();

        assertFalse(listaCliente.isEmpty());

    }

    @Test
    @Order(10)
    void buscarPorIdade() {
        this.cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        List<ClienteDto> listCliente = clienteController.buscarPorIdade(this.cliente.getIdade()).getBody();

        assertFalse(listCliente.isEmpty());
    }

    @Test
    @Order(11)
    void buscarPorTelefone() {
        this.cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        List<ClienteDto> listaCliente = clienteController.buscarPorTelefone(this.cliente.getTelefone()).getBody();

        assertFalse(listaCliente.isEmpty());
    }

    @Test
    @Order(12)
    void buscarPorCep() {
        this.cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        List<ClienteDto> listaCliente = clienteController.buscarPorCep(this.cliente.getCep()).getBody();

        assertFalse(listaCliente.isEmpty());
    }

    @Test
    @Order(13)
    void deveDeletarCliente() {
        Cliente cliente = clienteRepository.findByEmailContainingIgnoreCase(clienteDto.email()).get();

        clienteController.deletarCliente(cliente.getId());


        Exception exception = assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteService.buscarPorEmail(cliente.getEmail()));


        assertTrue(exception.getMessage().contains("Cliente não encontrado com email"));


    }
}