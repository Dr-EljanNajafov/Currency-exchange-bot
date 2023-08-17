package com.exchange.currency_exchange_bot.service.impl;

import com.exchange.currency_exchange_bot.client.CBRClient;
import com.exchange.currency_exchange_bot.exception.ServiceException;
import com.exchange.currency_exchange_bot.service.CurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private static final String USD_XPATH = "XXX/XXX/XXX";
    private static final String EUR_XPATH = "XXX/XXX/XXX";
    private static final String UAH_XPATH = "XXX/XXX/XXX";

    @Autowired
    private CBRClient client;

    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, USD_XPATH);
    }

    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, EUR_XPATH);
    }

    @Override
    public String getUAHExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXML(xml, UAH_XPATH);
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
