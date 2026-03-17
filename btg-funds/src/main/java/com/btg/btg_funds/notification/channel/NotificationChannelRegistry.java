package com.btg.btg_funds.notification.channel;

import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.notification.model.NotificationChannelType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationChannelRegistry {

    private final Map<NotificationChannelType, NotificationChannel> channels;

    public NotificationChannelRegistry(List<NotificationChannel> channelList) {
        this.channels = channelList.stream()
                .collect(Collectors.toMap(
                        NotificationChannel::getChannelType,
                        Function.identity()
                ));
    }

    public NotificationChannel getChannel(NotificationChannelType channelType) {
        if (channelType == null) {
            throw new BusinessException("El tipo de canal es obligatorio");
        }

        NotificationChannel channel = channels.get(channelType);

        if (channel == null) {
            throw new BusinessException("No existe implementación para el canal " + channelType);
        }

        return channel;
    }

    public NotificationChannel getChannel(String preference) {
        if (preference == null || preference.isBlank()) {
            throw new BusinessException("El cliente no tiene preferencia de notificación configurada");
        }

        try {
            NotificationChannelType channelType = NotificationChannelType.valueOf(preference.toUpperCase());
            return getChannel(channelType);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Preferencia de notificación no válida para el cliente");
        }
    }
}
