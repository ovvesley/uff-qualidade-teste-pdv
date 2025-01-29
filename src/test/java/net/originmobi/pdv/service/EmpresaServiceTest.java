package net.originmobi.pdv.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import net.originmobi.pdv.model.Cidade;
import net.originmobi.pdv.model.EmpresaParametro;
import net.originmobi.pdv.model.RegimeTributario;
import net.originmobi.pdv.repository.EmpresaParametrosRepository;
import net.originmobi.pdv.repository.EmpresaRepository;
import net.originmobi.pdv.service.EnderecoService;
import net.originmobi.pdv.service.RegimeTributarioService;
import net.originmobi.pdv.service.CidadeService;
import net.originmobi.pdv.model.Empresa;
import net.originmobi.pdv.model.Endereco;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmpresaServiceTest {

    @InjectMocks
    private EmpresaService empresaService;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private EmpresaParametrosRepository parametrosRepository;

    @Mock
    private RegimeTributarioService regimesService;

    @Mock
    private CidadeService cidadesService;

    @Mock
    private EnderecoService enderecosService;

    private Empresa empresa;

    @Before
    public void setUp() {
        empresa = new Empresa();
        empresa.setNome("bagulhos e cia.");
        empresa.setNome_fantasia("Vendinha");
        empresa.setCnpj("999999999");

        Endereco endereco = new Endereco();
        empresa.setEndereco(endereco);
    }

    @Test
    public void testVerificaEmpresaCadastradaNaoExiste() {
        when(empresaRepository.buscaEmpresaCadastrada()).thenReturn(Optional.empty());

        Optional<Empresa> lista_empresas = empresaService.verificaEmpresaCadastrada();
        assertFalse(lista_empresas.isPresent());
    }

    @Test
    public void testVerificaEmpresaExiste() {
        when(empresaRepository.buscaEmpresaCadastrada()).thenReturn(Optional.of(empresa));

        Optional<Empresa> lista_empresas = empresaService.verificaEmpresaCadastrada();
        assertTrue(lista_empresas.isPresent());
        assertEquals("bagulhos e cia.", lista_empresas.get().getNome());
    }

    @Test
    public void testBuscarEmpresaComNomeCorreto() {
        when(empresaRepository.buscaEmpresaCadastrada()).thenReturn(Optional.of(empresa));

        Optional<Empresa> lista_empresas = empresaService.verificaEmpresaCadastrada();
        assertTrue(lista_empresas.isPresent());
        assertEquals("Vendinha", lista_empresas.get().getNome_fantasia());
    }

    @Test
    public void testBuscarEmpresaComCNPJCorreto() {
        when(empresaRepository.buscaEmpresaCadastrada()).thenReturn(Optional.of(empresa));

        Optional<Empresa> lista_empresas = empresaService.verificaEmpresaCadastrada();
        assertTrue(lista_empresas.isPresent());
        assertEquals("999999999", lista_empresas.get().getCnpj());
    }

    @Test
    public void testMergerComEmpresaExistente() {
        Long codigo = 1L;
        String nome = "bagulhos e cia.";
        String nomeFantasia = "Vendinha";
        String cnpj = "999999999";
        String ie = "123456789";
        int serie = 1;
        int ambiente = 1;
        Long codRegime = 1L;
        Long codEndereco = 1L;
        Long codCidade = 1L;
        String rua = "Rua Exemplo";
        String bairro = "Bairro Exemplo";
        String numero = "123";
        String cep = "12345-678";
        String referencia = "Perto da escola";
        Double aliqCalcCredito = 5.0;

        String result = empresaService.merger(codigo, nome, nomeFantasia, cnpj, ie, serie, ambiente, codRegime,
                codEndereco, codCidade, rua, bairro, numero, cep, referencia, aliqCalcCredito);

        assertEquals("Empresa salva com sucesso", result);
    }

    @Test
    public void testCadastroComSucesso() {
        Empresa novaEmpresa = new Empresa();
        empresaService.cadastro(novaEmpresa);
        verify(empresaRepository).save(novaEmpresa);
    }

    @Test
    public void testCadastroComExcecao() {
        Empresa empresaInvalida = new Empresa();
        doThrow(new RuntimeException("Erro de banco")).when(empresaRepository).save(empresaInvalida);

        empresaService.cadastro(empresaInvalida);
        verify(empresaRepository).save(empresaInvalida);
    }

    @Test
    public void testMergerNovaEmpresaComSucesso() {
        when(regimesService.busca(anyLong())).thenReturn(Optional.of(new RegimeTributario()));
        when(cidadesService.busca(anyLong())).thenReturn(Optional.of(new Cidade()));
        when(enderecosService.cadastrar(any(Endereco.class))).thenReturn(new Endereco());

        String resultado = empresaService.merger(
                null, "Nova Empresa", "Fantasia", "123", "456",
                1, 1, 1L, null, 1L, "Rua", "Bairro", "123",
                "12345", "Ref", 2.0);

        assertEquals("Empresa salva com sucesso", resultado);
        verify(parametrosRepository).save(any(EmpresaParametro.class));
        verify(empresaRepository).save(any(Empresa.class));
    }

    @Test
    public void testMergerAtualizaEmpresaComErroAoSalvarEmpresa() {
        doThrow(new RuntimeException()).when(empresaRepository)
                .update(anyLong(), anyString(), anyString(), anyString(), anyString(), anyLong());

        String resultado = empresaService.merger(
                1L, "Nome", "Fantasia", "123", "456",
                1, 1, 1L, 1L, 1L, "Rua", "Bairro", "123",
                "12345", "Ref", 2.0);

        assertTrue(resultado.contains("Erro ao salvar dados da empresa"));
    }

    @Test
    public void testMergerAtualizaEmpresaComErroAoSalvarParametros() {
        doThrow(new RuntimeException()).when(parametrosRepository).update(anyInt(), anyInt(), anyDouble());

        String resultado = empresaService.merger(
                1L, "Nome", "Fantasia", "123", "456",
                1, 1, 1L, 1L, 1L, "Rua", "Bairro", "123",
                "12345", "Ref", 2.0);

        assertTrue(resultado.contains("Erro ao salvar dados da empresa"));
    }

    @Test
    public void testMergerAtualizaEmpresaComErroAoSalvarEndereco() {
        doThrow(new RuntimeException()).when(enderecosService)
                .update(anyLong(), anyLong(), anyString(), anyString(), anyString(), anyString(), anyString());

        String resultado = empresaService.merger(
                1L, "Nome", "Fantasia", "123", "456",
                1, 1, 1L, 1L, 1L, "Rua", "Bairro", "123",
                "12345", "Ref", 2.0);

        assertTrue(resultado.contains("Erro ao salvar dados da empresa"));
    }

    @Test(expected = RuntimeException.class)
    public void testMergerNovaEmpresaComRegimeTributarioNaoEncontrado() {
        when(regimesService.busca(anyLong())).thenReturn(Optional.empty());

        empresaService.merger(
                null, "Nome", "Fantasia", "123", "456",
                1, 1, 1L, null, 1L, "Rua", "Bairro", "123",
                "12345", "Ref", 2.0);
    }

    @Test(expected = RuntimeException.class)
    public void testMergerNovaEmpresaComCidadeNaoEncontrada() {
        when(regimesService.busca(anyLong())).thenReturn(Optional.of(new RegimeTributario()));
        when(cidadesService.busca(anyLong())).thenReturn(Optional.empty());

        empresaService.merger(
                null, "Nome", "Fantasia", "123", "456",
                1, 1, 1L, null, 1L, "Rua", "Bairro", "123",
                "12345", "Ref", 2.0);
    }

    

}
