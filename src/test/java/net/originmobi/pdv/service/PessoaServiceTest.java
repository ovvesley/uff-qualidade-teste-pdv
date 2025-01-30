package net.originmobi.pdv.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.originmobi.pdv.model.Cidade;
import net.originmobi.pdv.model.Endereco;
import net.originmobi.pdv.model.Pessoa;
import net.originmobi.pdv.model.Telefone;
import net.originmobi.pdv.repository.PessoaRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private CidadeService cidadeService;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private TelefoneService telefoneService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoa;
    private Cidade cidade;
    private Endereco endereco;
    private Telefone telefone;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        pessoa = new Pessoa();
        pessoa.setCodigo(1L);
        pessoa.setNome("João Silva");
        pessoa.setCpfcnpj("12345678901");
        pessoa.setData_nascimento(Date.valueOf("1990-01-01"));

        cidade = new Cidade();
        cidade.setCodigo(1L);

        endereco = new Endereco();
        endereco.setCodigo(1L);
        endereco.setCidade(cidade);
        endereco.setRua("Rua A");
        endereco.setBairro("Centro");
        endereco.setNumero("123");
        endereco.setCep("12345-678");
        endereco.setReferencia("Perto do mercado");

        telefone = new Telefone();
        telefone.setCodigo(1L);
        telefone.setFone("21999999999");
    }

    @Test
    public void lista_RetornaListaDePessoas() {
        List<Pessoa> pessoas = new ArrayList<>();
        pessoas.add(pessoa);

        when(pessoaRepository.findAll()).thenReturn(pessoas);

        List<Pessoa> resultado = pessoaService.lista();
        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());

        verify(pessoaRepository).findAll();
    }

    @Test
    public void buscaPessoaPorCodigo_RetornaPessoa() {
        when(pessoaRepository.findByCodigoIn(1L)).thenReturn(pessoa);

        Pessoa resultado = pessoaService.busca(1L);
        assertEquals("João Silva", resultado.getNome());

        verify(pessoaRepository).findByCodigoIn(1L);
    }

    @Test
    public void buscaPessoaOptionalPorCodigo_RetornaOptionalPessoa() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        Optional<Pessoa> resultado = pessoaService.buscaPessoa(1L);
        assertEquals(true, resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());

        verify(pessoaRepository).findById(1L);
    }

    @Test
    public void cadastrarPessoa_ComSucesso() throws ParseException {
        when(cidadeService.busca(1L)).thenReturn(Optional.of(cidade));
        when(enderecoService.cadastrar(any(Endereco.class))).thenReturn(endereco);
        when(telefoneService.cadastrar(any(Telefone.class))).thenReturn(telefone);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        String resultado = pessoaService.cadastrar(
                0L, "João Silva", "João", "12345678901", "01/01/1990", "Cliente VIP",
                0L, 1L, "Rua A", "Centro", "123", "12345-678", "Perto do mercado",
                0L, "21999999999", "CELULAR", redirectAttributes
        );

        assertEquals("Pessoa salva com sucesso", resultado);

        verify(pessoaRepository).save(any(Pessoa.class));
    }

    @Test
    public void cadastrarPessoa_JaExistente_DeveLancarErro() throws ParseException {
        when(pessoaRepository.findByCpfcnpjContaining("12345678901")).thenReturn(pessoa);

        try {
            pessoaService.cadastrar(
                    0L, "João Silva", "João", "12345678901", "01/01/1990", "Cliente VIP",
                    0L, 1L, "Rua A", "Centro", "123", "12345-678", "Perto do mercado",
                    0L, "21999999999", "CELULAR", redirectAttributes
            );
        } catch (RuntimeException e) {
            assertEquals("Já existe uma pessoa cadastrada com este CPF/CNPJ, verifique", e.getMessage());
        }

        verify(pessoaRepository).findByCpfcnpjContaining("12345678901");
        verifyNoMoreInteractions(pessoaRepository, enderecoService, telefoneService);
    }

    @Test
    public void cadastrarPessoa_CidadeNaoEncontrada_DeveLancarErro() throws ParseException {
        when(cidadeService.busca(1L)).thenReturn(Optional.empty());

        try {
            pessoaService.cadastrar(
                    0L, "João Silva", "João", "12345678901", "01/01/1990", "Cliente VIP",
                    0L, 1L, "Rua A", "Centro", "123", "12345-678", "Perto do mercado",
                    0L, "21999999999", "CELULAR", redirectAttributes
            );
        } catch (RuntimeException e) {
            assertEquals("Erro ao tentar cadastrar pessoa, chame o suporte", e.getMessage());
        }

        verify(cidadeService).busca(1L);
        verifyNoMoreInteractions(enderecoService, telefoneService, pessoaRepository);
    }
}
