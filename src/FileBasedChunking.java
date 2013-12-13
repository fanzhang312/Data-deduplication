import java.io.File;
import java.util.Hashtable;

/**
 * Perform a file based de-deuplication. Print out the results for duplicated
 * files.
 * 
 * @author Fan Zhang, Zhiqi Chen
 * 
 */
public class FileBasedChunking {
	public Hashtable<String, String> indexTable;
	public Checksum sum;
	public FileList list;
	public int count;

	public FileBasedChunking(String directory) {
		indexTable = new Hashtable<String, String>();
		sum = new Checksum();
		// list = new FileList(Config.DIRECTORY);
		list = new FileList(directory);
		count = 0;
	}

	public void setAll(File[] fileList) {
		for (File f : fileList) {
			count = 0;
			if (f.isFile() && !f.isHidden()) {
				String checksum = sum.generateChecksum(f.getAbsolutePath(),
						f.getName());
				if (duplicateDetection(checksum)) {
					System.out.println(++count + " duplicate files found: "
							+ f.getName());
				} else {
					indexTable.put(checksum, f.getName());
				}
			}
		}
	}

	public boolean duplicateDetection(String hashvalue) {
		return indexTable.containsKey(hashvalue);
	}

}
