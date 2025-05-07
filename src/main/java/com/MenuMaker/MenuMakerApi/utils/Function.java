package com.MenuMaker.MenuMakerApi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Function {

    public static String getCookie(String cookieHeader, String name) {
        String value = "; " + cookieHeader;
        String[] parts = value.split("; " + name + "=");
        if (parts.length == 2) {
            List<String> partsArray = new ArrayList<>(Arrays.asList(parts));
            String lastParts = partsArray.removeLast();
            return lastParts;
        }

        return null;
    }
}
