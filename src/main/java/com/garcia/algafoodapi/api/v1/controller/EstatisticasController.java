package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.AlgaLinks;
import com.garcia.algafoodapi.api.v1.openapi.controller.EstatisticasControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.filter.VendaDiariaFilter;
import com.garcia.algafoodapi.domain.model.dto.VendaDiaria;
import com.garcia.algafoodapi.domain.service.VendaQueryService;
import com.garcia.algafoodapi.domain.service.VendaReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/estatisticas")
public class EstatisticasController implements EstatisticasControllerOpenApi {

  @Autowired private VendaQueryService vendaQueryService;

  @Autowired private VendaReportService vendaReportService;

  @Autowired private AlgaLinks algaLinks;

  @CheckSecurity.Estatisticas.PodeConsultar
  @Override
  @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<VendaDiaria> consultarVendasDiarias(
      VendaDiariaFilter vendaDiariaFilter,
      @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
    return vendaQueryService.consultarVendasDiarias(vendaDiariaFilter, timeOffset);
  }

  @CheckSecurity.Estatisticas.PodeConsultar
  @Override
  @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> consultarVendasDiariasPdf(
      VendaDiariaFilter vendaDiariaFilter,
      @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
    byte[] bytesPdf = vendaReportService.emitirVendasDiarias(vendaDiariaFilter, timeOffset);

    var headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .headers(headers)
        .body(bytesPdf);
  }

  @CheckSecurity.Estatisticas.PodeConsultar
  @Override
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public EstatisticasModel estatisticas() {
    var estatisticasModel = new EstatisticasModel();

    estatisticasModel.add(algaLinks.linkToEstatisticasVendasDiarias("vendas-diarias"));

    return estatisticasModel;
  }

  public static class EstatisticasModel extends RepresentationModel<EstatisticasModel> {}
}
