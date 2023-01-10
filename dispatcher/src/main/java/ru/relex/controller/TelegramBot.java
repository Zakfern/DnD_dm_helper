package ru.relex.controller;

//import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    private UpdateController updateController;

    public TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostConstruct
    public void init(){
        updateController.registerBot(this);
    };

    //private static final Logger log = Logger.getLogger(TelegramBot.class);//используем ламбук вместо создания логера

    @Override
    public String getBotUsername() {
        return botName;
    }

//    @Override
//    public void onRegister() {
//        super.onRegister();
//    }

    @Override
    public String getBotToken() {
        return botToken;
    }


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("UPD received");
       // var originalMessage = update.getMessage();
//        var response = new SendMessage();
//        response.setChatId(originalMessage.getChatId().toString());
//        response.setText("лошара");
//        System.out.println(originalMessage.getText());
//        log.debug(originalMessage.getText());
//        sendAnswerMessage(response);
        updateController.processUpdate(update);

    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null){
            try{
                execute(message);
            } catch (TelegramApiException e){
                log.error(e);
            }
        }
    }

}
