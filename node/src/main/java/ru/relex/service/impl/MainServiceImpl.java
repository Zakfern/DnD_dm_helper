package ru.relex.service.impl;

import lombok.extern.log4j.Log4j;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.relex.dao.AppUserDAO;
import ru.relex.dao.RawDataDAO;
import ru.relex.entity.AppUser;
import ru.relex.entity.RawData;
import ru.relex.entity.enums.UserState;
import ru.relex.service.MainService;
import ru.relex.service.ProducerService;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Log4j
@Service
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;

    public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService, AppUserDAO appUserDAO) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        log.debug("NODE : Text message in MAINS SERVICE");
        var textMessage = update.getMessage();
        var telegramUser = textMessage.getFrom();
        var appUser = findOeSaveAppUser(telegramUser);

        //TODO реализовать ключевые рандомности
        //var message = update.getMessage();
        var sendMessage = new SendMessage();
        System.out.println(appUser.getId());
        System.out.println(textMessage.getChatId().toString());
//        sendMessage.setChatId(appUser.getTelegramUserId());
//        //sendMessage.setChatId(textMessage.getChatId().toString());
//        sendMessage.setText(appUser.getFirstName() + " "
//                + appUser.getLastName() + ", "
//                + "Hello from NODE");
//        producerService.producerAnswer(sendMessage);

    }

    private AppUser findOeSaveAppUser(User telegramUser) {
        //persistent    --юзер уже найден в базе
        //transient     --юзер который будет записан в базу
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .userName(telegramUser.getUserName())
                    //TODO .email()
                    // TODO изменить значение по умолчанию после завершения регистрации подтв почты
                    .isActive(true)
                    .state(UserState.BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return (persistentAppUser);
    }
    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData );
    }
}
