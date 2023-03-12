package com.juaracoding.DBLaundry.utils;

import java.util.Map;

public class ManipulationMap {


    public static String getKeyFromValue(Map<String,String> mapEntries,String value)
    {
        for (Map.Entry<String, String> entry : mapEntries.entrySet()) {
            if (entry.getValue().equals(value)) {
                System.out.println(entry.getKey());
            }
        }
        return "id";
    }
}
