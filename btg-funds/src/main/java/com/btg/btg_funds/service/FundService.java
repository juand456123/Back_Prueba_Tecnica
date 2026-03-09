package com.btg.btg_funds.service;


import com.btg.btg_funds.document.FundDocument;

import java.util.List;

public interface FundService {

    List<FundDocument> obtenerFondosActivos();
}