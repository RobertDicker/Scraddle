package com.robbies.scraddle;

import android.util.Log;

import java.math.BigInteger;

class PrimeValue {

    static String calculatePrimeValue(String word) {

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

        BigInteger wordNumber = new BigInteger("1");

        char[] wordAsCharArray = word.toCharArray();

        for (char letter : wordAsCharArray) {
            BigInteger charValue = new BigInteger(Integer.toString(primeNumbers[Character.getNumericValue(letter) - 10]));

            // Multiply each letter by eachothers prime number value
            // Words that contain the specific letter or word will be divisible with no remainder.
            // -10  is used to turn index of a = 10 to 0 so that a=3 b=5 etc;

            wordNumber = wordNumber.multiply(charValue);
            Log.d("--------", wordNumber.toString());
        }
        return wordNumber.toString();


    }

}
