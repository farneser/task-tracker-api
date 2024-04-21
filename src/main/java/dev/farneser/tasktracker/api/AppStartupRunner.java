package dev.farneser.tasktracker.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
public class AppStartupRunner implements ApplicationRunner {
    @Value("${server.port}")
    private Integer port;

    public List<String> getNetworkInfo(int port) {
        ArrayList<String> result = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            List<NetworkInterface> interfaceList = Collections.list(networkInterfaces);

            interfaceList.sort((ni1, ni2) -> ni1.getName().compareToIgnoreCase(ni2.getName()));

            for (NetworkInterface networkInterface : interfaceList) {
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

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
        List<String> remoteAddresses = getNetworkInfo(port);

        String localMessage = "Server running local:\thttp://localhost:" + port;

        List<String> remoteMessages = new ArrayList<>();
        remoteAddresses.forEach(address -> remoteMessages.add("Server running remote:\t" + address));

        int servers = 1 + remoteAddresses.size();
        int maxLength = Math.max(localMessage.length(), remoteMessages.stream().mapToInt(String::length).max().orElse(0));

        log.info("=".repeat(maxLength / 2 - 1) + " STATUS " + "=".repeat(maxLength / 2 - 1));
        log.info("Available: " + servers + " urls");
        log.info(localMessage);
        remoteMessages.forEach(log::info);
        log.info("=".repeat(maxLength + 6));
    }
}