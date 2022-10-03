package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.filter.VendaDiariaFilter;

public interface VendaReportService {

  byte[] emitirVendasDiarias(VendaDiariaFilter vendaDiariaFilter, String timeOffset);
}
