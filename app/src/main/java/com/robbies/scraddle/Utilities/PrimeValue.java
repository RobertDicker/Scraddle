package com.robbies.scraddle.Utilities;

import java.math.BigDecimal;

public class PrimeValue {

    public static String calculatePrimeValue(String word) {

        int[] primeNumbers = new int[]{3,
                5,
                7,
                11,
                13,
                17,
                19,
                23,
                29,
                31,
                37,
                41,
                43,
                47,
                53,
                59,
                61,
                67,
                71,
                73,
                79,
                83,
                89,
                97,
                101,
                103};

        BigDecimal wordNumber = new BigDecimal(1);

        char[] wordAsCharArray = word.toLowerCase().toCharArray();

        for (char letter : wordAsCharArray) {

            // Multiply each letter by eachothers prime number value
            // Words that contain the specific letter or word will be divisible with no remainder.
            // -10  is used to turn index of a = 10 to 0 so that a=3 b=5 etc;

            BigDecimal charValue = new BigDecimal(primeNumbers[Character.getNumericValue(letter) - 10]);

            wordNumber = wordNumber.multiply(charValue);


        }

        return wordNumber.toString();


    }

}
