package net.originmobi.pdv.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.originmobi.pdv.enumerado.ajuste.AjusteStatus;
import net.originmobi.pdv.model.Ajuste;
import net.originmobi.pdv.model.AjusteProduto;
import net.originmobi.pdv.model.Produto;
import net.originmobi.pdv.repository.AjusteProdutoRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AjusteProdutoServiceTest {

    @Mock
    private AjusteProdutoRepository ajusteProdutoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private AjusteService ajusteService;

    @InjectMocks
    private AjusteProdutoService ajusteProdutoService;

    private Ajuste ajusteNaoProcessado;
    private Ajuste ajusteProcessado;
    private Produto produto;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ajusteNaoProcessado = new Ajuste();
        ajusteNaoProcessado.setCodigo(1L);
        ajusteNaoProcessado.setStatus(AjusteStatus.APROCESSAR);

        ajusteProcessado = new Ajuste();
        ajusteProcessado.setCodigo(2L);
        ajusteProcessado.setStatus(AjusteStatus.PROCESSADO);

        produto = new Produto();
        produto.setCodigo(1L);
    }

    @Test
    public void listaProdutosAjuste_RetornaListaProdutos() {
        List<AjusteProduto> produtosAjuste = new ArrayList<>();
        when(ajusteProdutoRepository.findByAjusteCodigoEquals(1L)).thenReturn(produtosAjuste);

        List<AjusteProduto> resultado = ajusteProdutoService.listaProdutosAjuste(1L);

        assertEquals(produtosAjuste, resultado);
        verify(ajusteProdutoRepository).findByAjusteCodigoEquals(1L);
    }

   

    @Test
    public void removeProduto_RemocaoBemSucedida_RetornaSucesso() {
        when(ajusteService.busca(1L)).thenReturn(Optional.of(ajusteNaoProcessado));
        doNothing().when(ajusteProdutoRepository).removeProduto(1L, 1L);

        String resultado = ajusteProdutoService.removeProduto(1L, 1L);

        assertEquals("Produto removido com sucesso", resultado);
        verify(ajusteProdutoRepository).removeProduto(1L, 1L);
    }

    @Test
    public void buscaProdAjust_RetornaQuantidade() {
        when(ajusteProdutoRepository.buscaProdAjuste(1L, 1L)).thenReturn(3);

        int resultado = ajusteProdutoService.buscaProdAjust(1L, 1L);

        assertEquals(3, resultado);
        verify(ajusteProdutoRepository).buscaProdAjuste(1L, 1L);
    }


    
}