package com.robbies.scraddle;

public class PrimeValue {

    public static int calculatePrimeValue(String word) {

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

        int wordNumber = 1;

        char[] wordAsCharArray = word.toCharArray();

        for (char letter : wordAsCharArray) {
            int charValue = Character.getNumericValue(letter);

            // Multiply each letter by eachothers prime number value
            // Words that contain the specific letter or word will be divisible with no remainder.
            // -10  is used to turn index of a = 10 to 0 so that a=3 b=5 etc;
            wordNumber = wordNumber * primeNumbers[Character.getNumericValue(letter) - 10];
        }
        return wordNumber;


    }

}
