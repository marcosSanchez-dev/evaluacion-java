package com.evaluacion.gateway.service;

import com.evaluacion.gateway.dto.TransaccionInternaRequest;
import com.evaluacion.gateway.dto.TransaccionRequest;
import com.evaluacion.gateway.dto.TransaccionResponse;
import com.evaluacion.gateway.util.AesEncryptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransaccionService {

    private final RestTemplate restTemplate;

    @Value("${api.backend.url}")
    private String apiBackendUrl;

    public TransaccionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TransaccionResponse procesarTransaccion(TransaccionRequest request) {
        String secretoDescifrado = AesEncryptionUtil.decrypt(request.getSecreto());

        TransaccionInternaRequest internalRequest = new TransaccionInternaRequest(
                request.getOperacion(),
                request.getImporte(),
                request.getCliente(),
                secretoDescifrado
        );

        return restTemplate.postForObject(apiBackendUrl + "/api/backend/transacciones", internalRequest, TransaccionResponse.class);
    }
}
