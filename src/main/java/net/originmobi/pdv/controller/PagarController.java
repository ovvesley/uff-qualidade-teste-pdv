package net.originmobi.pdv.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.support.SessionStatus;


import net.originmobi.pdv.filter.PagarParcelaFilter;
import net.originmobi.pdv.model.Caixa;
import net.originmobi.pdv.model.Fornecedor;
import net.originmobi.pdv.model.PagarParcela;
import net.originmobi.pdv.model.PagarTipo;
import net.originmobi.pdv.service.CaixaService;
import net.originmobi.pdv.service.FornecedorService;
import net.originmobi.pdv.service.PagarParcelaService;
import net.originmobi.pdv.service.PagarService;
import net.originmobi.pdv.service.PagarTipoService;

@Controller
@RequestMapping("/pagar")
@SessionAttributes("filter")
public class PagarController {

    private static final String PAGAR_FORM = "pagar/list";

    @Autowired
    private PagarService pagarServ;

    @Autowired
    private FornecedorService fornecedores;

    @Autowired
    private PagarParcelaService pagarParcelas;

    @Autowired
    private PagarTipoService pagartipos;

    @Autowired
    private CaixaService caixas;

    @ModelAttribute("filter")
    public PagarParcelaFilter inicializerFilter() {
        return new PagarParcelaFilter();
    }

    @GetMapping
    public ModelAndView list(@ModelAttribute("filter") PagarParcelaFilter filter, Pageable pageable, Model model) {
        ModelAndView mv = new ModelAndView(PAGAR_FORM);
        Page<PagarParcela> paginas = pagarParcelas.lista(filter, pageable);
        mv.addObject("parcelas", paginas);

        model.addAttribute("qtdpaginas", paginas.getTotalPages());
        model.addAttribute("pagAtual", paginas.getNumber());
        model.addAttribute("proxPagina", paginas.hasNext() ? paginas.nextPageable().getPageNumber() : paginas.getNumber());
        model.addAttribute("pagAnterior", paginas.hasPrevious() ? paginas.previousPageable().getPageNumber() : paginas.getNumber());
        model.addAttribute("hasNext", paginas.hasNext());
        model.addAttribute("hasPrevious", paginas.hasPrevious());

        return mv;
    }

    @PostMapping
public @ResponseBody String cadastroDespesa(@RequestParam Map<String, String> request, SessionStatus status) {
    try {
        Long codFornecedor = Long.parseLong(request.get("fornecedor"));
        Long tipo = Long.parseLong(request.get("tipo"));
        Double valor = Double.parseDouble(request.get("valor"));
        String obs = request.get("obs");

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate vencimento = LocalDate.parse(request.get("vencimento"), format);
        Optional<PagarTipo> pagarTipo = pagartipos.busca(tipo);

        String result = pagarTipo.map(tipoPagamento -> pagarServ.cadastrar(codFornecedor, valor, obs, vencimento, tipoPagamento))
                .orElse("Tipo de pagamento inválido");
        status.setComplete();
        return result;
    } catch (NumberFormatException e) {
        return "Erro ao processar os dados: " + e.getMessage();
    }
}

    @PostMapping("/quitar")
    public @ResponseBody String quitar(@RequestParam Map<String, String> request) {
        try {
            Long codparcela = Long.parseLong(request.get("parcela"));
            Long codCaixa = Long.parseLong(request.get("caixa"));

            Double vlpago = parseDoubleOrDefault(request.get("vlpago"), 0.0);
            Double vldesc = parseDoubleOrDefault(request.get("desconto"), 0.0);
            Double vlacre = parseDoubleOrDefault(request.get("acrescimo"), 0.0);

            return pagarServ.quitar(codparcela, vlpago, vldesc, vlacre, codCaixa);
        } catch (NumberFormatException e) {
            return "Erro ao processar os dados: " + e.getMessage();
        }
    }

    private Double parseDoubleOrDefault(String value, Double defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @ModelAttribute("fornecedores")
    public List<Fornecedor> fornecedores() {
        return fornecedores.lista();
    }

    @ModelAttribute("pagartipos")
    public List<PagarTipo> pagartipo() {
        return pagartipos.lista();
    }

    @ModelAttribute("caixasabertos")
    public List<Caixa> caixasAbertos() {
        return caixas.caixasAbertos();
    }
}
