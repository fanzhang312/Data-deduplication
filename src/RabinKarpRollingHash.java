import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public class RabinKarpRollingHash {
	public static final int hconst = 69069; // good hash multiplier for MOD 2^32
	public int mult = 1; // this will hold the p^n value
	int[] buffer; // circular buffer - reading from file stream
	int buffptr = 0;
	int segment = 0; // Once a chunk found, reset to 0
	InputStream is;
	int window;
	public Hashtable<String, String> indexTable;
	public FileList list;
	public Checksum sum;
	
	public RabinKarpRollingHash(){
		indexTable = new Hashtable<String, String>();
		sum = new Checksum();
		list = new FileList(Config.DIRECTORY);
		window = 1024; // initial window size
	}
	
	public void setAll(File[] fileList){
		for(File file : fileList){
			if(!file.getName().equals(".DS_Store")){
				System.out.println(file.getName());
				displayChunks(file);
			}
		}
	}
	
	public void displayChunks(File filelocation) {
		int mask = 1 << 13;
		mask--; // 13 bit of '1's

		File f = filelocation;
		FileInputStream fs;
		try {
			fs = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fs);
			// BufferedInputStream is faster to read byte-by-byte from
			this.is = bis;

			long length = bis.available();
			long curr = 0;
			
			// get the initial 1k hash window //
			int hash = inithash(window);
			// //////////////////////////////////
			byte[] chunk = new byte[window];
			fs.read(chunk);
			String hashvalue = sum.chunking(chunk);
			indexTable.put(hashvalue, f.getName());

			int count = 0; // count chunks
			while (curr < length) {
				if ((hash & mask) == 0) {
					// window found - hash it,
					// compare it to the other file chunk list
					chunk = new byte[segment];
					if(fs.read(chunk) != -1){
						// perform the hash on the chunk
						hashvalue = sum.chunking(chunk);
						// If not exist then save
						if(!indexTable.containsKey(hashvalue)){
							indexTable.put(hashvalue, f.getName());
						}else{
							System.out.println(hash+ " Duplicate contents found in: "+ f.getName());
						}
					}
					segment=0;
					count++;
				}
				// next window's hash //
				hash = nexthash(hash);
				// ///////////////////////
				curr++;
				segment++;
			}
			System.out.println(count + " chunks generated: " + f.getName());
			fs.close();
		} catch (Exception e) {
		}
	}

	public int nexthash(int prevhash) throws IOException {
		int c = is.read(); // next byte from stream

		prevhash -= mult * buffer[buffptr]; // remove the last value
		prevhash *= hconst; // multiply the whole chain with prime
		prevhash += c; // add the new value
		buffer[buffptr] = c; // circular buffer, 1st pos == lastpos
		buffptr++;
		buffptr = buffptr % buffer.length;

		return prevhash;
	}

	public int inithash(int length) throws IOException {
		return inithash(length - 1, 0);
	}

	public int inithash(int from, int to) throws IOException {
		buffer = new int[from - to + 1]; // create circular buffer

		int hash = 0;

		// calculate the hash sum of p^n * a[x]
		for (int i = 0; i <= from - to; i++) {
			int c = is.read();
			if (c == -1) // file is shorter than the required window size
				break;

			// store byte so we can remove it from the hash later
			buffer[buffptr] = c;
			buffptr++;
			buffptr = buffptr % buffer.length;

			hash *= hconst; // multiply the current hash with constant

			hash += c; // add byte to hash

			if (i > 0) // calculate the large p^n value for later usage
				mult *= hconst;
		}

		return hash;
	}

	public static void main(String[] args) {
		RabinKarpRollingHash fsc = new RabinKarpRollingHash();
		fsc.setAll(fsc.list.filelist);
	}
}
