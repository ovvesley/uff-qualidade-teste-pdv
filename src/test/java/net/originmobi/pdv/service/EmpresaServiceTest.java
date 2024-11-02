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

    private Empresa empresa;

    // @Before
    // public void setUp() {
        
    // }

    @Test
    public void testVerificaEmpresaCadastradaNaoExiste(){
        Optional<Empresa> lista_empresas = EmpresaService.verificaEmpresaCadastrada();
        assertTrue(!lista_empresas.isPresent());
    }

    // @Test
    // public void testVerificaEmpresaExiste(){
    //     private EmpresaRepository empresa
    //     empresa.set

    //     Optional<Empresa> lista_empresas = EmpresaService.verificaEmpresaCadastrada();
    //     assertTrue(lista_empresas.isPresent());
    // }
   

}
