package com.exchange.currency_exchange_bot.bot;

import com.exchange.currency_exchange_bot.exception.ServiceException;
import com.exchange.currency_exchange_bot.service.CurrencyExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;


@Component
public class CurrencyExchangeBot extends TelegramLongPollingBot {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger(CurrencyExchangeBot.class);

    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String EUR = "/eur";
    private static final String UAH = "/uah";
    private static final String HELP = "/help";

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    public CurrencyExchangeBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case USD -> usdCommand(chatId);
            case EUR -> eurCommand(chatId);
            case UAH -> uahCommand(chatId);
            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return "phd_currency_bot";
    }


    private void startCommand(Long chatId, String userName) {
        var text = """
                Welcome to the bot, %s!
                               
                Here you can find information about the official exchange rates for today as set by the Central Bank of Russia.
                               
                To do this, use the following commands:
                /usd - USD exchange rate
                /eur - EUR exchange rate
                /uah - UAH exchange rate
                               
                Additional commands:
                /help - get assistance
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void usdCommand(Long chatId) {
        String formattedText;
        try {
            var usd = currencyExchangeService.getUSDExchangeRate();
            var text = "The exchange rate for the dollar on %s is %s rubles.";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("An error occurred while retrieving the dollar exchange rate.", e);
            formattedText = "Failed to retrieve the current dollar exchange rate. Please try again later.";
        }
        sendMessage(chatId, formattedText);
    }

    private void eurCommand(Long chatId) {
        String formattedText;
        try {
            var usd = currencyExchangeService.getEURExchangeRate();
            var text = "The exchange rate for the euro on %s is %s rubles.";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("An error occurred while retrieving the euro exchange rate.", e);
            formattedText = "Failed to retrieve the current euro exchange rate. Please try again later.";
        }
        sendMessage(chatId, formattedText);
    }

    private void uahCommand(Long chatId) {
        String formattedText;
        try {
            var usd = currencyExchangeService.getUAHExchangeRate();
            var text = "The exchange rate for the Ukrainian hryvnia on %s is %s rubles.";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("An error occurred while retrieving the Ukrainian hryvnia exchange rate.", e);
            formattedText = "Failed to retrieve the current Ukrainian hryvnia exchange rate. Please try again later.";
        }
        sendMessage(chatId, formattedText);
    }

    private void helpCommand(Long chatId) {
        var text = """
                Reference information about the bot:
                                
                To obtain current currency exchange rates, use the following commands:
                /usd - USD exchange rate
                /eur - EUR exchange rate
                /uah - UAH exchange rate
                """;
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId) {
        var text = "The command couldn't be recognized.";
        sendMessage(chatId, text);
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Message sending error.", e);
        }
    }
}
