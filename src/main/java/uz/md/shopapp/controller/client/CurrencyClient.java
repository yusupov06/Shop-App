package uz.md.shopapp.controller.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import uz.md.shopapp.config.feign.FeignConfig;
import uz.md.shopapp.domain.Currency;

import java.time.LocalDateTime;

@FeignClient(name = "currencyClient", url = "https://cbu.uz/oz/arkhiv-kursov-valyut/json", configuration = FeignConfig.class)
public interface CurrencyClient {

    @RequestLine(value = "POST /{name}/{date}/")
    @Headers("Content-Type: application/json")
    String getByNameAndDate(@Param("name") String name,
                              @Param("date") String date);

}
