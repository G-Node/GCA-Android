package com.yasiradnan.utils;


import com.yasiradnan.conference.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONReader {

    public static JSONArray parseStream(InputStream stream) throws IOException, JSONException {
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader jsonReader = new BufferedReader(isr);

        StringBuilder jsonBuilder = new StringBuilder();
        for (String line = null; (line = jsonReader.readLine()) != null;) {
            jsonBuilder.append(line).append("\n");
        }
        JSONTokener tokener = new JSONTokener(jsonBuilder.toString());
        return new JSONArray(tokener);
    }

}
