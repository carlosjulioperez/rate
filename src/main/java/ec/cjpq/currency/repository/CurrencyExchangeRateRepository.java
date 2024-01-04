package ec.cjpq.currency.repository;

import ec.cjpq.currency.model.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, Long> {
    Optional<CurrencyExchangeRate> findBySourceCurrencyAndTargetCurrencyAndEffectiveStartDate(
            String sourceCurrency, String targetCurrency, LocalDate effectiveStartDate);
    List<CurrencyExchangeRate> findBySourceCurrencyAndTargetCurrencyAndEffectiveStartDateLessThanEqualOrderByEffectiveStartDateDesc(
            String sourceCurrency, String targetCurrency, LocalDate effectiveStartDate);
    @Query("SELECT DISTINCT c.targetCurrency FROM CurrencyExchangeRate c")
    List<String> findAllDistinctTargetCurrency();
}
