package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        int index1, index2, index3;
        Sandwich sw = new Sandwich();
        String str1;
        String str2;
        List<String> alsoknownlist = new ArrayList<>();
        List<String> ingredientlist = new ArrayList<>();

        // find first ":"
        // is there a " on each side?
        // is there a [ on one side?
        while (json.length() > 0) {
            index1 = json.indexOf(":");
            index2 = 0;
            if (json.charAt(index1 + 1) == '"' && json.charAt(index1 - 1) == '"') {
                str1 = json.substring(0, index1 - 1);
                if (json.charAt(index1 + 2) == '"') {
                    str2 = "";
                    index2 = str1.lastIndexOf("\"");
                    str1 = str1.substring(index2 + 1, str1.length());
                }else {
                    str2 = json.substring(index1 + 1, json.length());
                    index2 = str1.lastIndexOf("\"");
                    str1 = str1.substring(index2 + 1, str1.length());
                    index2 = str2.indexOf("\",\"");
                    str2 = str2.substring(1, index2);
                }
                if (str1.matches("mainName")) {
                    sw.setMainName(str2);
                } else if (str1.matches("description")) {
                    str2 = cleanUp(str2);
                    sw.setDescription(str2);
                } else if (str1.matches("image")) {
                    sw.setImage(str2);
                } else if (str1.matches("placeOfOrigin")) {
                    sw.setPlaceOfOrigin(str2);
                }
                json = json.substring(index2 + index1 + 3, json.length());
            } else if (json.charAt(index1 + 1) == '[' && json.charAt(index1 - 1) == '"') {
                str1 = json.substring(0, index1 - 1);
                str2 = json.substring(index1 + 2, json.length());
                index2 = str1.lastIndexOf("\"");
                str1 = str1.substring(index2 + 1, str1.length());
                index2 = str2.indexOf("}");
                str2 = str2.substring(0, index2 - 1);
                if (str2.length() > 0) {
                    if (str1.matches("alsoKnownAs")) {
                        while (str2.length() > 0) {
                            str2 = str2.substring(1, str2.length());
                            index3 = str2.indexOf("\",\"");
                            if (index3 < 0) {
                                alsoknownlist.add(str2.substring(0, str2.length() - 1));
                                str2 = "";
                            } else {
                                alsoknownlist.add(str2.substring(0, index3));
                                str2 = str2.substring(index3 + 2, str2.length());
                            }
                        }
                        sw.setAlsoKnownAs(alsoknownlist);
                    } else if (str1.matches("ingredients")) {
                        while (str2.length() > 0) {
                            str2 = str2.substring(1, str2.length());
                            index3 = str2.indexOf("\",\"");
                            if (index3 < 0) {
                                ingredientlist.add(str2.substring(0, str2.length() - 1));
                                str2 = "";
                            } else {
                                ingredientlist.add(str2.substring(0, index3));
                                str2 = str2.substring(index3 + 2, str2.length());
                            }
                        }
                        sw.setIngredients(ingredientlist);
                    }
                }
                json = json.substring(index2 + index1 + 3, json.length());
            } else {
                json = json.substring(index1 + 3, json.length());
            }
            if (json.length() <= 0) {
                break;
            }
        }
        return sw;
//        return null;
    }

    public static String cleanUp(String dstr) {
        int index1;
        String slash = "\\";

        while(dstr.contains(slash)){
            index1 = dstr.indexOf(slash);
            dstr = dstr.substring(0, index1) + dstr.substring(index1+1, dstr.length());
        }

        return dstr;
    }

}
