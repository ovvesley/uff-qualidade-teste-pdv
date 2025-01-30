package net.originmobi.pdv.xml.nfe;

import java.security.SecureRandom;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import net.originmobi.pdv.model.NotaFiscal;
import net.originmobi.pdv.service.notafiscal.NotaFiscalService;

public class ConversorXmlNfe implements Converter {

    private final NotaFiscalService nfService;
    private String chaveNfeRetorno = "";
    private static final SecureRandom random = new SecureRandom();
    private static final DecimalFormat FORMATO = new DecimalFormat("#0.00");

    public ConversorXmlNfe(NotaFiscalService nfService) {
        this.nfService = nfService;
    }

    @Override
    public boolean canConvert(Class type) {
        return NotaFiscal.class.equals(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        NotaFiscal notaFiscal = (NotaFiscal) source;

        String ufEmissor = notaFiscal.getEmissor().getEndereco().getCidade().getEstado().getCodigoUF();
        String cnpjEmissor = notaFiscal.getEmissor().getCnpj().replace("\D", "");
        int tipoRegime = notaFiscal.getEmissor().getRegime_tributario().getTipoRegime();

        // Gerando código aleatório de forma mais segura
        int codAleatorio = 10000000 + random.nextInt(89999999);

        String serie = StringUtils.leftPad(String.valueOf(notaFiscal.getEmissor().getParametro().getSerie_nfe()), 3, "0");
        String numeroNf = StringUtils.leftPad(String.valueOf(notaFiscal.getNumero()), 9, "0");

        String cNF = String.valueOf(codAleatorio);
        String chaveNfe = ufEmissor + "1805" + cnpjEmissor + "55" + serie + numeroNf + "1" + cNF;

        // Gerando dígito verificador
        Integer cDV = nfService.geraDV(chaveNfe);
        chaveNfeRetorno = chaveNfe + cDV;

        writer.addAttribute("xmlns", "http://www.portalfiscal.inf.br/nfe");
        writer.addAttribute("versao", "3.10");

        writer.startNode("idLote");
        context.convertAnother(1);
        writer.endNode();

        writer.startNode("indSinc");
        context.convertAnother(0);
        writer.endNode();

        writer.startNode("NFe");
        writer.addAttribute("xmlns", "http://www.portalfiscal.inf.br/nfe");

        writer.startNode("infNFe");
        writer.addAttribute("Id", "NFe" + chaveNfe + cDV);
        writer.addAttribute("versao", "3.10");

        writer.startNode("ide");
        writer.startNode("cUF");
        context.convertAnother(ufEmissor);
        writer.endNode();

        writer.startNode("cNF");
        context.convertAnother(cNF);
        writer.endNode();

        writer.startNode("natOp");
        context.convertAnother(notaFiscal.getNatureza_operacao());
        writer.endNode();

        writer.startNode("tpNF");
        context.convertAnother(notaFiscal.getTipo().ordinal());
        writer.endNode();

        writer.startNode("cMunFG");
        context.convertAnother(notaFiscal.getEmissor().getEndereco().getCidade().getCodigo_municipio());
        writer.endNode();

        writer.startNode("tpAmb");
        context.convertAnother(notaFiscal.getTipo_ambiente());
        writer.endNode();

        writer.endNode(); // fim nodo ide
        writer.endNode(); // fim nodo infNFe
        writer.endNode(); // fim nodo NFe
    }

    public String retornaChaveNfe() {
        return chaveNfeRetorno;
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return null;
    }
}
