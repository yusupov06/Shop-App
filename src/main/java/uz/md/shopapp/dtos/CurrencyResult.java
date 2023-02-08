package uz.md.shopapp.dtos;

import lombok.*;
import uz.md.shopapp.domain.Currency;
import uz.md.shopapp.dtos.request.CurrencyRequest;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CurrencyResult {
    private CurrencyRequest currencyRequest;
    private Currency currency;
    private Double totalResult;
}
