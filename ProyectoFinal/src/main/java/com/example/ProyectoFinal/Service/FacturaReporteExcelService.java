package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.DetallePedido;
import com.example.ProyectoFinal.Modelo.Factura;
import com.example.ProyectoFinal.Modelo.Pedido;
import com.example.ProyectoFinal.Repository.FacturaRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacturaReporteExcelService {

    @Autowired
    private FacturaRepository facturaRepository;

    public byte[] generarReporteExcel(LocalDateTime inicio, LocalDateTime fin) {
        List<Factura> facturas = facturaRepository.findByPedidoFechaBetween(inicio, fin);

        try (Workbook workbook = new XSSFWorkbook()) {

            // ----------- HOJA 1: RESUMEN -------------
            Sheet resumenSheet = workbook.createSheet("Resumen");
            crearResumen(resumenSheet, facturas);

            // ----------- HOJA 2: DETALLE PRODUCTOS -------------
            Sheet productosSheet = workbook.createSheet("Productos Vendidos");
            llenarProductos(productosSheet, facturas);

            // ----------- HOJA 3: FACTURAS -------------
            Sheet facturasSheet = workbook.createSheet("Facturas");
            llenarFacturas(facturasSheet, facturas);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error creando Excel: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------
    // HOJA 1: RESUMEN
    // -------------------------------------------------------------
    private void crearResumen(Sheet sheet, List<Factura> facturas) {
        Row title = sheet.createRow(0);
        title.createCell(0).setCellValue("REPORTE RESUMEN DEL PERIODO");

        double totalVentas = facturas.stream().mapToDouble(Factura::getTotal).sum();
        double totalPropinas = facturas.stream().mapToDouble(f -> f.getPropina() != null ? f.getPropina() : 0).sum();
        long totalPedidos = facturas.size();

        Map<String, Long> metodosPago = facturas.stream()
                .collect(Collectors.groupingBy(Factura::getMetodoPago, Collectors.counting()));

        sheet.createRow(2).createCell(0).setCellValue("Total ventas:");
        sheet.getRow(2).createCell(1).setCellValue(totalVentas);

        sheet.createRow(3).createCell(0).setCellValue("Total propinas:");
        sheet.getRow(3).createCell(1).setCellValue(totalPropinas);

        sheet.createRow(4).createCell(0).setCellValue("Total facturas:");
        sheet.getRow(4).createCell(1).setCellValue(totalPedidos);

        // Métodos de pago
        int row = 6;
        sheet.createRow(row).createCell(0).setCellValue("MÉTODOS DE PAGO");
        row++;

        for (String metodo : metodosPago.keySet()) {
            sheet.createRow(row).createCell(0).setCellValue(metodo);
            sheet.getRow(row).createCell(1).setCellValue(metodosPago.get(metodo));
            row++;
        }
    }

    // -------------------------------------------------------------
    // HOJA 2: PRODUCTOS VENDIDOS
    // -------------------------------------------------------------
    private void llenarProductos(Sheet sheet, List<Factura> facturas) {
        Row header = sheet.createRow(0);
        String[] cols = {"Producto", "Cantidad", "Precio Unitario", "Subtotal", "Fecha", "Pedido ID", "Factura ID"};
        for (int i = 0; i < cols.length; i++) header.createCell(i).setCellValue(cols[i]);

        int rowNum = 1;

        for (Factura factura : facturas) {
            Pedido pedido = factura.getPedido();
            if (pedido == null) continue;

            for (DetallePedido det : pedido.getDetalles()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(det.getProducto().getNombre());
                row.createCell(1).setCellValue(det.getCantidad());
                row.createCell(2).setCellValue(det.getPrecioUnitario());
                row.createCell(3).setCellValue(det.getCantidad() * det.getPrecioUnitario());
                row.createCell(4).setCellValue(String.valueOf(factura.getFechaEmision()));
                row.createCell(5).setCellValue(pedido.getId());
                row.createCell(6).setCellValue(factura.getId());
            }
        }
    }

    // -------------------------------------------------------------
    // HOJA 3: FACTURAS COMPLETAS
    // -------------------------------------------------------------
    private void llenarFacturas(Sheet sheet, List<Factura> facturas) {
        Row header = sheet.createRow(0);

        String[] cols = {
                "Factura", "Fecha", "Cliente", "Documento", "Método Pago",
                "Subtotal", "Propina", "Impuestos", "Total", "Estado"
        };

        for (int i = 0; i < cols.length; i++) header.createCell(i).setCellValue(cols[i]);

        int row = 1;

        for (Factura f : facturas) {
            Row r = sheet.createRow(row++);
            r.createCell(0).setCellValue(f.getNumero());
            r.createCell(1).setCellValue(String.valueOf(f.getFechaEmision()));
            r.createCell(2).setCellValue(f.getClienteNombre());
            r.createCell(3).setCellValue(f.getClienteDocumento());
            r.createCell(4).setCellValue(f.getMetodoPago());
            r.createCell(5).setCellValue(f.getSubtotal());
            r.createCell(6).setCellValue(f.getPropina());
            r.createCell(7).setCellValue(f.getImpuestos());
            r.createCell(8).setCellValue(f.getTotal());
            r.createCell(9).setCellValue(f.getEstado());
        }
    }
}
