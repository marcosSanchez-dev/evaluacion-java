package com.evaluacion.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operacion;

    private String importe;

    private String cliente;

    // Referencia numerica aleatoria de 6 digitos
    private String referencia;

    // Aprobada / Cancelada
    private String estatus;

    // Aqui se guarda la palabra "secreto" ya SIN cifrar, como pide el enunciado
    private String secreto;

    public Transaccion() { }

    public Transaccion(String operacion, String importe, String cliente, String referencia, String estatus, String secreto) {
        this.operacion = operacion;
        this.importe = importe;
        this.cliente = cliente;
        this.referencia = referencia;
        this.estatus = estatus;
        this.secreto = secreto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOperacion() { return operacion; }
    public void setOperacion(String operacion) { this.operacion = operacion; }

    public String getImporte() { return importe; }
    public void setImporte(String importe) { this.importe = importe; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public String getSecreto() { return secreto; }
    public void setSecreto(String secreto) { this.secreto = secreto; }
}
