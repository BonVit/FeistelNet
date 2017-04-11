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

    /*private int f(int a, int k)
    {
        a += k;



        return a;
    }*/

    private int code(int a)
    {
        byte[] n = ArrayUtills.intTo4BitsArray(a);

        int[] i = mixArray(n);

        return ArrayUtills.byteArrayToInt(i);
    }

    private int[] mixArray(byte[] arr)
    {
        byte[] tmp = new byte[arr.length];

        final byte[][] CODE_TABLE = new byte[][] {
                {0, 7, 3, 4, 6, 2, 1, 8, 10, 13, 15, 14, 9, 12, 5, 11},
                {7, 3, 4, 6, 2, 1, 8, 10, 13, 15, 14, 9, 12, 5, 11, 0},
                {3, 4, 6, 2, 1, 8, 10, 13, 15, 14, 9, 12, 5, 11, 0, 7},
                {4, 6, 2, 1, 8, 10, 13, 15, 14, 9, 12, 5, 11, 0, 7, 3},
                {6, 2, 1, 8, 10, 13, 15, 14, 9, 12, 5, 11, 0, 7, 3, 4},
                {2, 1, 8, 10, 13, 15, 14, 9, 12, 5, 11, 0, 7, 3, 4, 6},
                {1, 8, 10, 13, 15, 14, 9, 12, 5, 11, 0, 7, 3, 4, 6, 2},
                {8, 10, 13, 15, 14, 9, 12, 5, 11, 0, 7, 3, 4, 6, 2, 1},
        };

        for(int i = 0; i < arr.length; i++) {
            byte t = arr[i];
            t = (t <= 0) ? (byte) -t: t;
            tmp[i] = CODE_TABLE[i][t];
        }
        int[] result = new int[tmp.length / 2];

        for(int i = 0; i < result.length; i++)
            result[i] = ArrayUtills.merge2Bytes(tmp[i * 2], tmp[i * 2 + 1]);

        return result;
    }
}
