package com.evaluacion.gateway.controller;

import com.evaluacion.gateway.dto.TransaccionRequest;
import com.evaluacion.gateway.dto.TransaccionResponse;
import com.evaluacion.gateway.service.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateway")
@CrossOrigin(origins = "*")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }


    @PostMapping("/venta")
    public ResponseEntity<TransaccionResponse> registrarVenta(@Valid @RequestBody TransaccionRequest request) {
        TransaccionResponse response = transaccionService.procesarTransaccion(request);
        return ResponseEntity.ok(response);
    }
}
