package org.ktb.chatbotbe.global.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ExceptionAlertAspect {
    private final DiscordClient discordClient;

    @Value("${spring.profiles.active}")
    private String serverType;

    public void sendAlert(String message) {
        if (serverType.equalsIgnoreCase("prod")) {
            discordClient.sendMessage(DiscordMessage.createErrorDiscordMessage(message));
        }
    }

    @AfterThrowing(
            pointcut = "@within(org.springframework.web.bind.annotation.RestController)",
            throwing = "e"
    )
    public void sendExceptionAlert(JoinPoint joinPoint, Exception e) {
        log.info("exception!!");
        String alertMessage =
                String.format(
                        "server-profile = `%s` 예외 발생! \n 클래스:` %s ` \n 메서드:` %s ` \n 메시지:` %s `",
                        serverType,
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        e.getMessage());
        sendAlert(alertMessage);
    }

}
