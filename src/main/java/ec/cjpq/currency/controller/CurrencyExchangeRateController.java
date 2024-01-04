package ec.cjpq.currency.controller;

import ec.cjpq.currency.model.CurrencyExchangeRate;
import ec.cjpq.currency.service.impl.CurrencyExchangeRateServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exchange-rates")
public class CurrencyExchangeRateController {
    private final CurrencyExchangeRateServiceImpl service;

    public CurrencyExchangeRateController(CurrencyExchangeRateServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<CurrencyExchangeRate> add(@RequestBody CurrencyExchangeRate rate) {
        return new ResponseEntity<>(service.add(rate), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CurrencyExchangeRate> update(@PathVariable("id") Long id, @RequestBody CurrencyExchangeRate rate) {
        Optional<CurrencyExchangeRate> updatedRate = service.update(id, rate);
        return updatedRate.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get/{source}/{target}/{date}")
    public ResponseEntity<CurrencyExchangeRate> get(
            @PathVariable("source") String sourceCurrency,
            @PathVariable("target") String targetCurrency,
            @PathVariable("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        Optional<CurrencyExchangeRate> exchangeRate = service.get(sourceCurrency, targetCurrency, localDate);
        return exchangeRate.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/getClosest/{source}/{target}/{date}")
    public ResponseEntity<CurrencyExchangeRate> getClosest(
            @PathVariable("source") String sourceCurrency,
            @PathVariable("target") String targetCurrency,
            @PathVariable("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        Optional<CurrencyExchangeRate> exchangeRate = service.getClosest(sourceCurrency, targetCurrency, localDate);
        return exchangeRate.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getExchangeRate/{source}/{target}")
    public BigDecimal getExchangeRate(@PathVariable("source") String sourceCurrency,
                                      @PathVariable("target") String targetCurrency) {
        return service.getExchangeRate(sourceCurrency, targetCurrency);
    }

    @GetMapping("/getAll")
    List<CurrencyExchangeRate> getAll() {
        return service.getAll();
    }

}
