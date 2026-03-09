package com.btg.btg_funds.service.impl;

import com.btg.btg_funds.document.FundDocument;
import com.btg.btg_funds.repository.FundRepository;
import com.btg.btg_funds.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private final FundRepository fundRepository;

    @Override
    public List<FundDocument> obtenerFondosActivos() {
        return fundRepository.findByActiveTrue();
    }
}
