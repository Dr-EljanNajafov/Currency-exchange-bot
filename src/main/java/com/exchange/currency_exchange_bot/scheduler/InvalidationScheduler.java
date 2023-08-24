package com.exchange.currency_exchange_bot.scheduler;

import com.exchange.currency_exchange_bot.service.CurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvalidationScheduler {

    @Autowired
    private CurrencyExchangeService service;

    @Scheduled(cron = "* 0 0 * * ?")
    public void invalidateCache() {
        service.clearUSDCache();
        service.clearEURCache();
        service.clearUAHCache();
    }

}
