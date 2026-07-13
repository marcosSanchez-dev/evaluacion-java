package com.evaluacion.gateway.dto;

public class TransaccionInternaRequest {

    private String operacion;
    private String importe;
    private String cliente;
    private String secreto;

    public TransaccionInternaRequest() { }

    public TransaccionInternaRequest(String operacion, String importe, String cliente, String secreto) {
        this.operacion = operacion;
        this.importe = importe;
        this.cliente = cliente;
        this.secreto = secreto;
    }

    public String getOperacion() { return operacion; }
    public void setOperacion(String operacion) { this.operacion = operacion; }

    public String getImporte() { return importe; }
    public void setImporte(String importe) { this.importe = importe; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getSecreto() { return secreto; }
    public void setSecreto(String secreto) { this.secreto = secreto; }
}
