package sistemaGestionFront.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import sistemaGestionFront.session.Sesion;

import java.io.FileOutputStream;

import java.text.NumberFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Map;
import java.util.Locale;

public class PdfGenerator {

    public static String generarFactura(
            Map<String,Object> venta)
            throws Exception{

        String ruta =
                "factura_"+venta.get("id")+".pdf";

        Document document =
                new Document(
                        PageSize.LETTER,
                        30,30,30,30
                );

        PdfWriter.getInstance(
                document,
                new FileOutputStream(ruta)
        );

        document.open();

        NumberFormat moneda =
                NumberFormat.getCurrencyInstance(
                        new Locale("es","CO")
                );

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                );

        Font titulo =
                new Font(
                        Font.FontFamily.HELVETICA,
                        16,
                        Font.BOLD
                );

        Font normal =
                new Font(
                        Font.FontFamily.HELVETICA,
                        10
                );

        Font bold =
                new Font(
                        Font.FontFamily.HELVETICA,
                        10,
                        Font.BOLD
                );


        // =========================
        // ENCABEZADO
        // =========================

        PdfPTable header =
                new PdfPTable(2);

        header.setWidthPercentage(100);

        PdfPCell empresa =
                new PdfPCell();

        empresa.setBorder(0);

        empresa.addElement(
                new Paragraph("LICORERA HM",titulo));

        empresa.addElement(
                new Paragraph("NIT: 900123xxx",normal));

        empresa.addElement(
                new Paragraph("Nechí, Antioquia, Colombia",normal));

        empresa.addElement(
                new Paragraph("Tel: 3112330002",normal));


        PdfPCell facturaBox =
                new PdfPCell();

        facturaBox.setPadding(10);

        facturaBox.addElement(
                new Paragraph("FACTURA",titulo));

        facturaBox.addElement(
                new Paragraph(
                        "No: "+
                        String.format("%06d",
                                ((Number)venta.get("id")).intValue())
                )
        );

        LocalDateTime fecha =
                LocalDateTime.parse(
                        venta.get("fecha").toString()
                );

        facturaBox.addElement(
                new Paragraph(
                        "Fecha: "+
                        fecha.format(formato)
                )
        );

        header.addCell(empresa);
        header.addCell(facturaBox);

        document.add(header);

        document.add(new Paragraph("\n"));


        // =========================
        // CLIENTE CORREGIDO
        // =========================

        PdfPTable cliente =
                new PdfPTable(2);

        cliente.setWidthPercentage(100);

        Object clienteObj =
                venta.get("cliente");

        String nombreCliente =
                "CONSUMIDOR FINAL";

        if(clienteObj!=null){

            // si viene como Map
            if(clienteObj instanceof Map){

                Map clienteMap =
                        (Map)clienteObj;

                nombreCliente =
                        clienteMap.get("nombre")
                        .toString();

            }
            else{

                // si viene como String
                String texto =
                        clienteObj.toString();

                if(texto.contains("nombre=")){

                    nombreCliente =
                            texto.split("nombre=")[1]
                            .split(",")[0];

                }else{

                    nombreCliente = texto;

                }

            }

        }

        cliente.addCell("Cliente");

        cliente.addCell(nombreCliente);

        cliente.addCell("Vendedor");

        cliente.addCell(
                Sesion.getNombre()
        );

        document.add(cliente);

        document.add(new Paragraph("\n"));


        // =========================
        // TABLA PRODUCTOS
        // =========================

        PdfPTable tabla =
                new PdfPTable(4);

        tabla.setWidthPercentage(100);

        tabla.setWidths(
                new int[]{4,1,2,2}
        );

        tabla.addCell(
                new Phrase("Descripción",bold));

        tabla.addCell(
                new Phrase("Cant",bold));

        tabla.addCell(
                new Phrase("Precio",bold));

        tabla.addCell(
                new Phrase("Total",bold));


        List<Map<String,Object>> detalles =
                (List<Map<String,Object>>)
                        venta.get("detalles");


        for(Map<String,Object> d:detalles){

            Map producto =
                    (Map)d.get("producto");

            tabla.addCell(
                    producto.get("nombre")
                            .toString());

            tabla.addCell(
                    String.valueOf(
                            d.get("cantidad")
                    ));

            tabla.addCell(
                    moneda.format(
                            ((Number)d.get("precioVendido"))
                                    .doubleValue()
                    ));

            tabla.addCell(
                    moneda.format(
                            ((Number)d.get("subtotal"))
                                    .doubleValue()
                    ));
        }

        document.add(tabla);

        document.add(new Paragraph("\n"));


        // =========================
        // TOTALES
        // =========================

        double subtotal =
                ((Number)venta.get("totalSinDescuento"))
                        .doubleValue();

        double descuento =
                ((Number)venta.get("descuento"))
                        .doubleValue();

        double total =
                ((Number)venta.get("totalFinal"))
                        .doubleValue();


        PdfPTable totales =
                new PdfPTable(2);

        totales.setWidthPercentage(40);

        totales.setHorizontalAlignment(
                Element.ALIGN_RIGHT);

        totales.addCell("Subtotal");

        totales.addCell(
                moneda.format(subtotal));

        totales.addCell("Descuento");

        totales.addCell(
                moneda.format(descuento));

        totales.addCell("TOTAL");

        PdfPCell totalCell =
                new PdfPCell(
                        new Phrase(
                                moneda.format(total),
                                new Font(
                                        Font.FontFamily.HELVETICA,
                                        14,
                                        Font.BOLD
                                )
                        )
                );

        totales.addCell(totalCell);

        document.add(totales);


        // =========================
        // PIE
        // =========================

        document.add(new Paragraph("\n"));

        Paragraph legal =
                new Paragraph(
                        "Factura generada por LicoSoft HM",
                        new Font(
                                Font.FontFamily.HELVETICA,
                                8
                        )
                );

        legal.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(legal);


        document.close();

        return ruta;
    }
}