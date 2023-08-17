package com.exchange.currency_exchange_bot.client;

import com.exchange.currency_exchange_bot.exception.ServiceException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CBRClient {

    @Autowired
    private OkHttpClient client;

    @Value("${XXX.XXX.XXX}")
    private String url;

    public String getCurrencyRatesXML() throws ServiceException {
        var request = new Request.Builder()
                .url(url)
                .build();

        try (var response = client.newCall(request).execute();) {
            var body = response.body();
            return body == null ? null :body.string();
        } catch (IOException e) {
            throw new ServiceException("Error receiving currencies", e);
        }
    }
}
