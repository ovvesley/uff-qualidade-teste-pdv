package net.originmobi.pdv.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import net.originmobi.pdv.model.Empresa;
import net.originmobi.pdv.model.Endereco;
import net.originmobi.pdv.repository.EmpresaRepository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmpresaServiceTest {

    @InjectMocks
    private EmpresaService empresaService;

    @Mock
    private EmpresaRepository empresaRepository;

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

}
