package com.exchange.currency_exchange_bot.service;

import com.exchange.currency_exchange_bot.exception.ServiceException;

public interface CurrencyExchangeService {

    String getUSDExchangeRate() throws ServiceException;

    String getEURExchangeRate() throws ServiceException;

    String getUAHExchangeRate() throws ServiceException;

    void clearUSDCache();
    void clearEURCache();
    void clearUAHCache();
}
