 /**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2014, Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com> 
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.utils;

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