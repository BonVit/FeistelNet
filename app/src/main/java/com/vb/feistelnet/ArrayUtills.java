package com.vb.feistelnet;

/**
 * Created by bonar on 3/29/2017.
 */

public class ArrayUtills {
    public static int byteArrayToInt(final byte[] b)
    {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public static byte[] mergeByteArrays(byte[] a, byte[] b)
    {
        byte[] result = new byte[a.length + b.length];
        int i = 0;
        for(int j = 0; j < a.length; j++, i++)
            result[i] += a[j];
        for(int j = 0; j < b.length; j++, i++)
            result[i] += b[j];

        return result;
    }

    public static byte[] subByteArray(final byte[] src, int srcPos, int dstSize)
    {
        if(src.length < dstSize || src.length == 0)
            return null;

        byte[] dst = new byte[dstSize];

        for(int i = 0; i < dst.length; i++)
            dst[i] = src[srcPos + i];

        return dst;
    }
}
