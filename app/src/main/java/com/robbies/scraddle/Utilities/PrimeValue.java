package com.robbies.scraddle.Utilities;

import android.util.Log;

import java.math.BigDecimal;
import java.math.BigInteger;

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

            char letters = letter;
            int lettervalue = Character.getNumericValue(letter);
            int lettervalueminus10 = Character.getNumericValue(letter)-10;
            int primeletterminus10 = primeNumbers[lettervalueminus10];

            BigDecimal charValue = new BigDecimal(primeNumbers[Character.getNumericValue(letter) - 10]);

            // Multiply each letter by eachothers prime number value
            // Words that contain the specific letter or word will be divisible with no remainder.
            // -10  is used to turn index of a = 10 to 0 so that a=3 b=5 etc;

            wordNumber = wordNumber.multiply(charValue);
            Log.d("--------", wordNumber.toString());

        }
        Log.d("--------", "Prime Digits:" + wordNumber.toString().length());
        return wordNumber.toString();


    }

}
