package uz.md.shopapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.CurrencyResult;
import uz.md.shopapp.dtos.request.CurrencyRequest;
import uz.md.shopapp.service.contract.CurrencyService;
import uz.md.shopapp.utils.AppConstants;

@RestController
@RequestMapping(CurrencyClientController.BASE_URL + "/")
@RequiredArgsConstructor
@Slf4j
public class CurrencyClientController {

    public static final String BASE_URL = AppConstants.BASE_URL + "currency";

    private final CurrencyService currencyService;

    @PostMapping
    public ApiResult<CurrencyResult> getCurrency(@RequestBody @Valid CurrencyRequest request){
        log.info("Get Currency client api call");
        return currencyService.getCurrency(request);
    }

}
