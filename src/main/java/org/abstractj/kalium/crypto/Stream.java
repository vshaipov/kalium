package org.abstractj.kalium.crypto;

import static org.abstractj.kalium.NaCl.sodium;
import static org.abstractj.kalium.crypto.Util.checkLength;
import static org.abstractj.kalium.crypto.Util.isValid;
import static org.abstractj.kalium.crypto.Util.prependZeros;
import static org.abstractj.kalium.crypto.Util.removeZeros;

import org.abstractj.kalium.NaCl;

public class Stream {

	private byte[] key;

	public Stream(byte[] key) {
		this.key = key;
		checkLength(key, NaCl.Sodium.AES_128_CTR_KEYBYTES);
	}

	public byte[] encrypt(byte[] nonce, byte[] message) {
		checkLength(nonce, NaCl.Sodium.CRYPTO_STREAM_AES_128_CTR_NONCEBYTES);
		byte[] msg = prependZeros(NaCl.Sodium.ZERO_BYTES, message);
		byte[] ct = new byte[msg.length];

		isValid(sodium().crypto_stream_aes128ctr_xor(ct, msg, msg.length,
				nonce, key), "Encryption failed");

		return removeZeros(NaCl.Sodium.BOXZERO_BYTES, ct);
	}

	public byte[] decrypt(byte[] nonce, byte[] ciphertext) {
		checkLength(nonce, NaCl.Sodium.CRYPTO_STREAM_AES_128_CTR_NONCEBYTES);
		byte[] ct = prependZeros(NaCl.Sodium.BOXZERO_BYTES, ciphertext);
		byte[] message = new byte[ct.length];

		isValid(sodium().crypto_stream_aes128ctr_xor(message, ct,
				message.length, nonce, key), "Decryption failed");

		return removeZeros(NaCl.Sodium.ZERO_BYTES, message);
	}

}
