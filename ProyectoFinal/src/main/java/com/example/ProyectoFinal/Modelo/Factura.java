package com.example.ProyectoFinal.Modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroFactura;
    private String fecha;

    @OneToOne
    @JoinColumn(name = "pedido_id", unique = true)
    private Pedido pedido;

    private Double subtotal;
    private Double iva;
    private Double propina;
    private Double total;

    private String metodoPago;

    public Factura() {}

    public Factura(Pedido pedido, String metodoPago, Double propina) {
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        this.propina = propina;

        this.fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        double subtotalCalc = pedido.getTotal() / 1.19;
        double ivaCalc = pedido.getTotal() - subtotalCalc;

        this.subtotal = subtotalCalc;
        this.iva = ivaCalc;
        this.total = pedido.getTotal() + propina;

        this.numeroFactura = "FAC-" + System.currentTimeMillis();
    }

    public Long getId() { return id; }
    public String getNumeroFactura() { return numeroFactura; }
    public String getFecha() { return fecha; }
    public Pedido getPedido() { return pedido; }
    public Double getSubtotal() { return subtotal; }
    public Double getIva() { return iva; }
    public Double getPropina() { return propina; }
    public Double getTotal() { return total; }
    public String getMetodoPago() { return metodoPago; }
}
