package br.com.cadastroApi.services;

import br.com.cadastroApi.dto.ClienteAtualizarDto;
import br.com.cadastroApi.dto.ClienteCadastrarDto;
import br.com.cadastroApi.dto.ClienteDto;
import br.com.cadastroApi.model.Cliente;
import br.com.cadastroApi.repositorys.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteDto> buscarClientes(){
        try{
            return clienteRepository.findAll().stream().map(ClienteDto::new).toList();
        } catch(Exception e){
            throw new RuntimeException("Erro ao buscar clientes \n" + e.getMessage());
        }
    }

    public String deletarCliente(Long idCliente){
        try{
            clienteRepository.deleteById(idCliente);
            return "Cliente deletado com sucesso";
        }catch (Exception e){
            throw new RuntimeException("Erro ao deletar cliente \n" + e.getMessage());
        }

    }

    public ClienteDto atualizarCliente(ClienteAtualizarDto clienteAtualizarDto){
        try{
            Optional<Cliente> pessoaFisica = clienteRepository.findById(clienteAtualizarDto.id());
            if (pessoaFisica.isPresent()){
                Cliente clienteAtualizado = pessoaFisica.get();
                clienteAtualizado.setNome(clienteAtualizarDto.nome());
                clienteAtualizado.setCpf(clienteAtualizarDto.cpf());
                clienteAtualizado.setDataNascimento(clienteAtualizarDto.dataNascimento());
                clienteAtualizado.setEmail(clienteAtualizarDto.email());
                clienteAtualizado.setTelefone(clienteAtualizarDto.telefone());
                clienteAtualizado.setEndereco(clienteAtualizarDto.endereco());
                clienteAtualizado.setUpdatedAt(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));



                return new ClienteDto(clienteRepository.save(clienteAtualizado));
            }
        }catch (Exception e){
            throw new RuntimeException("Erro ao atualizar cliente \n" + e.getMessage());
        }
        return null;
    }

    public ClienteDto cadastrarCliente(ClienteCadastrarDto clienteCadastrarDto){
        try{
            Cliente cliente = new Cliente();
            cliente.setNome(clienteCadastrarDto.nome());
            cliente.setCpf(clienteCadastrarDto.cpf());
            cliente.setDataNascimento(clienteCadastrarDto.dataNascimento());
            cliente.setEmail(clienteCadastrarDto.email());
            cliente.setTelefone(clienteCadastrarDto.telefone());
            cliente.setEndereco(clienteCadastrarDto.endereco());
            cliente.setUpdatedAt(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            cliente.setCreatedAt(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            cliente.setIdade(geradorDeIdade(clienteCadastrarDto.dataNascimento()));


            return new ClienteDto(clienteRepository.save(cliente));
        }catch (Exception e){
            throw new RuntimeException("Erro ao cadastrar pessoa cliente \n" + e.getMessage());
        }
    }

    public ClienteDto buscarClientePorId(Long id) {
        try {
            return clienteRepository.findById(id).stream().map(ClienteDto::new).toList().getFirst();
        } catch (Exception e){
            throw new RuntimeException("Cliente não encontrado");
        }

    }

    public List<ClienteDto> buscarPorNome(String nomeCliente) {
        try{
            return clienteRepository.findByNomeContainingIgnoreCase(nomeCliente).stream().map(ClienteDto::new).toList();
        } catch (Exception e){
            throw new RuntimeException("Erro ao buscar cliente por nome \n" + e.getMessage());
        }

    }

    public ClienteDto buscarPorCpf(String cpf) {

        try {
            return clienteRepository.findByCpfContains(cpf).stream().map(ClienteDto::new).toList().getFirst();
        } catch (Exception e){
            throw new RuntimeException("Erro ao buscar cliente por CPF \n" + e.getMessage());
        }

    }

    public ClienteDto buscarPorEmail(String email) {

        try {
            return clienteRepository.findByEmailContainingIgnoreCase(email).stream().map(ClienteDto::new).toList().getFirst();
        } catch (Exception e){
            throw new RuntimeException("Erro ao buscar cliente por email \n" + e.getMessage());
        }
    }

    public List<ClienteDto> buscarPorEndereco(String endereco) {
        try {
            return clienteRepository.findByEnderecoContainingIgnoreCase(endereco).orElse(Collections.emptyList())
                    .stream().map(ClienteDto::new).toList();
        } catch (Exception e){
            throw new RuntimeException("Erro ao buscar cliente por endereço \n" + e.getMessage());
        }

   }

    public List<ClienteDto> buscarPorIdade(Integer idade) {
        try{
            return clienteRepository.findByIdade(idade).orElse(Collections.emptyList())
                    .stream().map(ClienteDto::new).toList();
        } catch (Exception e){
            throw new RuntimeException("Erro ao buscar clientes por idade \n" + e.getMessage());
        }
    }

    public List<ClienteDto> buscarClientesComPaginacao(Integer page, Integer size){
        try{
            var pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
            var pageResult = clienteRepository.findAll(pageable);
            return pageResult.getContent().stream().map(ClienteDto::new).toList();
        } catch(Exception e){
            throw new RuntimeException("Erro ao buscar clientes com paginação \n" + e.getMessage());
        }
    }

    private Integer geradorDeIdade(LocalDate dataNascimento) {
        LocalDate dataAtual = LocalDate.now();
        return  Period.between(dataNascimento, dataAtual).getYears();
    }

}