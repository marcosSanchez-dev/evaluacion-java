package com.evaluacion.backend.controller;

import com.evaluacion.backend.dto.PatchEstatusRequest;
import com.evaluacion.backend.dto.TransaccionRequest;
import com.evaluacion.backend.dto.TransaccionResponse;
import com.evaluacion.backend.model.Transaccion;
import com.evaluacion.backend.repository.TransaccionRepository;
import com.evaluacion.backend.service.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

@CrossOrigin(origins = {
        "http://127.0.0.1:5500",
        "http://localhost:5500"
})
@RestController
@RequestMapping("/api/backend")
public class TransaccionController {

    private final TransaccionService transaccionService;
    private final TransaccionRepository transaccionRepository;

    public TransaccionController(TransaccionService transaccionService, TransaccionRepository transaccionRepository) {
        this.transaccionService = transaccionService;
        this.transaccionRepository = transaccionRepository;
    }

    @PostMapping("/transacciones")
    public ResponseEntity<TransaccionResponse> guardar(@Valid @RequestBody TransaccionRequest request) {
        return ResponseEntity.ok(transaccionService.guardarTransaccion(request));
    }

    @PatchMapping("/transacciones")
    public ResponseEntity<?> cancelar(@Valid @RequestBody PatchEstatusRequest request) {
        if (!"cancelar".equalsIgnoreCase(request.getEstatus())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El unico valor soportado en 'estatus' es 'cancelar'"));
        }

        boolean actualizado = transaccionService.cancelarTransaccion(request.getId(), request.getReferencia());

        if (actualizado) {
            return ResponseEntity.ok(Map.of("mensaje", "Transaccion cancelada correctamente", "id", request.getId()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No se encontro una transaccion con ese id y referencia"));
    }


    @GetMapping("/transacciones")
    public ResponseEntity<Page<Transaccion>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(dir, sortBy));

        return ResponseEntity.ok(transaccionRepository.findAll(pageRequest));
    }
}
