package com.exchange.currency_exchange_bot.service.impl;

import com.exchange.currency_exchange_bot.client.CBRClient;
import com.exchange.currency_exchange_bot.exception.ServiceException;
import com.exchange.currency_exchange_bot.service.CurrencyExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyExchangeServiceImpl.class);

    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";
    private static final String UAH_XPATH = "/ValCurs//Valute[@ID='R01720']/Value";

    @Autowired
    private CBRClient client;

    @Cacheable(value = "usd", unless = "#result==null or #result.isEmpty()")
    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xmlOptional = client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Not able to get XML")
        );
        return extractCurrencyValueFromXML(xml, USD_XPATH);
    }

    @Cacheable(value = "eur", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xmlOptional = client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Not able to get XML")
        );
        return extractCurrencyValueFromXML(xml, EUR_XPATH);
    }

    @Cacheable(value = "uah", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getUAHExchangeRate() throws ServiceException {
        var xmlOptional = client.getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new ServiceException("Not able to get XML")
        );
        return extractCurrencyValueFromXML(xml, UAH_XPATH);
    }


    @CacheEvict("usd")
    @Override
    public void clearUSDCache() {
        LOG.info("Cache \"usd\" cleared!");
    }

    @CacheEvict("eur")
    @Override
    public void clearEURCache() {
        LOG.info("Cache \"eur\" cleared!");
    }

    @CacheEvict("uah")
    @Override
    public void clearUAHCache() {
        LOG.info("Cache \"uah\" cleared!");
    }

    private static String extractCurrencyValueFromXML(String xml, String xpathException) throws ServiceException {
        var source = new InputSource(new StringReader(xml));
            try {
                var xpath = XPathFactory.newInstance().newXPath();
                var document = xpath.evaluate("/", source, XPathConstants.NODE);

                return xpath.evaluate(xpathException, document);
            } catch (XPathExpressionException e) {
                throw new ServiceException("Failed to parse XML", e);
            }

    }
}
