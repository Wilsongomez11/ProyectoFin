package com.example.ProyectoFinal.Service;

import com.example.ProyectoFinal.Modelo.GenerarFacturaRequest;
import com.example.ProyectoFinal.Modelo.Factura;
import com.example.ProyectoFinal.Modelo.Pedido;
import com.example.ProyectoFinal.Repository.FacturaRepository;
import com.example.ProyectoFinal.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Factura generarFactura(GenerarFacturaRequest request) {

        Optional<Pedido> pedidoOpt = pedidoRepository.findById(request.getPedidoId());
        if (pedidoOpt.isEmpty()) {
            throw new RuntimeException("Pedido no encontrado con id " + request.getPedidoId());
        }

        Pedido pedido = pedidoOpt.get();

        Factura factura = new Factura();
        factura.setPedido(pedido);
        factura.setFechaEmision(LocalDateTime.now());

        // Numero simple. Si quieres, luego lo cambias por consecutivo en base de datos
        String numero = "F-" + System.currentTimeMillis();
        factura.setNumero(numero);

        factura.setClienteNombre(request.getClienteNombre());
        factura.setClienteDocumento(request.getClienteDocumento());
        factura.setMetodoPago(request.getMetodoPago());

        Double subtotal = pedido.getTotal(); // ajusta si tu campo se llama diferente
        if (subtotal == null) {
            subtotal = 0.0;
        }
        factura.setSubtotal(subtotal);

        Double propina = request.getPropina() != null ? request.getPropina() : 0.0;
        factura.setPropina(propina);

        Double impuestos = 0.0;
        factura.setImpuestos(impuestos);

        Double total = subtotal + propina + impuestos;
        factura.setTotal(total);

        factura.setEstado("EMITIDA");

        return facturaRepository.save(factura);
    }

    public Optional<Factura> obtenerPorId(Long id) {
        return facturaRepository.findById(id);
    }

    public Optional<Factura> obtenerPorPedido(Long pedidoId) {
        return facturaRepository.findByPedidoId(pedidoId);
    }

    public List<Factura> listarTodas() {
        return facturaRepository.findAll();
    }
    public List<Factura> buscarPorRango(LocalDateTime inicio, LocalDateTime fin) {
        return facturaRepository.findByPedidoFechaBetween(inicio, fin);
    }

    public List<Factura> reporteDiario() {
        LocalDate hoy = LocalDate.now();
        return buscarPorRango(hoy.atStartOfDay(), hoy.atTime(23, 59, 59));
    }

    public List<Factura> reporteSemanal() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate finSemana = hoy.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        return buscarPorRango(inicioSemana.atStartOfDay(), finSemana.atTime(23, 59, 59));
    }

    public List<Factura> reporteMensual() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());

        return buscarPorRango(inicioMes.atStartOfDay(), finMes.atTime(23, 59, 59));
    }
    public byte[] generarExcel(List<Factura> facturas) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte");

            Row header = sheet.createRow(0);
            String[] columnas = {
                    "ID", "Numero", "Fecha Emision", "Cliente",
                    "Documento", "Método Pago", "Subtotal",
                    "Propina", "Impuestos", "Total", "Estado", "Pedido ID"
            };

            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            int rowIdx = 1;
            for (Factura f : facturas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(f.getId());
                row.createCell(1).setCellValue(f.getNumero());
                row.createCell(2).setCellValue(f.getFechaEmision().toString());
                row.createCell(3).setCellValue(f.getClienteNombre());
                row.createCell(4).setCellValue(f.getClienteDocumento());
                row.createCell(5).setCellValue(f.getMetodoPago());
                row.createCell(6).setCellValue(f.getSubtotal());
                row.createCell(7).setCellValue(f.getPropina());
                row.createCell(8).setCellValue(f.getImpuestos());
                row.createCell(9).setCellValue(f.getTotal());
                row.createCell(10).setCellValue(f.getEstado());
                row.createCell(11).setCellValue(f.getPedido().getId());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando Excel: " + e.getMessage());
        }
    }
}

