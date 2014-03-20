package org.tarent.cvio.server.cv;

import java.util.HashMap;
import java.util.Map;

public class CVHelper {

    public static Map<String, Object> demoCVWithoutId() {
        return new HashMap<String, Object>() {
            {
                put("userName", "smanck");
                put("givenName", "Sebastian");
                put("familyName", "Mancke");

                put("skills", new HashMap<String, Integer>() {
                    {
                        put("idxxyz-1", 1);
                        put("idxxyz-2", 3);
                    }
                });
            }
        };
    }
}
