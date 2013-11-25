import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;


public class FixedSizeChunking {
	public Hashtable<String, String> indexTable;
	public Checksum sum;
	public FileList list;
	public int count;
	private FileInputStream fis;
	
	public FixedSizeChunking(){
		indexTable = new Hashtable<String, String>();
		sum = new Checksum();
		list = new FileList(Config.DIRECTORY);
		count = 0;
	}
	
	public void setAll(File[] fileList){
		// Read each file and perform fixed-size chunking
		byte[] chunk = new byte[Config.FIXED_CHUNKING];
		for(File f: fileList){
			if(f.isDirectory()){
				// do nothing if it is a directory
			}else{
				// Read file into a byte array and use SHA-1 hash the chunk
				try {
					fis = new FileInputStream(f.getAbsolutePath());
					while(fis.read(chunk) != -1){
						// perform the hash on the chunk
						String hashvalue = sum.chunking(chunk);
						// If not exist then save
						if(!indexTable.containsKey(hashvalue)){
							indexTable.put(hashvalue, f.getName());
						}else{
							System.out.println(++count + " duplicate contents found in: "+ f.getName());
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
	
	public static void main(String[] args){
		FixedSizeChunking fsc = new FixedSizeChunking();
		fsc.setAll(fsc.list.filelist);
	}
}
