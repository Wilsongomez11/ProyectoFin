package com.example.ProyectoFinal.Controller;

import com.example.ProyectoFinal.Modelo.GenerarFacturaRequest;
import com.example.ProyectoFinal.Modelo.Factura;
import com.example.ProyectoFinal.Service.FacturaPdfService;
import com.example.ProyectoFinal.Service.FacturaReporteExcelService;
import com.example.ProyectoFinal.Service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private FacturaPdfService facturaPdfService;

    @Autowired
    private FacturaReporteExcelService facturaReporteExcelService;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {
        byte[] pdfBytes = facturaPdfService.generarPdfFactura(id);

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf")
                .header(
                        "Content-Disposition",
                        "attachment; filename=factura-" + id + ".pdf"
                )
                .body(pdfBytes);
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarFactura(@RequestBody GenerarFacturaRequest request) {
        try {
            Factura factura = facturaService.generarFactura(request);
            return ResponseEntity.ok(factura);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return facturaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> obtenerPorPedido(@PathVariable Long pedidoId) {
        return facturaService.obtenerPorPedido(pedidoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Factura>> listarTodas() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }
    @GetMapping("/reporte/dia")
    public ResponseEntity<List<Factura>> reporteDia() {
        return ResponseEntity.ok(facturaService.reporteDiario());
    }

    @GetMapping("/reporte/semana")
    public ResponseEntity<List<Factura>> reporteSemana() {
        return ResponseEntity.ok(facturaService.reporteSemanal());
    }

    @GetMapping("/reporte/mes")
    public ResponseEntity<List<Factura>> reporteMes() {
        return ResponseEntity.ok(facturaService.reporteMensual());
    }

    @GetMapping("/excel/dia")
    public ResponseEntity<byte[]> excelDia() {
        byte[] excel = facturaService.generarExcel(facturaService.reporteDiario());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reporte-dia.xlsx")
                .body(excel);
    }

    @GetMapping("/excel/semana")
    public ResponseEntity<byte[]> excelSemana() {
        byte[] excel = facturaService.generarExcel(facturaService.reporteSemanal());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reporte-semana.xlsx")
                .body(excel);
    }

    @GetMapping("/excel/mes")
    public ResponseEntity<byte[]> excelMes() {
        byte[] excel = facturaService.generarExcel(facturaService.reporteMensual());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reporte-mes.xlsx")
                .body(excel);
    }

    @GetMapping("/reporte/excel")
    public ResponseEntity<byte[]> generarReporteExcel(@RequestParam String tipo) {
        tipo = tipo.toLowerCase();

        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio;
        LocalDateTime fin;

        switch (tipo) {
            case "dia":
            case "diario":
                inicio = hoy.atStartOfDay();
                fin = hoy.plusDays(1).atStartOfDay().minusNanos(1);
                break;
            case "semana":
            case "semanal":
                LocalDate hace7 = hoy.minusDays(6);
                inicio = hace7.atStartOfDay();
                fin = hoy.plusDays(1).atStartOfDay().minusNanos(1);
                break;
            case "mes":
            case "mensual":
                LocalDate primerDiaMes = hoy.withDayOfMonth(1);
                inicio = primerDiaMes.atStartOfDay();
                fin = primerDiaMes.plusMonths(1).atStartOfDay().minusNanos(1);
                break;
            default:
                return ResponseEntity.badRequest()
                        .body(null);
        }

        byte[] excel = facturaReporteExcelService.generarReporteExcel(inicio, fin);

        String nombreArchivo = "reporte-" + tipo + ".xlsx";

        return ResponseEntity
                .ok()
                .header("Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header("Content-Disposition",
                        "attachment; filename=" + nombreArchivo)
                .body(excel);
    }
}

