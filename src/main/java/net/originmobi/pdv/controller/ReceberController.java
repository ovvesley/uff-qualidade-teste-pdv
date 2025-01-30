package net.originmobi.pdv.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.SessionStatus;
import org.springframework.web.util.UriComponentsBuilder;

import net.originmobi.pdv.filter.ClienteFilter;
import net.originmobi.pdv.model.Parcela;
import net.originmobi.pdv.model.Pessoa;
import net.originmobi.pdv.model.Receber;
import net.originmobi.pdv.service.ParcelaService;
import net.originmobi.pdv.service.PessoaService;
import net.originmobi.pdv.service.RecebimentoService;

@Controller
@RequestMapping("/receber")
@SessionAttributes("filtrocli")
public class ReceberController {

    private static final String RECEBER_LIST = "receber/list";
    private static final String RECEBER_FORM = "receber/form";

    @Autowired
    private PessoaService pessoas;

    @Autowired
    private ParcelaService parcelas;

    @Autowired
    private RecebimentoService recebimentos;

    @GetMapping("/form")
    public ModelAndView form() {
        return new ModelAndView(RECEBER_FORM, "receber", new Receber());
    }

    @ModelAttribute("filtrocli")
    public ClienteFilter inicializerFilter() {
        return new ClienteFilter();
    }

    @GetMapping
    public ModelAndView list(@ModelAttribute("filtrocli") ClienteFilter filter) {
        ModelAndView mv = new ModelAndView(RECEBER_LIST);
        List<Parcela> pagina = parcelas.lista(filter);
        mv.addObject("parcelas", pagina);
        mv.addObject("totalReceber", parcelas.totalReceberCliente(filter));
        return mv;
    }

    @PostMapping("/parcelaReceber")
    public @ResponseBody String receber(@RequestParam Map<String, String> request, SessionStatus status) {
        try {
            Long parcela = Long.parseLong(request.get("receber"));
            Double totalPago = parseDoubleOrDefault(request.get("vltotalPago"), 0.00);
            Double acrescimo = parseDoubleOrDefault(request.get("vlacre"), 0.00);
            Double desconto = parseDoubleOrDefault(request.get("vldesc"), 0.00);

            String mensagem = parcelas.receber(parcela, totalPago, acrescimo, desconto);
            status.setComplete();
            return mensagem;
        } catch (NumberFormatException e) {
            return "Erro ao processar os dados: " + e.getMessage();
        }
    }

    @GetMapping("/parcelas")
    public @ResponseBody String recebimento(@RequestParam Map<String, String> request, UriComponentsBuilder b) {
        try {
            Long codpes = Long.parseLong(request.get("codpessoa"));
            String[] arrayParcelas = request.get("parcelas").split(", ?");

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(b.path("/recebimento/").build().toUri());

            return headers.toString() + recebimentos.abrirRecebimento(codpes, arrayParcelas);
        } catch (Exception e) {
            return "Erro ao processar o recebimento: " + e.getMessage();
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

    @ModelAttribute("pessoas")
    public List<Pessoa> pessoas() {
        return pessoas.lista();
    }
}
