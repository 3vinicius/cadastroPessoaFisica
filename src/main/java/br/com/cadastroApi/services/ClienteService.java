package br.com.cadastroApi.services;

import br.com.cadastroApi.dto.ClienteAtualizarDto;
import br.com.cadastroApi.dto.ClienteCadastrarDto;
import br.com.cadastroApi.dto.ClienteDto;
import br.com.cadastroApi.exceptions.ClienteNaoEncontradoException;
import br.com.cadastroApi.exceptions.DataInvalidaException;
import br.com.cadastroApi.mapper.ClienteMapper;
import br.com.cadastroApi.model.Cliente;
import br.com.cadastroApi.repositorys.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CepApiService cepApiService;

    public List<ClienteDto> buscarClientes(){
        return clienteRepository.findAll().stream().map(ClienteDto::new).toList();
    }

    public void deletarCliente(Long id){
        clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado com id: " + id));
        clienteRepository.deleteById(id);
    }

    public ClienteDto atualizarCliente(ClienteAtualizarDto clienteAtualizarDto){
        try{
            Cliente cliente = clienteRepository.findById(clienteAtualizarDto.id()).orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));

            if (clienteAtualizarDto.dataNascimento() != null && !clienteAtualizarDto.dataNascimento().equals(cliente.getDataNascimento())) {
                cliente.setIdade(obterIdadePorNascimento(clienteAtualizarDto.dataNascimento()));
            }

            ClienteMapper.dtoParaCliente(clienteAtualizarDto, cliente);


            cepApiService.validarCep(cliente.getCep());

            cliente.setUpdatedAt(LocalDateTime.now());
            return new ClienteDto(clienteRepository.save(cliente));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException(extrairMensagemDuplicada(e));
        }
    }

    public ClienteDto cadastrarCliente(ClienteCadastrarDto clienteCadastrarDto){
        try{

            Cliente cliente = new Cliente();
            ClienteMapper.dtoParaCliente(clienteCadastrarDto,cliente);

            cepApiService.validarCep(cliente.getCep());

            cliente.setUpdatedAt(LocalDateTime.now());
            cliente.setCreatedAt(LocalDateTime.now());
            cliente.setIdade(obterIdadePorNascimento(clienteCadastrarDto.dataNascimento()));

            return new ClienteDto(clienteRepository.save(cliente));
        }catch (DataIntegrityViolationException e){
            throw new DuplicateKeyException(extrairMensagemDuplicada(e));
        }
    }

    public ClienteDto buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .stream().map(ClienteDto::new).toList().getFirst();
    }

    public List<ClienteDto> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome)
                .stream().map(ClienteDto::new).toList();
    }

    public ClienteDto buscarPorCpf(String cpf) {
        return clienteRepository.findByCpfContains(cpf)
                .stream().map(ClienteDto::new).toList().getFirst();
    }

    public ClienteDto buscarPorEmail(String email) {
        return clienteRepository.findByEmailContainingIgnoreCase(email)
                .stream().map(ClienteDto::new).toList().getFirst();
    }

    public List<ClienteDto> buscarPorEndereco(String endereco) {
        return clienteRepository.findByEnderecoContainingIgnoreCase(endereco).orElse(Collections.emptyList())
                .stream().map(ClienteDto::new).toList();
   }

    public List<ClienteDto> buscarPorIdade(Integer idade) {
        return clienteRepository.findByIdade(idade).orElse(Collections.emptyList())
                .stream().map(ClienteDto::new).toList();
    }

    public List<ClienteDto> buscarClientesComPaginacao(Integer page, Integer size){
        try{
            var pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
            var pageResult = clienteRepository.findAll(pageable);
            return pageResult.getContent().stream().map(ClienteDto::new).toList();
        } catch(Exception e){
            throw new RuntimeException("Erro ao buscar clientes com paginação " + e.getMessage());
        }
    }


    public List<ClienteDto> buscarPorTelefone(String telefone){

        return clienteRepository.findByTelefoneContainingIgnoreCase(telefone).orElse(Collections.emptyList())
                .stream().map(ClienteDto::new).toList();

    }

    public List<ClienteDto> buscarPorCep(String cep){
        cepApiService.validarCep(cep);
        return clienteRepository.findByCepContainingIgnoreCase(cep).orElse(Collections.emptyList())
                .stream().map(ClienteDto::new).toList();
    }


    private Integer obterIdadePorNascimento(LocalDate dataNascimento) {
        LocalDate dataAtual = LocalDate.now();

        Period diferencaDatas = Period.between(dataNascimento, dataAtual);

        if (diferencaDatas.getYears() >= 0 && diferencaDatas.getMonths() >= 0 && diferencaDatas.getDays() >= 0 ) {
            return  diferencaDatas.getYears();
        } else {
            throw new DataInvalidaException("Data de nascimento inválida. A data de nascimento não pode ser no futuro.");
        }
    }

    private String extrairMensagemDuplicada(DataIntegrityViolationException e) {
        String msg = e.getMostSpecificCause().getMessage();
        if (msg.contains("clientes_cpf_key")) return "CPF já cadastrado";
        if (msg.contains("clientes_email_key")) return "Email já cadastrado";
        return "Violação de chave única";
    }




}