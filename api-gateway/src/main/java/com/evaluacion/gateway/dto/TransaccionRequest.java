package com.evaluacion.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TransaccionRequest {

    @NotBlank(message = "El campo 'operacion' es obligatorio")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El campo 'operacion' solo debe contener letras")
    private String operacion;

    @NotBlank(message = "El campo 'importe' es obligatorio")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "El campo 'importe' debe tener formato de moneda, ej. 100.00")
    private String importe;

    @NotBlank(message = "El campo 'cliente' es obligatorio")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El campo 'cliente' solo debe contener letras")
    private String cliente;

    @NotBlank(message = "El campo 'secreto' es obligatorio")
    private String secreto;

    public String getOperacion() { return operacion; }
    public void setOperacion(String operacion) { this.operacion = operacion; }

    public String getImporte() { return importe; }
    public void setImporte(String importe) { this.importe = importe; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getSecreto() { return secreto; }
    public void setSecreto(String secreto) { this.secreto = secreto; }
}
