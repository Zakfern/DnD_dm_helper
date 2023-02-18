package ru.relex.controller.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.relex.enums.KeyboardLayouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageUtils {
    public SendMessage generateSendMessageWithText(Update update, String text){
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;

    }

    public static SendMessage addInlineKeyBoardToMessage(SendMessage sendMessage, KeyboardLayouts keyboardLayouts) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //инлайн клавиатура
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (HashMap<String, String> variableName : keyboardLayouts.getKeyboardLayout()){
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            System.out.println(variableName.values());
            for(Map.Entry<String, String> entry : variableName.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(key);
                button.setCallbackData(value);
                keyboardButtonsRow.add(button);
            }
            rowList.add(keyboardButtonsRow);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
    public static EditMessageText addInlineKeyBoardEditMessage(EditMessageText editMessageText, KeyboardLayouts keyboardLayouts) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //инлайн клавиатура
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (HashMap<String, String> variableName : keyboardLayouts.getKeyboardLayout()){
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            System.out.println(variableName.values());
            for(Map.Entry<String, String> entry : variableName.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(key);
                button.setCallbackData(value);
                keyboardButtonsRow.add(button);
            }
            rowList.add(keyboardButtonsRow);
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        return editMessageText;
    }

}
