package db;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.RuntimeCryptoException;

public class DBException extends RuntimeCryptoException {

	private static final long serialVersionUID = 1L;
	
	public DBException(String str) {
		super(str);
	}

}
