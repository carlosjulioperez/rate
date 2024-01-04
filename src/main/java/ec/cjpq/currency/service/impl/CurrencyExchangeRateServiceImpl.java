package ec.cjpq.currency.service.impl;

import ec.cjpq.currency.model.CurrencyExchangeRate;
import ec.cjpq.currency.repository.CurrencyExchangeRateRepository;
import ec.cjpq.currency.service.CurrencyExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyExchangeRateServiceImpl implements CurrencyExchangeRateService {

    private final CurrencyExchangeRateRepository rateRepository;

    @Autowired
    public CurrencyExchangeRateServiceImpl(CurrencyExchangeRateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @Override
    public CurrencyExchangeRate add(CurrencyExchangeRate currencyExchangeRate) {
        return rateRepository.save(currencyExchangeRate);
    }

    @Override
    public Optional<CurrencyExchangeRate> update(Long id, CurrencyExchangeRate currencyExchangeRate) {
        if (rateRepository.existsById(id)) {
            currencyExchangeRate.setId(id);
            return Optional.of(rateRepository.save(currencyExchangeRate));
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        rateRepository.deleteById(id);
    }

    @Override
    public Optional<CurrencyExchangeRate> get(String sourceCurrency, String targetCurrency, LocalDate date) {
        return rateRepository.findBySourceCurrencyAndTargetCurrencyAndEffectiveStartDate(
                sourceCurrency, targetCurrency, date);
    }

    @Override
    public Optional<CurrencyExchangeRate> getClosest(String sourceCurrency, String targetCurrency, LocalDate date) {
        List<CurrencyExchangeRate> exchangeRates = rateRepository
                .findBySourceCurrencyAndTargetCurrencyAndEffectiveStartDateLessThanEqualOrderByEffectiveStartDateDesc(
                        sourceCurrency, targetCurrency, date);

        if (!exchangeRates.isEmpty())
            return Optional.ofNullable(exchangeRates.get(0));
        else
            throw new RuntimeException("No exchange rate found for the given currencies and date.");

    }

    @Override
    public List<CurrencyExchangeRate> getAll() {
        return rateRepository.findAll();
    }

    public BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency) {
        // Attempt direct conversion first
        BigDecimal directRate = getDirectRate(sourceCurrency, targetCurrency);
        if (directRate != null) {
            return directRate;
        }

        // Attempt triangular conversion
        return getTriangularRate(sourceCurrency, targetCurrency);
    }

    private BigDecimal getDirectRate(String sourceCurrency, String targetCurrency) {
        LocalDate today = LocalDate.now();
        List<CurrencyExchangeRate> rates = rateRepository.findBySourceCurrencyAndTargetCurrencyAndEffectiveStartDateLessThanEqualOrderByEffectiveStartDateDesc(
                sourceCurrency, targetCurrency, today
        );
        return rates.isEmpty() ? null : rates.get(0).getExchangeRate();
    }

    private BigDecimal getTriangularRate(String sourceCurrency, String targetCurrency) {
        for (String intermediateCurrency : rateRepository.findAllDistinctTargetCurrency()) {
            BigDecimal rate1 = getDirectRate(sourceCurrency, intermediateCurrency);
            BigDecimal rate2 = getDirectRate(intermediateCurrency, targetCurrency);
            if (rate1 != null && rate2 != null) {
                return rate1.multiply(rate2);
            }
        }
        return null;
    }
}
