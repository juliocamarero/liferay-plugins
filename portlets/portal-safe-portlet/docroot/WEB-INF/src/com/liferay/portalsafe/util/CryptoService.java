/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portalsafe.util;

import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portalsafe.PortalSafeConfigurationException;
import com.liferay.util.Encryptor;

import java.io.InputStream;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

import com.liferay.util.EncryptorException;
import jodd.util.BCrypt;

/**
 * Encryption and decryption service
 *
 * @author Tomas Polesovsky
 */
public class CryptoService {

	/**
	 * Returns singleton of the instance
	 *
	 * @return the instance
	 */
	public static CryptoService getInstance() {
		return _instance;
	}

	/**
	 * Resets bytes in the password, we shouldn't wait for GC
	 *
	 * @param password the password to be cleared
	 */
	public void clearPassword(byte[] password) {
		if (password == null) {
			return;
		}

		synchronized (_secureRandom) {
			_secureRandom.nextBytes(password);
		}
	}

	/**
	 * Resets characters in the password, we shouldn't wait for GC
	 *
	 * @param password the password to be cleared
	 */
	public void clearPassword(char[] password) {
		if (password == null) {
			return;
		}

		synchronized (_secureRandom) {
			for (int i = 0; i < password.length; i++) {
				password[i] = (char) _secureRandom.nextInt();
			}
		}
	}

	/**
	 * Decrypts content using secret key belonging to the company
	 *
	 * @param  companyId ID of company to be used to get and decrypt the key
	 * @param  content the content to be decrypted
	 * @return decrypted content in bytes
	 * @throws Exception if decrypting fails
	 */
	public byte[] decrypt(long companyId, byte[] content) throws Exception {
		Cipher cipher = getCipher(Cipher.DECRYPT_MODE, companyId);

		return cipher.doFinal(content);
	}

	/**
	 * Encrypts content using secret key belonging to the company
	 *
	 * @param  companyId ID of company to be used to get and decrypt the key
	 * @param  content the content to be encrypted
	 * @return encrypted content in bytes
	 * @throws Exception if encrypting fails
	 */
	public byte[] encrypt(long companyId, byte[] content) throws Exception {
		Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, companyId);

