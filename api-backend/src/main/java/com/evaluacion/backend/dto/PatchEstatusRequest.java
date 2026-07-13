package com.evaluacion.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class PatchEstatusRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String referencia;

    @NotBlank
    private String estatus; // se espera el valor "cancelar"

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
}
