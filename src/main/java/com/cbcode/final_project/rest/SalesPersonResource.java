package com.cbcode.final_project.rest;

import com.cbcode.final_project.model.SalesPersonDTO;
import com.cbcode.final_project.service.SalesPersonService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/salesPersons", produces = MediaType.APPLICATION_JSON_VALUE)
public class SalesPersonResource {

    private final SalesPersonService salesPersonService;

    public SalesPersonResource(final SalesPersonService salesPersonService) {
        this.salesPersonService = salesPersonService;
    }

    @GetMapping
    public ResponseEntity<List<SalesPersonDTO>> getAllSalesPersons() {
        return ResponseEntity.ok(salesPersonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPersonDTO> getSalesPerson(@PathVariable final Long id) {
        return ResponseEntity.ok(salesPersonService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSalesPerson(
            @RequestBody @Valid final SalesPersonDTO salesPersonDTO) {
        return new ResponseEntity<>(salesPersonService.create(salesPersonDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSalesPerson(@PathVariable final Long id,
            @RequestBody @Valid final SalesPersonDTO salesPersonDTO) {
        salesPersonService.update(id, salesPersonDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSalesPerson(@PathVariable final Long id) {
        salesPersonService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