		return cipher.doFinal(content);
	}

	/**
	 * Generates new safe password based on the userInput
	 *
	 * @param  companyId ID of company to be used to encrypt the password
	 * @param  userInput random array of characters to be used for password
	 * @return character arrays {plainTextPassword, encryptedPassword}
	 * @throws Exception if password generating fails
	 */
	public char[][] generatePassword(long companyId, char[] userInput)
		throws Exception {

		if (Validator.isNull(userInput)) {
			throw new IllegalArgumentException(
				"Parameter userInput must be specified!");
		}

		// generate safer password, the userInput may be weak or not random
		// enough

		String password =
			BCrypt.hashpw(
				new String(userInput), BCrypt.gensalt(
					PortletPropsValues.PORTAL_SAFE_BCRYPT_LOG_ROUNDS));

		// Encrypt the password with the company key. An attacker, to be able to
		// decrypt the files, has to steal both parts - company key from DB and
		// the password from the disk.

		Company company = CompanyLocalServiceUtil.getCompanyById(companyId);
		Key key = company.getKeyObj();
		byte[] encryptedWithCompanyKey = Encryptor.encryptUnencoded(
			key, password);

		char[] encodedEncryptedPassword = Base64.encode(
			encryptedWithCompanyKey).toCharArray();

		return new char[][]{password.toCharArray(), encodedEncryptedPassword};
	}

	/**
	 * Returns cipher stream based which encrypts or decrypts the inputStream
	 * using portal safe's secret key.
	 *
	 * @param  cipherMode either {@link Cipher#ENCRYPT_MODE} or {@link
	 *         Cipher#DECRYPT_MODE} or to be used for cipher
	 * @param  companyId ID of company to be used to get and decrypt the key
	 * @param  inputStream stream to be encrypted using CipherInputStream
	 * @return encrypting or decrypting {@link CipherInputStream}
	 * @throws Exception if {@link #getCipher(int, long)} throws the exception
	 */
	public CipherInputStream getCipherStream(
		int cipherMode, long companyId, InputStream inputStream)
		throws Exception {

		Cipher cipher = getCipher(cipherMode, companyId);

		return new CipherInputStream(inputStream, cipher);
	}

	/**
	 * Returns portal safe's secret key for the companyId. Returned array must
	 * be cleared using {@link #clearPassword(char[])}.
	 *
	 * @param  companyId ID of company to be used to get and decrypt the key
	 * @return the secret key in form of a character array
	 * @throws Exception if {@link #getPasswordAsBytes(long)} throws the
	 *         exception
	 */
	public char[] getPassword(long companyId) throws Exception {
		byte[] passwordBytes = getPasswordAsBytes(companyId);

		char[] result = new char[passwordBytes.length];
		for (int i = 0; i < passwordBytes.length; i++) {
			result[i] = (char) passwordBytes[i];
		}

		clearPassword(passwordBytes);

		return result;
	}

	/**
	 * Loads and decrypts portal safe's secret key belonging to the company.
	 * Returned array must be cleared using {@link #clearPassword(char[])}.
	 *
	 * @param  companyId ID of company to be used to get and decrypt the key
	 * @return decrypted password as bytes
	 */
	public byte[] getPasswordAsBytes(long companyId) throws Exception {
		String safePasswordEncoded =
			PropsUtil.get(PropsKeys.PORTAL_SAFE_PASSWORD +
				StringPool.PERIOD + companyId);

		if (Validator.isNull(safePasswordEncoded)) {
			throw new PortalSafeConfigurationException(
				"Password is not configured!");
		}

		byte[] safePassword = Base64.decode(safePasswordEncoded);
		try {
			Company company = CompanyLocalServiceUtil.getCompanyById(companyId);
			Key key = company.getKeyObj();

			byte[] decryptedSafePassword = Encryptor.decryptUnencodedAsBytes(
				key, safePassword);

			return decryptedSafePassword;
		} catch (EncryptorException e){
			throw new PortalSafeConfigurationException(
				"Unable to decrypt password for companyId " + companyId, e);
		} finally {
			clearPassword(safePassword);
		}

	}

	/**
	 * Constructs cipher
	 *
	 * @param  cipherMode either {@link Cipher#ENCRYPT_MODE} or {@link
	 *         Cipher#DECRYPT_MODE} or to be used for cipher
	 * @param  companyId ID of company to be used to get and decrypt the key
	 * @throws Exception if {@link #getPasswordAsBytes(long)} throws the
	 *         exception or if the Cipher cannot be initialized
	 */
	protected Cipher getCipher(int cipherMode, long companyId)
		throws Exception {

		byte[] password = getPasswordAsBytes(companyId);
		SecretKeySpec secretKeySpec = null;
		try {
			if ((PortletPropsValues.PORTAL_SAFE_ALGORITHM_KEY_SIZE % 8) > 0) {
				throw new PortalSafeConfigurationException(
					"Key size specified in " +
						PortletPropsKeys.PORTAL_SAFE_ALGORITHM_KEY_SIZE +
						" must be rounded to 8 bits!");
			}

			int keySize = PortletPropsValues.PORTAL_SAFE_ALGORITHM_KEY_SIZE / 8;

			if (keySize > password.length) {
				throw new PortalSafeConfigurationException(
					"Key size specified in " +
						PortletPropsKeys.PORTAL_SAFE_ALGORITHM_KEY_SIZE +
							" cannot be more than " + password.length);
			}

			int start = password.length - keySize;
			secretKeySpec =
				new SecretKeySpec(
					password, start, keySize,
					PortletPropsValues.PORTAL_SAFE_ALGORITHM);

		} finally {
			clearPassword(password);
		}

		Cipher cipher = Cipher.getInstance(
			PortletPropsValues.PORTAL_SAFE_ALGORITHM);

		cipher.init(cipherMode, secretKeySpec);

		return cipher;
	}

	/**
	 * This class's singleton
	 */
	private static CryptoService _instance = new CryptoService();

	/**
	 * Source of randomness
	 */
	private SecureRandom _secureRandom = new SecureRandom();

}