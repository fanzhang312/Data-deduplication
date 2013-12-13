import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * Perform content defined chunking. Print out the number of chunks for each
 * file and the number of duplicated chunks if greater than 0. The Rabin-Karp
 * Rolling hash algorithm is learnt from Adam Horvath's blog. Modification is
 * being made. Hashing the chunks and save the hash value into a index table.
 * 
 * @author Fan Zhang, Zhiqi Chen
 */
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

	public RabinKarpRollingHash(String directory) {
		indexTable = new Hashtable<String, String>();
		sum = new Checksum();
		// list = new FileList(Config.DIRECTORY);
		list = new FileList(directory);
		window = 1024; // initial window size
	}

	// Initialize some value before start find chunks for each file
	public void initialize() {
		mult = 1;
		buffptr = 0;
	}

	public void setAll(File[] fileList) {
		for (File file : fileList) {
			if (file.isFile() && !file.isHidden()) {
				initialize();
				displayChunks(file);
			}
		}
	}

	public void displayChunks(File filelocation) {
		int mask = 1 << 13;
		mask--; // 13 bit of '1's

		File f = filelocation;
		FileInputStream fs = null; // For sliding window
		FileInputStream fsChunk = null; // For chunking the input stream
		BufferedInputStream bis = null;
		try {
			fs = new FileInputStream(f);
			fsChunk = new FileInputStream(f);
			bis = new BufferedInputStream(fs);
			// BufferedInputStream is faster to read byte-by-byte from
			this.is = bis;

			long length = bis.available();
			long curr = length;
			// get the initial 1k hash window //
			int hash = inithash(window);
			curr -= bis.available(); // move the curr to next byte of the initial hash window

			byte[] chunk = null;
			String hashvalue = null;
			boolean firstChunk = true;
			int count = 0; // count chunks
			int duplicate = 0; // count duplicate
			while (curr < length) {
				if ((hash & mask) == 0) {
					// window found - hash it,
					if (firstChunk == true) {
						chunk = new byte[(int) curr];
						firstChunk = false;
					} else {
						chunk = new byte[segment];
					}
					if (fsChunk.read(chunk) != -1) {
						// perform the hash on the chunk
						hashvalue = sum.chunking(chunk);
						// If not exist then save
						if (!indexTable.containsKey(hashvalue)) {
							indexTable.put(hashvalue, f.getName());
						} else {
							// found duplicated chunks
							duplicate++;
						}
					}

					segment = 0;
					count++;
				}
				// next window's hash //
				hash = nexthash(hash);
				curr++;
				segment++;
			}
			System.out.println(count + " chunks generated for: " + f.getName());
			if (duplicate != 0) {
				System.out.println(duplicate + " duplicated chunks in: "
						+ f.getName());
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// clean up
			if (fs != null) {
				try {
					is.close();
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
		buffer = new int[length]; // create circular buffer

		int hash = 0;

		// calculate the hash sum of p^n * a[x]
		for (int i = 0; i < length; i++) {
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

}
