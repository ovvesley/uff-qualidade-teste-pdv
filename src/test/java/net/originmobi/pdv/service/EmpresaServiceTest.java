package net.originmobi.pdv.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.originmobi.pdv.model.Cidade;
import net.originmobi.pdv.model.Empresa;
import net.originmobi.pdv.model.EmpresaParametro;
import net.originmobi.pdv.model.Endereco;
import net.originmobi.pdv.model.RegimeTributario;
import net.originmobi.pdv.repository.EmpresaParametrosRepository;
import net.originmobi.pdv.repository.EmpresaRepository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmpresaServiceTest {

    @InjectMocks
    private EmpresaService EmpresaService;

    @Mock
    private EmpresaRepository empresaRepository;

    private EmpresaRepository empresas;

    // @Before
    // public void setUp() {
        
    // }

    @Test
    public void testVerificaEmpresaCadastradaNaoExiste(){
        Optional<Empresa> lista_empresas = EmpresaService.verificaEmpresaCadastrada();
        assertTrue(!lista_empresas.isPresent());
    }

    @Test
    public void testVerificaEmpresaExiste(){
        Empresa empresa = new Empresa();
        empresa.setNome("bagulhos e cia."); 
        empresa.setNome_fantasia("Vendinha"); 
        empresa.setCnpj("999999999"); 
        
        Endereco endereco = new Endereco(); 
        // endereco.setBairro("Limoeiro");
        // endereco.setCidade("Niteroi");
        
        empresa.setEndereco(endereco);

        // Configurando o mock para retornar a empresa quando buscar
        when(empresaRepository.buscaEmpresaCadastrada()).thenReturn(Optional.of(empresa));

        empresas.save(empresa);

        Optional<Empresa> lista_empresas = EmpresaService.verificaEmpresaCadastrada();
        assertTrue(lista_empresas.isPresent());
    }
   

}
