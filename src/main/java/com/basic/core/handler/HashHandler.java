package com.basic.core.handler;

import com.basic.core.model.MurmurHash;

import java.io.UnsupportedEncodingException;

public class HashHandler {
	public static int[] getHashBuckets(String item, int hashCount, int max) {
		byte[] b = null;
		try {
			b = item.getBytes("UTF-16");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] result = new int[hashCount];
	    int hash1 = MurmurHash.hash(b, b.length, 0);
	    int hash2 = MurmurHash.hash(b, b.length, hash1);
	    for (int i = 0; i < hashCount; i++) {
	        result[i] = Math.abs((hash1 + i * hash2) % max);
	    }
	    return result;
	}
}
