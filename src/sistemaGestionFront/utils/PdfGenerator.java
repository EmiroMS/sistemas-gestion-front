package sistemaGestionFront.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import sistemaGestionFront.session.Sesion;

import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PdfGenerator {

    public static String generarFactura(Map<String,Object> venta)
            throws Exception {

        String ruta = "factura_" + venta.get("id") + ".pdf";

        List<Map<String,Object>> detalles =
                (List<Map<String,Object>>) venta.get("detalles");

        // ======================
        // ALTURA DINAMICA
        // ======================

        int items = detalles.size();

        float altura =
                250 + (items * 35); // se ajusta segun productos

        Rectangle ticket =
                new Rectangle(226f, altura);

        Document document =
                new Document(ticket,5,5,5,5);

        PdfWriter.getInstance(
                document,
                new FileOutputStream(ruta)
        );

        document.open();

        NumberFormat moneda =
                NumberFormat.getCurrencyInstance(
                        new Locale("es","CO"));

        DateTimeFormatter formatoFecha =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm");

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
                        "LICOSOFT HM\nNIT: 900123456-7\nBarranquilla - Colombia",
                        titulo);

        empresa.setAlignment(Element.ALIGN_CENTER);
        document.add(empresa);

        document.add(new Paragraph(" "));

        int idVenta =
                ((Number)venta.get("id")).intValue();

        String numeroFactura =
                String.format("%08d", idVenta);

        document.add(
                new Paragraph(
                        "FACTURA POS #" + numeroFactura,
                        normal));

        LocalDateTime fecha =
                LocalDateTime.parse(
                        venta.get("fecha").toString());

        document.add(
                new Paragraph(
                        "Fecha: "
                        + fecha.format(formatoFecha),
                        normal));

        document.add(
                new Paragraph(
                        "Vendedor: "
                        + Sesion.getNombre(),
                        normal));

        document.add(
                new Paragraph(
                        "--------------------------------",
                        normal));

        document.add(
                new Paragraph(
                        "Producto",
                        normal));

        document.add(
                new Paragraph(
                        "Cant x Precio        Total",
                        normal));

        document.add(
                new Paragraph(
                        "--------------------------------",
                        normal));

        // ======================
        // PRODUCTOS
        // ======================

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

            document.add(new Paragraph(nombre, normal));

            String linea = String.format(
                    "%2d x %-10s %10s",
                    cantidad,
                    moneda.format(precio),
                    moneda.format(subtotal)
            );

            document.add(new Paragraph(linea, normal));
        }

        document.add(
                new Paragraph(
                        "--------------------------------",
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
                        "SUBTOTAL:      " + moneda.format(subtotal),
                        normal));

        document.add(
                new Paragraph(
                        "DESCUENTO:    -" + moneda.format(descuento),
                        normal));

        Paragraph totalTxt =
                new Paragraph(
                        "TOTAL:        " + moneda.format(total),
                        totalFont);

        document.add(totalTxt);

        document.add(new Paragraph("\n"));

        Paragraph gracias =
                new Paragraph(
                        "*** GRACIAS POR SU COMPRA ***",
                        titulo);

        gracias.setAlignment(Element.ALIGN_CENTER);

        document.add(gracias);

        document.close();

        return ruta;
    }
}