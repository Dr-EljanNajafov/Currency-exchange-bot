package com.exchange.currency_exchange_bot.configuration;

import com.exchange.currency_exchange_bot.bot.CurrencyExchangeBot;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class CurrencyExchangeBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(CurrencyExchangeBot currencyExchangeBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(currencyExchangeBot);
        return api;
    }

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient();
    }
}
