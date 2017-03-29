package com.vb.feistelnet;

import android.util.Log;

import java.nio.charset.Charset;

/**
 * Created by bonar on 3/29/2017.
 */

public class FeistelNet {
    private static final String TAG = "FeistelNet";

    private static final int MAX_ROUNDS = 5;

    private static final int BLOCK_SIZE = 8;

    public String crypt(String s, boolean encrypt) {

        String result = "";

        while(s.length() % BLOCK_SIZE != 0)
            s += " ";

        for(int i = 0; i + BLOCK_SIZE - 1 < s.length(); i += BLOCK_SIZE) {
            String sub = s.substring(i, i + BLOCK_SIZE);
            byte[] block = sub.getBytes(Charset.forName("UTF-8"));
            result += new String(getBlock(block, encrypt), Charset.forName("UTF-8"));
        }

        return result;
    }

    public byte[] getBlock(final byte[] block, boolean encrypt) {
        if(block.length != BLOCK_SIZE)
            return null;

        int l = ArrayUtills.byteArrayToInt(ArrayUtills.subByteArray(block, 0, BLOCK_SIZE / 2));
        int r = ArrayUtills.byteArrayToInt(ArrayUtills.subByteArray(block, BLOCK_SIZE / 2 + BLOCK_SIZE % 2, BLOCK_SIZE / 2));

        Log.d(TAG, "" + l);
        Log.d(TAG, "" + r);

        int round = (encrypt) ? 1 : MAX_ROUNDS;

        for(int i = 0; i < MAX_ROUNDS; i++)
        {
            if (i < MAX_ROUNDS - 1)
            {
                int t = l;
                l = r ^ f(l, round);
                r = t;
            } else
                r = r ^ f(l, round);

            Log.d(TAG, "Round: " + round);

            round += (encrypt) ? 1 : -1;
        }

        return ArrayUtills.mergeByteArrays(ArrayUtills.intToByteArray(l), ArrayUtills.intToByteArray(r));
    }

    private int f(int a, int k)
    {
        return a + k;
    }



}
