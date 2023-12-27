package dev.farneser.tasktracker.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class AppStartupRunner implements ApplicationRunner {
    @Value("${server.port}")
    private Integer port;

    public List<String> getNetworkInfo(int port) {
        var result = new ArrayList<String>();

        try {
            var networkInterfaces = NetworkInterface.getNetworkInterfaces();

            var interfaceList = Collections.list(networkInterfaces);

            interfaceList.sort((ni1, ni2) -> ni1.getName().compareToIgnoreCase(ni2.getName()));

            for (var networkInterface : interfaceList) {
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }

                var addresses = networkInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    var address = addresses.nextElement();

                    if (address instanceof Inet4Address) {
                        result.add("http://" + address.getHostAddress() + ":" + port + "/");
                    }
                }
            }
        } catch (SocketException e) {
            log.error("Failed to get network interfaces", e);
        }

        return result;
    }

    @Override
    public void run(ApplicationArguments args) {
        var remoteAddresses = getNetworkInfo(port);

        var localMessage = "Server running local:\thttp://localhost:" + port;

        var remoteMessages = new ArrayList<String>();
        remoteAddresses.forEach(address -> remoteMessages.add("Server running remote:\t" + address));

        var servers = 1 + remoteAddresses.size();
        var maxLength = Math.max(localMessage.length(), remoteMessages.stream().mapToInt(String::length).max().orElse(0));

        log.info("=".repeat(maxLength / 2 - 1) + " STATUS " + "=".repeat(maxLength / 2 - 1));
        log.info("Available: " + servers + " urls");
        log.info(localMessage);
        remoteMessages.forEach(log::info);
        log.info("=".repeat(maxLength + 6));
    }
}