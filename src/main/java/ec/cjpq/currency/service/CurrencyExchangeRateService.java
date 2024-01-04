package ec.cjpq.currency.service;

import ec.cjpq.currency.model.CurrencyExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CurrencyExchangeRateService {
    CurrencyExchangeRate add(CurrencyExchangeRate currencyExchangeRate);
    Optional<CurrencyExchangeRate> update(Long id, CurrencyExchangeRate currencyExchangeRate);
    void delete(Long id);
    Optional<CurrencyExchangeRate> get(String sourceCurrency, String targetCurrency, LocalDate date);
    Optional<CurrencyExchangeRate> getClosest(String sourceCurrency, String targetCurrency, LocalDate date);
    List<CurrencyExchangeRate> getAll();
    public BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency);
}
