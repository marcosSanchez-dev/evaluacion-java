package com.evaluacion.backend.service;

import com.evaluacion.backend.dto.TransaccionRequest;
import com.evaluacion.backend.dto.TransaccionResponse;
import com.evaluacion.backend.model.Transaccion;
import com.evaluacion.backend.repository.TransaccionRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final SecureRandom random = new SecureRandom();

    public TransaccionService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    public TransaccionResponse guardarTransaccion(TransaccionRequest request) {
        // Genera una referencia numerica aleatoria de 6 digitos (000000-999999)
        String referencia = String.format("%06d", random.nextInt(1_000_000));

        Transaccion transaccion = new Transaccion(
                request.getOperacion(),
                request.getImporte(),
                request.getCliente(),
                referencia,
                "Aprobada",
                request.getSecreto() // ya llega descifrado desde la API 1
        );

        Transaccion guardada = transaccionRepository.save(transaccion);

        return new TransaccionResponse(
                String.valueOf(guardada.getId()),
                guardada.getEstatus(),
                guardada.getReferencia(),
                guardada.getOperacion()
        );
    }

    public boolean cancelarTransaccion(Long id, String referencia) {
        int filasActualizadas = transaccionRepository.cancelarTransaccion(id, referencia);
        return filasActualizadas > 0;
    }
}
