package com.robbies.scraddle;

import java.util.List;

public class tidyStringFormatterHelper {


    public static String formatNamesArrayWithCommasAnd(List<String> items) {
        if (items.isEmpty()) {
            return "";
        }

        StringBuilder sB = new StringBuilder();

        for (String item : items) {
            sB.append(", ");
        }

        if (items.size() > 1) {
            sB.replace(sB.lastIndexOf(", "), sB.lastIndexOf(", ") + 2, " and ");
        }

        return sB.toString().substring(1);

    }

    static String addToItemStringWithCommasAnd(String originalString, String itemToAdd) {
        if (itemToAdd.isEmpty()) return originalString;

        StringBuilder sb = new StringBuilder();
        sb.append(originalString);
        int indexOfAnd = sb.lastIndexOf(" and ");
        if (indexOfAnd == -1) {
            if (originalString.isEmpty()) {
                sb.append(itemToAdd);
            } else {
                sb.append(" and ");
                sb.append(itemToAdd);
            }
            return sb.toString();
        }

        sb.replace(indexOfAnd, indexOfAnd + 5, ", ");
        sb.append(" and ");
        sb.append(itemToAdd);
        return sb.toString();
    }

}
