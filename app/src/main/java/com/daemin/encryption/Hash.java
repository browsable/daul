/*

package com.daemin.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

*/
/**
 * Created by hernia on 2015-06-20.
 *//*


public class Hash {
    public static void printHex(byte[] output)
    {
        for(int i=0; i<output.length; i++){
            System.out.printf("%02X ", output[i]);
            if((i+1)%16==0) System.out.println();
        }
        System.out.println();
    }
    public Hash() throws NoSuchAlgorithmException {
        MessageDigest hash = MessageDigest.getInstance("SHA-256");
    }


 public static void main(String[] args) {
            String plaintext01 = "This is a simple message.";
            String plaintext02 = "This is a simple message";
            try{
                //MessageDigest hash = MessageDigest.getInstance("MD5");
                //MessageDigest hash = MessageDigest.getInstance("SHA-1");
                MessageDigest hash = MessageDigest.getInstance("SHA-256");
                hash.update(plaintext01.getBytes());
                byte[] digest01 = hash.digest();
                System.out.printf("Hash-value(%d Bytes):%n", digest01.length);
                printHex(digest01);
                hash.update(plaintext02.getBytes());
                byte[] digest02 = hash.digest();
                System.out.printf("Hash-value(%d Bytes):%n", digest02.length);
                printHex(digest02);
                System.out.printf("Verified = %s%n",
                        MessageDigest.isEqual(digest01, digest02));

                String byteToString = new String(digest01,0,digest01.length);
                System.out.println(byteToString);
                String byteToString2 = new String(digest02,0,digest02.length);
                System.out.println(byteToString2);
            }
            catch(NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        } // main
    } // DigestTest
}

*/
