

package com.daemin.encryption;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by hernia on 2015-06-20.
 */

public class MyHash {
    MessageDigest hash;
    /**
     * byte[] 타입의 데이타를 암호화 한다.
     * @return byte[] 암호화 된 데이타.
     * @throws NoSuchAlgorithmException 암호화 알고리즘을 찾을 수 없을때 예외 처리
     * @throws InvalidKeyException 규칙에 맞지 않은 key 일때 예외 처리
     * @throws NoSuchPaddingException 패딩 정보를 찾을 수 없을때 예외 처리
     * @throws IOException 입/출력 예외 처리
     * @throws InvalidKeySpecException 규칙에 맞지 않은 keySpec 일때 예외 처리
     * @throws IllegalBlockSizeException 규칙에 맞지 않은 블럭사이즈 일때 예외 처리
     * @throws BadPaddingException 잘못된 패딩 일때 예외처리
     * @throws InvalidAlgorithmParameterException 유효하지 않은 알고리즘 파라미터 일때 예외처리
     */
    public MyHash() throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException, IOException,
            InvalidKeySpecException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {
        //MessageDigest hash = MessageDigest.getInstance("MD5");
        //MessageDigest hash = MessageDigest.getInstance("SHA-1");
       // MessageDigest.isEqual(digest01, digest02)
        hash = MessageDigest.getInstance("SHA-256");
    }

    public String encrypt(String encrypt) throws NoSuchAlgorithmException,
            InvalidKeyException, NoSuchPaddingException, IOException,
            InvalidKeySpecException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException{
        byte[] digest = null;
            hash.update(encrypt.getBytes());
            digest = hash.digest();
        String encryptString = "";
        for(int i=0; i<digest.length; i++){
            encryptString += String.valueOf(digest[i]);
        }
        return encryptString;
    }
}

