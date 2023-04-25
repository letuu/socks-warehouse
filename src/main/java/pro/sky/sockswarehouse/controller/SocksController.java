package pro.sky.sockswarehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.sockswarehouse.dto.SocksDto;
import pro.sky.sockswarehouse.entity.Socks;
import pro.sky.sockswarehouse.operation.ComparisonOperator;
import pro.sky.sockswarehouse.service.SocksService;

@RestController
@RequestMapping("/api/socks")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }


    @GetMapping
    @Operation(summary = "Returns the total number of socks - Возвращает общее количество носков на складе")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "*/*", schema = @Schema(implementation = Socks.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Query parameters are missing or are not in the correct format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error",
                    content = @Content()
            )
    })
    public ResponseEntity<String> getSocks(@RequestParam String color,
                                           @RequestParam ComparisonOperator comparisonOperator,
                                           @RequestParam int cottonPart) {
            return ResponseEntity.ok(socksService.getSocks(color, comparisonOperator, cottonPart));
    }


    @PostMapping("/income")
    @Operation(summary = "Income Socks - Приход носков на склад")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "*/*", schema = @Schema(implementation = Socks.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Query parameters are missing or are not in the correct format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error",
                    content = @Content()
            )
    })
    public ResponseEntity<Socks> incomeSocks(@RequestBody SocksDto socksDto) {
        return ResponseEntity.ok(socksService.incomeSocks(socksDto));
    }


    @PostMapping("/outcome")
    @Operation(summary = "Outcome Socks - Отпуск носков со склада")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "*/*", schema = @Schema(implementation = Socks.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Query parameters are missing or are not in the correct format",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Server error",
                    content = @Content()
            )
    })
    public ResponseEntity<Socks> outcomeSocks(@RequestBody SocksDto socksDto) {
        return ResponseEntity.ok(socksService.outcomeSocks(socksDto));
    }
}
