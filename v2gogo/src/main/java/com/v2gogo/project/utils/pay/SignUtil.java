package com.v2gogo.project.utils.pay;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 签名工具类
 * 
 * @author houjun
 */
public class SignUtil
{
	private static final String ALGORITHM = "RSA";
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	public static String sign(String content, String privateKey)
	{
		try
		{
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));
			byte[] signed = signature.sign();
			return Base64.encode(signed);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
