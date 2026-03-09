package com.btg.btg_funds.service.impl;

import com.btg.btg_funds.entity.ClienteEntity;
import com.btg.btg_funds.repository.ClienteRepository;
import com.btg.btg_funds.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<ClienteEntity> obtenerClientesConProductosDisponiblesEnSucursalesQueVisitan() {
        return clienteRepository.findClientesConProductosDisponiblesEnSucursalesQueVisitan();
    }
}
