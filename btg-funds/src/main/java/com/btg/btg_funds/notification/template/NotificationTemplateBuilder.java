package com.btg.btg_funds.notification.template;

import com.btg.btg_funds.document.ClientDocument;
import com.btg.btg_funds.document.FundDocument;
import org.springframework.stereotype.Component;
import com.btg.btg_funds.notification.*;

@Component
public class NotificationTemplateBuilder {

    public String buildPlainMessage(NotificationEventType eventType,
                                    ClientDocument client,
                                    FundDocument fund,
                                    Double amount) {
        return switch (eventType) {
            case SUBSCRIPTION -> buildSubscriptionPlainMessage(client, fund, amount);
            case CANCELLATION -> buildCancellationPlainMessage(client, fund, amount);
        };
    }

    public String buildHtmlMessage(NotificationEventType eventType,
                                   ClientDocument client,
                                   FundDocument fund,
                                   Double amount) {
        return switch (eventType) {
            case SUBSCRIPTION -> buildSubscriptionHtmlMessage(client, fund, amount);
            case CANCELLATION -> buildCancellationHtmlMessage(client, fund, amount);
        };
    }

    public String buildSubject(NotificationEventType eventType) {
        return switch (eventType) {
            case SUBSCRIPTION -> "Confirmación de suscripción";
            case CANCELLATION -> "Confirmación de cancelación";
        };
    }

    public String buildSubscriptionPlainMessage(ClientDocument client, FundDocument fund, Double amount) {
        return "Hola " + client.getName() + ", te has suscrito al fondo "
                + fund.getName() + " por un valor de COP " + amount + ".";
    }

    public String buildCancellationPlainMessage(ClientDocument client, FundDocument fund, Double amount) {
        return "Hola " + client.getName() + ", has cancelado tu suscripción al fondo "
                + fund.getName() + ". Se reintegraron COP " + amount + " a tu saldo.";
    }

    public String buildSubscriptionHtmlMessage(ClientDocument client, FundDocument fund, Double amount) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Confirmación de suscripción</title>
                </head>
                <body style="margin:0;padding:0;background-color:#f4f7fb;font-family:Arial,sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f7fb;padding:24px 0;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 2px 10px rgba(0,0,0,0.08);">
                                    <tr>
                                        <td style="background:#0f172a;padding:24px;text-align:center;color:#ffffff;">
                                            <h1 style="margin:0;font-size:24px;">BTG Funds</h1>
                                            <p style="margin:8px 0 0 0;font-size:14px;">Confirmación de suscripción</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:32px;">
                                            <p style="margin:0 0 16px 0;font-size:16px;color:#111827;">
                                                Hola <strong>%s</strong>,
                                            </p>
                                            <p style="margin:0 0 16px 0;font-size:15px;color:#374151;line-height:1.6;">
                                                Tu suscripción fue procesada exitosamente.
                                            </p>
                                            <table width="100%%" cellpadding="0" cellspacing="0" style="margin:24px 0;background:#f9fafb;border:1px solid #e5e7eb;border-radius:8px;">
                                                <tr>
                                                    <td style="padding:16px;">
                                                        <p style="margin:0 0 8px 0;font-size:14px;color:#6b7280;">Fondo</p>
                                                        <p style="margin:0 0 16px 0;font-size:16px;color:#111827;"><strong>%s</strong></p>
                                                        <p style="margin:0 0 8px 0;font-size:14px;color:#6b7280;">Valor suscrito</p>
                                                        <p style="margin:0;font-size:18px;color:#059669;"><strong>COP %,.0f</strong></p>
                                                    </td>
                                                </tr>
                                            </table>
                                            <p style="margin:0 0 16px 0;font-size:14px;color:#4b5563;line-height:1.6;">
                                                Gracias por confiar en nuestra plataforma para la gestión de tus fondos.
                                            </p>
                                            <p style="margin:24px 0 0 0;font-size:13px;color:#9ca3af;">
                                                Este es un mensaje automático. Por favor no respondas a este correo.
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(client.getName(), fund.getName(), amount);
    }

    public String buildCancellationHtmlMessage(ClientDocument client, FundDocument fund, Double amount) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Cancelación de suscripción</title>
                </head>
                <body style="margin:0;padding:0;background-color:#f4f7fb;font-family:Arial,sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f7fb;padding:24px 0;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 2px 10px rgba(0,0,0,0.08);">
                                    <tr>
                                        <td style="background:#7c2d12;padding:24px;text-align:center;color:#ffffff;">
                                            <h1 style="margin:0;font-size:24px;">BTG Funds</h1>
                                            <p style="margin:8px 0 0 0;font-size:14px;">Cancelación de suscripción</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:32px;">
                                            <p style="margin:0 0 16px 0;font-size:16px;color:#111827;">
                                                Hola <strong>%s</strong>,
                                            </p>
                                            <p style="margin:0 0 16px 0;font-size:15px;color:#374151;line-height:1.6;">
                                                Tu cancelación fue procesada correctamente.
                                            </p>
                                            <table width="100%%" cellpadding="0" cellspacing="0" style="margin:24px 0;background:#f9fafb;border:1px solid #e5e7eb;border-radius:8px;">
                                                <tr>
                                                    <td style="padding:16px;">
                                                        <p style="margin:0 0 8px 0;font-size:14px;color:#6b7280;">Fondo</p>
                                                        <p style="margin:0 0 16px 0;font-size:16px;color:#111827;"><strong>%s</strong></p>
                                                        <p style="margin:0 0 8px 0;font-size:14px;color:#6b7280;">Valor reintegrado</p>
                                                        <p style="margin:0;font-size:18px;color:#2563eb;"><strong>COP %,.0f</strong></p>
                                                    </td>
                                                </tr>
                                            </table>
                                            <p style="margin:24px 0 0 0;font-size:13px;color:#9ca3af;">
                                                Este es un mensaje automático. Por favor no respondas a este correo.
                                            </p>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(client.getName(), fund.getName(), amount);
    }
}