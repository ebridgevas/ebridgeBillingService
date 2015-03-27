package com.ebridge.commons.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

/**
 * @author david@tekeshe.com
 */
public class JsonUtils {

    public static Map<String, Map<String, Map<String, String>>> config ( String configFilename )
            throws FileNotFoundException {

        return new Gson()
                .fromJson(
                        new BufferedReader(new FileReader(configFilename)),
                        new TypeToken<Map<String, Map<String, Map<String, String>>>>() {
                        }.getType());
    }
}
