import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Perform a fixed size de-duplication. Print out de-duplication results if
 * there is duplicated data chunks
 * 
 * @author Fan Zhang, Zhiqi Chen
 * 
 */
public class FixedSizeChunking {
	public Hashtable<String, String> indexTable;
	public Checksum sum;
	public FileList list;
	public int count;
	private FileInputStream fis;

	public FixedSizeChunking(String directory) {
		indexTable = new Hashtable<String, String>();
		sum = new Checksum();
		// list = new FileList(Config.DIRECTORY);
		list = new FileList(directory);
		count = 0;
	}

	public void setAll(File[] fileList) {
		// Read each file and perform fixed-size chunking
		byte[] chunk = new byte[Config.FIXED_CHUNKING];
		for (File f : fileList) {
			count = 0;
			if (f.isFile() && !f.isHidden()) {
				// Read file into a byte array and use SHA-1 hash the chunk
				try {
					fis = new FileInputStream(f.getAbsolutePath());
					while (fis.read(chunk) != -1) {
						// perform the hash on the chunk
						String hashvalue = sum.chunking(chunk);
						// If not exist then save
						if (!indexTable.containsKey(hashvalue)) {
							indexTable.put(hashvalue, f.getName());
						} else {
							System.out.println(++count
									+ " duplicate contents found in: "
									+ f.getName());
						}
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
