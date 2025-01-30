package net.originmobi.pdv.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.support.SessionStatus;

import net.originmobi.pdv.enumerado.Ativo;
import net.originmobi.pdv.enumerado.produto.ProdutoBalanca;
import net.originmobi.pdv.enumerado.produto.ProdutoControleEstoque;
import net.originmobi.pdv.enumerado.produto.ProdutoSubstTributaria;
import net.originmobi.pdv.enumerado.produto.ProdutoVendavel;
import net.originmobi.pdv.filter.ProdutoFilter;
import net.originmobi.pdv.model.*;
import net.originmobi.pdv.service.*;

@Controller
@RequestMapping("/produto")
@SessionAttributes("filterProduto")
public class ProdutoController {

    private static final String PRODUTO_LIST = "produto/list";
    private static final String PRODUTO_FORM = "produto/form";

    @Autowired
    private ProdutoService produtos;

    @Autowired
    private FornecedorService fornecedores;

    @Autowired
    private GrupoService grupos;

    @Autowired
    private CategoriaService categorias;

    @Autowired
    private ImagemProdutoService imagens;

    @Autowired
    private TributacaoService tributacoes;

    @Autowired
    private ModBcIcmsService modbcs;

    @GetMapping("/form")
    public ModelAndView form(Produto produto) {
        return new ModelAndView(PRODUTO_FORM, "produto", new Produto());
    }

    @ModelAttribute("filterProduto")
    public ProdutoFilter inicializerProdutoFilter() {
        return new ProdutoFilter();
    }

    @GetMapping
    public ModelAndView lista(@ModelAttribute("filterProduto") ProdutoFilter filter, Pageable pageable, Model model) {
        ModelAndView mv = new ModelAndView(PRODUTO_LIST);
        Page<Produto> listProdutos = produtos.filter(filter, pageable);
        mv.addObject("produtos", listProdutos);

        model.addAttribute("qtdpaginas", listProdutos.getTotalPages());
        model.addAttribute("pagAtual", listProdutos.getNumber());
        model.addAttribute("proxPagina", listProdutos.hasNext() ? listProdutos.nextPageable().getPageNumber() : listProdutos.getNumber());
        model.addAttribute("pagAnterior", listProdutos.hasPrevious() ? listProdutos.previousPageable().getPageNumber() : listProdutos.getNumber());
        model.addAttribute("hasNext", listProdutos.hasNext());
        model.addAttribute("hasPrevious", listProdutos.hasPrevious());

        return mv;
    }

    @PostMapping
    public String cadastrar(@RequestParam Map<String, String> request, RedirectAttributes attributes, SessionStatus status) {
        try {
            Long codigoprod = parseLongOrDefault(request.get("codigo"), 0L);
            Long codforne = Long.parseLong(request.get("fornecedor"));
            Long categoria = Long.parseLong(request.get("categoria"));
            Long grupo = Long.parseLong(request.get("grupo"));
            Double valorCusto = parseDoubleOrDefault(request.get("valor_custo"), 0.0);
            Double valorVenda = parseDoubleOrDefault(request.get("valor_venda"), 0.0);
            Date dataValidade = parseDate(request.get("data_validade"));

            Ativo situacao = Ativo.valueOf(request.get("ativo"));
            ProdutoSubstTributaria substituicao = ProdutoSubstTributaria.valueOf(request.get("subtributaria"));

            String mensagem = produtos.merger(codigoprod, codforne, categoria, grupo, request.get("balanca"), request.get("descricao"),
                    valorCusto, valorVenda, dataValidade, request.get("controla_estoque"), situacao.toString(),
                    request.get("unidade"), substituicao, request.get("ncm"), request.get("cest"),
                    parseLongOrDefault(request.get("tributacao"), null), parseLongOrDefault(request.get("modBcIcms"), null), request.get("vendavel"));

            attributes.addFlashAttribute("mensagem", mensagem);
            status.setComplete();
            return codigoprod != 0 ? "redirect:/produto/" + codigoprod : "redirect:/produto";
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagem", "Erro ao cadastrar produto: " + e.getMessage());
            return "redirect:/produto/form";
        }
    }

    private Long parseLongOrDefault(String value, Long defaultValue) {
        return (value == null || value.isEmpty()) ? defaultValue : Long.parseLong(value);
    }

    private Double parseDoubleOrDefault(String value, Double defaultValue) {
        return (value == null || value.isEmpty()) ? defaultValue : Double.parseDouble(value.replace(",", "."));
    }

    private Date parseDate(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    @GetMapping("{codigo}")
    public ModelAndView editar(@PathVariable("codigo") Produto produto) {
        return new ModelAndView(PRODUTO_FORM, "produto", produto).addObject("imagem", imagens.busca(produto.getCodigo()));
    }

    @ModelAttribute("ativo")
    public List<Ativo> ativo() {
        return Arrays.asList(Ativo.values());
    }

    @ModelAttribute("fornecedores")
    public List<Fornecedor> fornecedores() {
        return fornecedores.lista();
    }

    @ModelAttribute("grupos")
    public List<Grupo> grupos() {
        return grupos.lista();
    }

    @ModelAttribute("categorias")
    public List<Categoria> categorias() {
        return categorias.lista();
    }

    @ModelAttribute("tributacoes")
    public List<Tributacao> tributacoes() {
        return tributacoes.lista();
    }

    @ModelAttribute("modbcs")
    public List<ModBcIcms> modbc() {
        return modbcs.lista();
    }

    @ModelAttribute("produtoVendavel")
    public List<ProdutoVendavel> produtoVendavel() {
        return Arrays.asList(ProdutoVendavel.values());
    }
}
