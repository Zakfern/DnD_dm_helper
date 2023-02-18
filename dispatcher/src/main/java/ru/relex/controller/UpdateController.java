package ru.relex.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.relex.controller.utils.MessageUtils;
import ru.relex.enums.KeyboardLayouts;
import ru.relex.service.AnswerConsumer;
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
        if (update.hasMessage()) {
            distributeMessageByType(update);
        }
        else if (update.hasCallbackQuery()) {
            distributeMessageByUpdate(update);
        }
        else {
            log.error("Received unsupported message type " + update);
        }
    }

    private void distributeMessageByType(Update update) {
        var message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        Long chat_id = update.getMessage().getChatId();
        sendMessage.setChatId(chat_id);
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }
    private void distributeMessageByUpdate(Update update) {
        String call_data = update.getCallbackQuery().getData();
        Integer message_id = update.getCallbackQuery().getMessage().getMessageId();
        long chat_id = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chat_id);
        editMessageText.setMessageId(message_id);
        String answer = "Updated message text";
        editMessageText.setText(answer);
        switch (call_data) {
            case "get_to_menu":
                doSendUpdate(messageUtils.addInlineKeyBoardEditMessage(editMessageText, KeyboardLayouts.START_LAYOUT));
                break;
            case "get_random_trap":
                doSendUpdate(messageUtils.addInlineKeyBoardEditMessage(editMessageText, KeyboardLayouts.TRAPS_LAYOUT));
                break;
            case "get_next_trap":
                System.out.println("Fuuck");
                break;
            default:
                System.out.println("Oooops, something wrong !");
        }
    }
    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Не поддерживаемый тип данных");
        doSendMessage(sendMessage, update);
    }
    public void doSendUpdate(EditMessageText editMessageText) {
        telegramBot.sendEditMessage(editMessageText);


    }
    public void doSendMessage(SendMessage sendMessage, Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
        //answerConsumer.consume(sendMessage, update);
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
        System.out.println("processTextMessage");
        updateProducer.produce(TEXT_MESSAGE_UPDATE,update);
        Long chat_id = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat_id);
        sendMessage.setText("Выберите действие из наблора возможных");
        doSendMessage(messageUtils.addInlineKeyBoardToMessage(sendMessage, KeyboardLayouts.START_LAYOUT), update);

    }
}
