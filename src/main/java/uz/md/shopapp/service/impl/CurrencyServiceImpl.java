package uz.md.shopapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.md.shopapp.controller.client.CurrencyClient;
import uz.md.shopapp.domain.Currency;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.CurrencyResult;
import uz.md.shopapp.dtos.request.CurrencyRequest;
import uz.md.shopapp.service.contract.CurrencyService;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyClient currencyClient;
    private final ObjectMapper objectMapper;

    @Override
    public ApiResult<CurrencyResult> getCurrency(CurrencyRequest request) {
        try {
            String str = currencyClient.getByNameAndDate(request.getCurrency(), request.getDateTime());
            Currency currency = objectMapper.readValue(str, Currency[].class)[0];
            Double rate = currency.getRate();
            Double total = request.getAmount() * rate;

            return ApiResult.successResponse(
                    new CurrencyResult(request, currency, total)
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
