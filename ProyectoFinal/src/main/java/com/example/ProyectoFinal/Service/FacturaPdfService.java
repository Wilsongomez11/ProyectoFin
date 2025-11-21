package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.DetallePedido;
import com.example.ProyectoFinal.Modelo.Factura;
import com.example.ProyectoFinal.Modelo.Pedido;
import com.example.ProyectoFinal.Repository.FacturaRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class FacturaPdfService {

    @Autowired
    private FacturaRepository facturaRepository;

    public byte[] generarPdfFactura(Long facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        Pedido pedido = factura.getPedido();
        if (pedido == null) {
            throw new RuntimeException("La factura no tiene un pedido asociado");
        }

        try {
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            try {
                Image logo = Image.getInstance(
                        new ClassPathResource("static/logo.png").getURL()
                );
                logo.scaleToFit(120, 120);
                logo.setAlignment(Image.ALIGN_LEFT);
                document.add(logo);
            } catch (Exception e) {
                System.out.println("⚠ No se encontró el logo");
            }

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph encabezado = new Paragraph("FACTURA ELECTRÓNICA DE VENTA\n", titleFont);
            encabezado.setAlignment(Element.ALIGN_CENTER);
            document.add(encabezado);

            Paragraph nroFactura = new Paragraph("N° " + factura.getNumero(), titleFont);
            nroFactura.setAlignment(Element.ALIGN_CENTER);
            document.add(nroFactura);

            document.add(new Paragraph("\n"));


            Font infoFont = new Font(Font.FontFamily.HELVETICA, 11);

            document.add(new Paragraph("PIZZA STEVE", infoFont));
            document.add(new Paragraph("NIT: 901999888-1", infoFont));
            document.add(new Paragraph("Dirección: Carrera 15 #3-09, Zipaquira - Cundinamarca", infoFont));
            document.add(new Paragraph("Tel: 300 339 3591", infoFont));
            document.add(new Paragraph("Responsabilidad: Wilson Gomez", infoFont));
            document.add(new Paragraph("Email: facturas@pizzeria.com", infoFont));

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("DATOS DEL CLIENTE:", titleFont));
            document.add(new Paragraph("Nombre: " + (factura.getClienteNombre() != null ? factura.getClienteNombre() : "Consumidor Final")));
            document.add(new Paragraph("Documento: " + (factura.getClienteDocumento() != null ? factura.getClienteDocumento() : "N/A")));
            document.add(new Paragraph("Fecha de emisión: " + factura.getFechaEmision()));
            document.add(new Paragraph("Método de pago: " + factura.getMetodoPago()));
            document.add(new Paragraph("Propina: $" + factura.getPropina()));

            document.add(new Paragraph("\n"));

            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{4, 1.2f, 2, 2});

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            String[] headers = {"Producto", "Cant.", "P. Unitario", "Subtotal"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);
            }

            for (DetallePedido item : pedido.getDetalles()) {
                String nombre = item.getProducto() != null ? item.getProducto().getNombre() : "Producto";
                double cant = item.getCantidad();
                double unit = item.getPrecioUnitario();
                double subtotal = cant * unit;

                tabla.addCell(nombre);
                tabla.addCell(String.format("%.1f", cant));
                tabla.addCell("$" + String.format("%,.0f", unit));
                tabla.addCell("$" + String.format("%,.0f", subtotal));
            }

            document.add(tabla);

            document.add(new Paragraph("\n"));

            //--------------------------------------------------------------------
            // TOTAL ÍCONICO ESTILO DIAN
            //--------------------------------------------------------------------
            Font totalFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);

            Paragraph total = new Paragraph("TOTAL A PAGAR: $" + String.format("%,.0f", factura.getTotal()), totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage());
        }
    }
}
