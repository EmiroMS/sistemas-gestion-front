package sistemaGestionFront.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class PdfGenerator {

    public static String generarFactura(Map<String,Object> venta)
            throws Exception {

        String ruta = "factura_" + venta.get("id") + ".pdf";

        // ✅ TAMAÑO 80mm TERMICO
        Rectangle ticket =
                new Rectangle(226f, 800f);

        Document document =
                new Document(ticket,5,5,5,5);

        PdfWriter.getInstance(
                document,
                new FileOutputStream(ruta)
        );

        document.open();

        // ======================
        // FUENTES
        // ======================
        Font titulo =
                new Font(Font.FontFamily.COURIER,10,Font.BOLD);

        Font normal =
                new Font(Font.FontFamily.COURIER,8);

        Font totalFont =
                new Font(Font.FontFamily.COURIER,10,Font.BOLD);

        // ======================
        // ENCABEZADO
        // ======================
        Paragraph empresa =
                new Paragraph(
                        "LICOSOFT HM\nLICORERA",
                        titulo);

        empresa.setAlignment(Element.ALIGN_CENTER);
        document.add(empresa);

        document.add(new Paragraph(" "));

        document.add(
                new Paragraph(
                        "Factura #: "
                        + venta.get("id"),
                        normal));

        document.add(
                new Paragraph(
                        "Fecha: "
                        + venta.get("fecha"),
                        normal));

        document.add(
                new Paragraph(
                        "-----------------------------",
                        normal));

        // ======================
        // PRODUCTOS
        // ======================
        List<Map<String,Object>> detalles =
                (List<Map<String,Object>>) venta.get("detalles");

        for(Map<String,Object> d : detalles){

            Map producto =
                    (Map)d.get("producto");

            String nombre =
                    producto.get("nombre").toString();

            int cantidad =
                    ((Number)d.get("cantidad")).intValue();

            double precio =
                    ((Number)d.get("precioVendido"))
                            .doubleValue();

            double subtotal =
                    ((Number)d.get("subtotal"))
                            .doubleValue();

            document.add(
                    new Paragraph(nombre,normal));

            document.add(
                    new Paragraph(
                            cantidad+
                            " x "+precio+
                            "    "+subtotal,
                            normal));
        }

        document.add(
                new Paragraph(
                        "-----------------------------",
                        normal));

        // ======================
        // TOTALES
        // ======================
        double subtotal =
                ((Number)venta.get("totalSinDescuento"))
                        .doubleValue();

        double descuento =
                ((Number)venta.get("descuento"))
                        .doubleValue();

        double total =
                ((Number)venta.get("totalFinal"))
                        .doubleValue();

        document.add(
                new Paragraph(
                        "Subtotal: "+subtotal,
                        normal));

        document.add(
                new Paragraph(
                        "Descuento: -"+descuento,
                        normal));

        Paragraph totalTxt =
                new Paragraph(
                        "TOTAL: "+total,
                        totalFont);

        totalTxt.setAlignment(Element.ALIGN_CENTER);

        document.add(totalTxt);

        document.add(new Paragraph("\n"));

        Paragraph gracias =
                new Paragraph(
                        "GRACIAS POR SU COMPRA",
                        titulo);

        gracias.setAlignment(Element.ALIGN_CENTER);

        document.add(gracias);

        document.close();

        return ruta;
    }
}