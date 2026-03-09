package com.btg.btg_funds.service;

import com.btg.btg_funds.entity.ClienteEntity;

import java.util.List;

public interface ClienteService {

    List<ClienteEntity> obtenerClientesConProductosDisponiblesEnSucursalesQueVisitan();

}
