package com.btg.btg_funds.notification.provider;

import com.btg.btg_funds.exception.BusinessException;
import com.btg.btg_funds.notification.model.NotificationChannelType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationProviderRegistry {

    private final Map<NotificationChannelType, Map<String, NotificationProvider>> providers;

    public NotificationProviderRegistry(List<NotificationProvider> providerList) {
        this.providers = providerList.stream()
                .collect(Collectors.groupingBy(
                        NotificationProvider::getChannelType,
                        Collectors.toMap(
                                provider -> provider.getProviderName().toUpperCase(),
                                Function.identity()
                        )
                ));
    }

    public NotificationProvider getProvider(NotificationChannelType channelType, String providerName) {
        if (channelType == null) {
            throw new BusinessException("El tipo de canal es obligatorio");
        }

        if (providerName == null || providerName.isBlank()) {
            throw new BusinessException("El nombre del provider es obligatorio");
        }

        Map<String, NotificationProvider> channelProviders = providers.get(channelType);

        if (channelProviders == null || channelProviders.isEmpty()) {
            throw new BusinessException("No hay providers registrados para el canal " + channelType);
        }

        NotificationProvider provider = channelProviders.get(providerName.toUpperCase());

        if (provider == null) {
            throw new BusinessException(
                    "No existe un provider configurado con nombre " + providerName + " para el canal " + channelType
            );
        }

        return provider;
    }
}
