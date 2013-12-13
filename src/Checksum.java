import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generate checksum and provide method to store file name and corresponding
 * checksum value
 * 
 * @author Fan Zhang, Zhiqi Chen
 * 
 */
public class Checksum {
	public MessageDigest md;
	private FileInputStream fis;

	public Checksum() {
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	// generate checksum for specific file use SHA1. Return: hash value for the
	// file
	public String generateChecksum(String filepath, String filename) {
		try {
			fis = new FileInputStream(filepath);

			byte[] dataBytes = new byte[1024];

			int nread = 0;

			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] mdbytes = md.digest();

		String result = byteToHex(mdbytes);
		
//		System.out.println(filename + " :: " + result);

		return result;
	}

	public String chunking(byte[] dataBytes){
		md.update(dataBytes);
		byte[] mdbytes = md.digest();
		String result = byteToHex(mdbytes);
		return result;
	}
	
	// convert the byte to hex format
	public String byteToHex(byte[] mdbytes) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}

}
