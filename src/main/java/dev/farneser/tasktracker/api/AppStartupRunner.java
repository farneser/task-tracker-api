package dev.farneser.tasktracker.api;

import dev.farneser.tasktracker.api.exceptions.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.DatagramSocket;
import java.net.InetAddress;

@Slf4j
@Component
public class AppStartupRunner implements ApplicationRunner {
    @Value("${server.port}")
    private Integer port;

    private String getAddress() throws InternalServerException {
        try (final var datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("1.1.1.1"), 80);

            return datagramSocket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            throw new InternalServerException("Remote address not found");
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        var servers = 2;
        var localMessage = "Server running local:\thttp://localhost:" + port;
        var remoteMessage = new StringBuilder();

        try {
            remoteMessage.append("Server running remote:\thttp://").append(getAddress()).append(":").append(port);
        } catch (InternalServerException e) {
            remoteMessage = new StringBuilder("Failed to run server remote");

            servers--;
        }

        var maxLength = Math.max(localMessage.length(), remoteMessage.length());

        log.info("-".repeat(maxLength / 2) + "STATUS" + "-".repeat(maxLength / 2));
        log.info("Available: " + servers + " urls");
        log.info(localMessage);
        log.info(remoteMessage.toString());
        log.info("-".repeat(maxLength + 6));
    }
}