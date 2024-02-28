package com.adrian.library.config;

import org.ini4j.Ini;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Map;


import static java.util.stream.Collectors.toMap;

@Configuration
public class Constants {

    public static final double firstChargeAmount;
    public static final double subsequentChargeAmount;
    public static final String email;
    public static final int actionPoints;

    static {
        Map<String, Map<String, String>> map;
        try {
             map = parseIniFile(new File("src/main/resources/constants.ini"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        firstChargeAmount = Double.parseDouble(map.get("charge").get("firstChargeAmount"));
        subsequentChargeAmount = Double.parseDouble(map.get("charge").get("subsequentChargeAmount"));
        email = map.get("email").get("email");
        actionPoints = Integer.parseInt(map.get("user").get("actionPoints"));
    }

    public static Map<String, Map<String, String>> parseIniFile(File fileToParse) throws IOException {
        Ini ini = new Ini(fileToParse);
        return ini.entrySet().stream().collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
