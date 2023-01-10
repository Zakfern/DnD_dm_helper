package ru.relex.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.controller.utils.MessageUtils;
import ru.relex.service.UpdateProducer;

import static ru.relex.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer ;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot bot_to_register){
        this.telegramBot = bot_to_register;
    }
    public void processUpdate(Update update){
        if (update == null){
            log.error("Received update is null");
            return;
        }
        if (update.hasMessage()){
            distributeMessageByType(update);
        } else {
            log.error("Received unsupported message type " + update);
        }
    }

    private void distributeMessageByType(Update update) {
        var message = update.getMessage();
        if (message.hasText()){
            processTextMessage(update);
        }
        else if (message.hasDocument()){
            processDocMessage(update);
        }
        else if (message.hasPhoto()){
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }

    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Не поддерживаемый тип данных");
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
    private void setFileReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Данные получены. Идет обработка...");
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE,update);
        setFileReceivedView(update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE,update);
        setFileReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE,update);
    }
}
