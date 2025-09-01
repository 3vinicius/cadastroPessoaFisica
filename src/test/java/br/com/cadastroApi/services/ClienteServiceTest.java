package br.com.cadastroApi.services;

import br.com.cadastroApi.dto.ClienteAtualizarDto;
import br.com.cadastroApi.dto.ClienteCadastrarDto;
import br.com.cadastroApi.dto.ClienteDto;
import br.com.cadastroApi.exceptions.ClienteNaoEncontradoException;
import br.com.cadastroApi.mapper.ClienteMapper;
import br.com.cadastroApi.model.Cliente;
import br.com.cadastroApi.model.Usuario;
import br.com.cadastroApi.repositorys.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private  ClienteRepository clienteRepository;

    @Mock
    private  CepApiService cepApiService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    private Cliente cliente = geradorDeCliente();
    private ClienteDto clienteDto = ClienteMapper.clienteParaDto(this.cliente);
    private ClienteAtualizarDto clienteAtualizarDto =
            new ClienteAtualizarDto(1L,cliente.getNome(), clienteDto.cpf(), cliente.getDataNascimento(),cliente.getEmail()
                    ,cliente.getTelefone(),cliente.getCep(),cliente.getEndereco());
    private ClienteCadastrarDto clienteCadastrarDto = new ClienteCadastrarDto(cliente.getNome(), clienteDto.cpf(), cliente.getDataNascimento(),cliente.getEmail()
            ,cliente.getTelefone(),cliente.getCep(),cliente.getEndereco());
    private ClienteCadastrarDto clienteCadastrarDtoInvalido = new ClienteCadastrarDto(cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(),cliente.getEmail()
            ,cliente.getTelefone(),cliente.getCep(),cliente.getEndereco());

    @Test
    @Description("Deve retornar uma lista vazia quando não houver clientes cadastrados")
    void deveRetornarListaVaziaQuandoNaoHaClientesAoBuscarClientes() {

        when(clienteRepository.findAll()).thenReturn(List.of());
        assertTrue(clienteService.buscarClientes().isEmpty());

    }

    @Test
    @Description("Deve retornar uma lista com ClienteDto quando houver clientes cadastrados")
    void deveRetornarListaClientesDtoAoBuscarClientes() {

        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        ClienteDto clienteDto = clienteService.buscarClientes().get(0);

        assertEquals(clienteDto, this.clienteDto);


    }

    @Test
    @Description("Deve deletar o cliente quando o id for encontrado")
    void deveDeletarClienteQuandoIdEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(this.cliente));

        clienteService.deletarCliente(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    @Description("Deve lançar ClienteNaoEncontradoException quando o id não for encontrado")
    void deveLancarClienteNaoEncontradoExceptionQuandoIdNaoEcontradoAoDeletar() {
        Exception exception = assertThrows(ClienteNaoEncontradoException.class,
               () -> clienteService.deletarCliente(2L));

        assertTrue(exception.getMessage().contains("Cliente não encontrado com id:"));

    }

    @Test
    @Description("Deve lançar ClienteNaoEncontradoException ao atualizar um cliente que não existe")
    void deveLancarClienteNaoEncontradoExceptionQuandoClienteNaoExistirAoAtualizar() {
        Exception exception = assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteService.atualizarCliente(this.clienteAtualizarDto));

        assertTrue(exception.getMessage().contains("Cliente não encontrado"));

    }

    @Test
    @Description("Deve lançar DuplicateKeyException caso tente atualizar um cliente com cpf já existente")
    void deveLancarDuplicateKeyExceptionQuandoExistirCpfAoAtualizar() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(this.cliente));
        when(clienteRepository.save(any(Cliente.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry clientes_cpf_key"));

        Exception exception = assertThrows(DuplicateKeyException.class,
                () -> clienteService.atualizarCliente(this.clienteAtualizarDto));

        assertTrue(exception.getMessage().contains("CPF já cadastrado"));

    }

    @Test
    @Description("Deve lançar DuplicateKeyException caso tente atualizar um cliente com email já existente")
    void deveLancarDuplicateKeyExceptionQuandoExistirEmailAoAtualizar() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(this.cliente));
        when(clienteRepository.save(any(Cliente.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry clientes_email_key"));

        Exception exception = assertThrows(DuplicateKeyException.class,
                () -> clienteService.atualizarCliente(this.clienteAtualizarDto));

        assertTrue(exception.getMessage().contains("Email já cadastrado"));

    }

    @Test
    @Description("Deve atualizar campos não nulos do cliente e manter os campos não alterados")
    void deveAtualizarCamposNaoNulosEhManterCamposNaoAlteradosAoAtualizar() {
        ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);

        LocalDateTime dataAtualizacao = this.cliente.getUpdatedAt();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(this.cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(this.cliente);
        clienteService.atualizarCliente(new ClienteAtualizarDto(1L,"Nome Alterado",null,
                null,null,null,null,"Endereço Alterado"));

        verify(clienteRepository, times(1)).save(clienteCaptor.capture());


        assertTrue(clienteCaptor.getValue().getNome().equals("Nome Alterado"));
        assertTrue(clienteCaptor.getValue().getEndereco().equals("Endereço Alterado"));
        assertTrue(clienteCaptor.getValue().getCpf().equals(this.cliente.getCpf()));
        assertTrue(clienteCaptor.getValue().getDataNascimento().equals(this.cliente.getDataNascimento()));
        assertTrue(clienteCaptor.getValue().getCep().equals(this.cliente.getCep()));
        assertTrue(clienteCaptor.getValue().getCreatedAt().equals(this.cliente.getCreatedAt()));
        assertFalse(clienteCaptor.getValue().getUpdatedAt().equals(dataAtualizacao));
    }

    @Test
    @Description("Deve atualizar a ideade com base na data de nascimento do cliente ao alterar a data de nascimento")
    void deveAtualizarIdadeComBaseNaDataDeNascimentoAoAtualizar() {

        this.cliente = geradorDeCliente();

        ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);

        LocalDateTime dataAtualizacao = this.cliente.getUpdatedAt();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(this.cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(this.cliente);
        clienteService.atualizarCliente(new ClienteAtualizarDto(1L,null,null,
                LocalDate.now().minusMonths(12),null,null,null,null));

        verify(clienteRepository, times(1)).save(clienteCaptor.capture());


        assertTrue(clienteCaptor.getValue().getIdade().equals(1));
        assertFalse(clienteCaptor.getValue().getUpdatedAt().equals(dataAtualizacao));
    }

    @Test
    @Description("Deve lançar DuplicateKeyException caso tente cadastrar um cliente com email já existente")
    void deveLancarDuplicateKeyExceptionCasoTenteCadastrarUmEmailJaExistenteAoCadastrar() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(this.cliente));
        when(clienteRepository.save(any(Cliente.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry clientes_email_key"));

        Exception exception = assertThrows(DuplicateKeyException.class,
                () -> clienteService.cadastrarCliente(this.clienteCadastrarDto));

        assertTrue(exception.getMessage().contains("Email já cadastrado"));

    }

    @Test
    @Description("Deve lançar DuplicateKeyException caso tente cadastrar um cliente com cpf já existente")
    void deveLancarDuplicateKeyExceptionCasoTenteCadastrarUmCpfJaExistenteAoCadastrar() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(this.cliente));
        when(clienteRepository.save(any(Cliente.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry clientes_cpf_key"));

        Exception exception = assertThrows(DuplicateKeyException.class,
                () -> clienteService.cadastrarCliente(this.clienteCadastrarDto));

        assertTrue(exception.getMessage().contains("CPF já cadastrado"));

    }

    @Test
    @Description("Deve cadastrar um cliente com idade com base na data de nascimento e atualizar as datas de criação e atualização")
    void deveAtualizarIdadeDataNascimentoEhDataUpdatedAoCadastrar() {

        ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(this.cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(this.cliente);

        clienteService.cadastrarCliente(this.clienteCadastrarDto);

        verify(clienteRepository, times(1)).save(clienteCaptor.capture());

        assertTrue(clienteCaptor.getValue().getIdade().equals(0));

    }

    @Test
    @Description("Deve retornar ClienteDto quando o id for encontrado")
    void deveRetornarClienteDtoCasoIdEncontradoAoBuscarPorId() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(this.cliente));

        try (var mocker = mockStatic(ClienteMapper.class)) {
            mocker.when(() -> ClienteMapper.clienteParaDto(any(Cliente.class))).thenReturn(this.clienteDto);

            ClienteDto result = clienteService.buscarClientePorId(1L);

            assertEquals(this.clienteDto, result);
            verify(clienteRepository, times(1)).findById(1L);

        }

    }

    @Test
    @Description("Deve lançar ClienteNaoEncontradoException quando o id não for encontrado")
    void deveLancarClienteNaoEncontradoExceptionQuandoIdNaoEncontradoAoBuscarPorId() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());


        Exception exception = assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteService.buscarClientePorId(1L));

        assertTrue(exception.getMessage().contains("Cliente não encontrado com id:"));

    }

    @Test
    @Description("Deve retornar uma lista vazia quando não houver clientes")
    void deveRetornarListaVaziaQuandoNaoHouverClientesAoBuscarPorNome() {
        when(clienteRepository.findByNomeContainingIgnoreCase("teste")).thenReturn(Optional.empty());
        assertTrue(clienteService.buscarPorNome("teste").isEmpty());
    }

    @Test
    @Description("Deve retornar uma lista de clientesDto quando houver clientes")
    void deveRetornarListaClientesDtoAoBuscarPorNome() {
        when(clienteRepository.findByNomeContainingIgnoreCase("teste")).thenReturn(Optional.of(List.of(this.cliente)));
        assertTrue(clienteService.buscarPorNome("teste").contains(this.clienteDto));
    }

    @Test
    @Description("Deve retornar ClienteDto quando o cpf for encontrado")
    void buscarRetornarClienteDtoAoBuscarPorCpf() {
        when(clienteRepository.findByCpfContains(this.cliente.getCpf())).thenReturn(Optional.of(this.cliente));

        ClienteDto clienteDto = clienteService.buscarPorCpf(this.cliente.getCpf());

        assertEquals(this.clienteDto, clienteDto);
    }

    @Test
    @Description("Deve lançar ClienteNaoEncontradoException quando o cpf for encontrado")
    void DeveLancarClienteNaoEncontradoExceptionQuandoCpfNaoEncontradoAoBuscarPorCpf() {
        when(clienteRepository.findByCpfContains(this.cliente.getCpf())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteService.buscarPorCpf("00000000000"));

        assertTrue(exception.getMessage().contains("Cliente não encontrado com cpf"));
    }

    @Test
    @Description("Deve retornar ClienteDto quando o email for encontrado")
    void deveRetornarClienteDtoQuandoEmailEncontradoAoBuscarPorEmail() {
        when(clienteRepository.findByEmailContainingIgnoreCase(this.cliente.getEmail())).thenReturn(Optional.of(this.cliente));

        ClienteDto clienteDto = clienteService.buscarPorEmail(this.cliente.getEmail());

        assertEquals(this.clienteDto, clienteDto);

    }

    @Test
    @Description("Deve lançar ClienteNaoEncontradoException quando o email não for encontrado")
    void deveLancarClienteNaoEncontradoExceptionDtoQuandoEmailEncontradoAoBuscarPorEmail() {
        when(clienteRepository.findByEmailContainingIgnoreCase(this.cliente.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteService.buscarPorEmail("00000000000"));

        assertTrue(exception.getMessage().contains("Cliente não encontrado com email"));

    }

    @Test
    @Description("Deve retornar ClienteDto quando houver clientes")
    void deveRetornarListaClienteDtoAoBuscarPorEndereco() {
        when(clienteRepository.findByEnderecoContainingIgnoreCase(this.cliente.getEndereco())).thenReturn(Optional.of(List.of(this.cliente)));

       List<ClienteDto> listaClienteDto = clienteService.buscarPorEndereco(this.cliente.getEndereco());

        assertTrue(listaClienteDto.contains(this.clienteDto));
    }

    @Test
    @Description("Deve retornar uma lista vazia quando não houver clientes")
    void deveRetornarListaVaziaCasoNaoEncontreClientesAoBuscarPorEndereco() {
        when(clienteRepository.findByEnderecoContainingIgnoreCase(this.cliente.getEndereco())).thenReturn(Optional.empty());

        List<ClienteDto> listaClienteDto = clienteService.buscarPorEndereco(this.cliente.getEndereco());

        assertTrue(listaClienteDto.isEmpty());
    }

    @Test
    @Description("Deve retornar ClienteDto quando houver clientes")
    void deveRetornarClienteDtoAoBuscarPorIdade() {
        when(clienteRepository.findByIdade(this.cliente.getIdade())).thenReturn(Optional.of(List.of(this.cliente)));

        List<ClienteDto> listaClienteDto = clienteService.buscarPorIdade(this.cliente.getIdade());

        assertTrue(listaClienteDto.contains(this.clienteDto));
    }

    @Test
    @Description("Deve retornar uma lista vazia quando não houver clientes")
    void deveRetornarListaVaziaCasoNaoEncontreClientesAoBuscarPorIdade() {
        when(clienteRepository.findByIdade(this.cliente.getIdade())).thenReturn(Optional.empty());

        List<ClienteDto> listaClienteDto = clienteService.buscarPorIdade(this.cliente.getIdade());

        assertTrue(listaClienteDto.isEmpty());
    }

    @Test
    @Description("Deve retornar ClienteDto quando houver clientes na paginação")
    void deveRetornarListaClienteDtoAoBuscarPorPaginacao() {
        when(clienteRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(this.cliente)));

        List<ClienteDto> listaClienteDto = clienteService.buscarClientesComPaginacao(0, 10);

        assertTrue(listaClienteDto.contains(this.clienteDto));
    }

    @Test
    @Description("Deve lançar ClienteNaoEncontradoException quando não houver clientes na paginação")
    void deveLancarClienteNaoEncontradoExceptionCasoNaoEncontreClientesAoBuscarPorPaginacao() {
        when(clienteRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenThrow(new RuntimeException());

        Exception exception = assertThrows(ClienteNaoEncontradoException.class,
                () -> clienteService.buscarClientesComPaginacao(0, 10));
        assertTrue(exception.getMessage().contains("Cliente não encontrado"));


    }

    @Test
    @Description("Deve retornar ClienteDto quando houver clientes")
    void deveRetornarListaClienteDtoAoBuscarPorTelefone() {
        when(clienteRepository.findByTelefoneContainingIgnoreCase(this.cliente.getTelefone())).thenReturn(Optional.of(List.of(this.cliente)));

        List<ClienteDto> listaClienteDto = clienteService.buscarPorTelefone(this.cliente.getTelefone());

        assertTrue(listaClienteDto.contains(this.clienteDto));
    }

    @Test
    @Description("Deve retornar uma lista vazia quando não houver clientes")
    void deveRetornarListaVaziaCasoNaoEncontreClientesAoBuscarPorTelefone() {
        when(clienteRepository.findByTelefoneContainingIgnoreCase(this.cliente.getTelefone())).thenReturn(Optional.empty());

        List<ClienteDto> listaClienteDto = clienteService.buscarPorTelefone(this.cliente.getTelefone());

        assertTrue(listaClienteDto.isEmpty());
    }

    @Test
    @Description("Deve retornar ClienteDto quando houver clientes")
    void deveRetornarListaClienteDtoCasoNaoEncontreClientesAoBuscarPorCe() {
        when(clienteRepository.findByCepContainingIgnoreCase(this.cliente.getCep())).thenReturn(Optional.of(List.of(this.cliente)));

        List<ClienteDto> listaClienteDto = clienteService.buscarPorCep(this.cliente.getCep());

        assertTrue(listaClienteDto.contains(this.clienteDto));
    }

    @Test
    @Description("Deve retornar uma lista vazia quando não houver clientes")
    void deveRetornarListaVaziaCasoNaoEncontreClientesAoBuscarPorCep() {
        when(clienteRepository.findByCepContainingIgnoreCase(this.cliente.getCep())).thenReturn(Optional.empty());

        List<ClienteDto> listaClienteDto = clienteService.buscarPorCep(this.cliente.getCep());

        assertTrue(listaClienteDto.isEmpty());
    }

    private Cliente geradorDeCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(Long.valueOf(1));
        cliente.setNome("teste");
        cliente.setCpf("123456789-17");
        cliente.setDataNascimento(LocalDate.now());
        cliente.setEmail("teste@gmail.com");
        cliente.setTelefone("(92) 9 8954-1541");
        cliente.setEndereco("rua teste");
        cliente.setCep("68753-00");
        cliente.setIdade(12);
        cliente.setCreatedAt(LocalDateTime.now().minusMonths(5));
        cliente.setUpdatedAt(LocalDateTime.now().minusMonths(2));
        return cliente;
    }

}