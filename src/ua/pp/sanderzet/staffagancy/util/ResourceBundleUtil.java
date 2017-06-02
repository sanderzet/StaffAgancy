package ua.pp.sanderzet.staffagancy.util;

import java.util.*;

/**
 * Created by alzet on 02.06.17.
 */
public class ResourceBundleUtil {



        public static ArrayList<String> getValuesFromResourceBandle (ResourceBundle bundle, String
                keyPrefix) {
            ArrayList<String> result;
            Enumeration<String> keys = bundle.getKeys();
            ArrayList<String> temp = new ArrayList<String>();

            // get the keys and add them in a temporary ArrayList
            for (Enumeration<String> e = keys; keys.hasMoreElements(); ) {
                String key = e.nextElement();
                if (key.startsWith(keyPrefix)) {
                    temp.add(key);
                }
            }


            // create a string array based on the size of temporary ArrayList
            result = new ArrayList<>(temp.size());

            // store the bundle Strings in the StringArray
            for (int i = 0; i < temp.size(); i++) {
                result.add(bundle.getString(temp.get(i)));
            }
            return result;
        }

    public static HashMap<String,String> getHashMapDoc (ResourceBundle bundle, String
            keyPrefix) {
        HashMap<String,String> result;
        Enumeration<String> keys = bundle.getKeys();
        ArrayList<String> temp = new ArrayList<String>();

        // get the keys and add them in a temporary ArrayList
        for (Enumeration<String> e = keys; keys.hasMoreElements(); ) {
            String key = e.nextElement();
            if (key.startsWith(keyPrefix)) {
                temp.add(key);
            }
        }


        // create a string array based on the size of temporary ArrayList
        result = new HashMap<>(temp.size());

        // store the bundle Strings in the StringArray
        for (int i = 0; i < temp.size(); i++) {
            result.put(temp.get(i),bundle.getString(temp.get(i)));
        }
        return result;
    }


        public static String getKeyForValue (ResourceBundle bundle, String keyPrefix, String value) {
            String result = "";
            Enumeration<String> keys = bundle.getKeys();
            ArrayList<String> temp = new ArrayList<String>();

            // get the keys and add them in a temporary ArrayList
            for (Enumeration<String> e = keys; keys.hasMoreElements(); ) {
                String key = e.nextElement();
                if (key.startsWith(keyPrefix) && bundle.getString(key).equals(value)) {
                    result = key;
                }
            }


            return result;
        }

}