package uz.md.shopapp.service.contract;

import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.CurrencyResult;
import uz.md.shopapp.dtos.request.CurrencyRequest;

public interface CurrencyService  {
    ApiResult<CurrencyResult> getCurrency(CurrencyRequest request);
}
