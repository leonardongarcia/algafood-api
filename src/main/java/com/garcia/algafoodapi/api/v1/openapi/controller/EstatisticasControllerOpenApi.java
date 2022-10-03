package com.garcia.algafoodapi.api.v1.openapi.controller;

import com.garcia.algafoodapi.api.v1.controller.EstatisticasController;
import com.garcia.algafoodapi.domain.filter.VendaDiariaFilter;
import com.garcia.algafoodapi.domain.model.dto.VendaDiaria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Estatísticas")
@SecurityRequirement(name = "security_auth")
public interface EstatisticasControllerOpenApi {

  @Operation(hidden = true)
  EstatisticasController.EstatisticasModel estatisticas();

  @Operation(
      summary = "Consulta estatísticas de vendas diárias",
      parameters = {
        @Parameter(
            in = ParameterIn.QUERY,
            name = "restauranteId",
            description = "ID do restaurante para filtro da pesquisa",
            example = "1",
            schema = @Schema(type = "integer")),
        @Parameter(
            in = ParameterIn.QUERY,
            name = "dataCriacaoFim",
            description = "Data/hora da criação final para filtro da pesquisa",
            example = "2019-12-01T00:00:00Z",
            schema = @Schema(type = "string", format = "date-time")),
        @Parameter(
            in = ParameterIn.QUERY,
            name = "dataCriacaoInicio",
            description = "Data/hora da criação inicial para filtro da pesquisa",
            example = "2019-12-01T00:00:00Z",
            schema = @Schema(type = "string", format = "date-time"))
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = VendaDiaria.class))),
              @Content(
                  mediaType = "application/pdf",
                  schema = @Schema(type = "string", format = "binary"))
            })
      })
  List<VendaDiaria> consultarVendasDiarias(
      @Parameter(hidden = true) VendaDiariaFilter vendaDiariaFilter,
      @Parameter(
              description =
                  "Deslocamento de horário a ser considerado na consulta em relação ao UTC",
              schema = @Schema(type = "string", defaultValue = "+00:00"))
          String timeOffset);

  @Operation(hidden = true)
  ResponseEntity<byte[]> consultarVendasDiariasPdf(
      VendaDiariaFilter vendaDiariaFilter, String timeOffset);
}
