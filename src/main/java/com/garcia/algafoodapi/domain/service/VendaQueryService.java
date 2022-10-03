package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.filter.VendaDiariaFilter;
import com.garcia.algafoodapi.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {

  List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter vendaDiariaFilter, String timeOffset);
}
