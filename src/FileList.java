import java.io.File;

/**
 * FileList class get the client directory and provide methods to get all the
 * file names under that directory or get file path for a given file name.
 * 
 * @author Fan Zhang, Zhiqi Chen
 * 
 */
public class FileList {
	String path;
	File root;
	File[] filelist;

	public FileList(String path) {
		this.path = path;
		root = new File(path);
		filelist = root.listFiles();
	}

	// Display the content of the given directory
	// return all the files' name
	public String getList() {
		String files = "";
		for (File f : filelist) {
			if (f.isDirectory()) {
				// System.out.println("Dir:" + f.getAbsoluteFile());
			} else {
				// System.out.println("File:" + f.getAbsoluteFile());
				files += f.getName() + "\n";
			}
		}
		return files;
	}

	public String getList(String name) {
		String files = "";
		for (File f : filelist) {
			if (f.isDirectory()) {
				// System.out.println("Dir:" + f.getAbsoluteFile());
			} else {
				// System.out.println("File:" + f.getAbsoluteFile());
				if (f.getAbsolutePath().contains(name))
					files = f.getAbsolutePath();
			}
		}
		return files;
	}

	// return a full path to the file
	public String getFile(int index) {
		int length = filelist.length;
		if (index < 0 || index >= length) {
			System.out.println("invalid index, please check input");
			return null;
		}
		if (filelist[index].isDirectory()) {
			System.out.println("Folder is not supported, please choose a file");
			return null;
		}
		return filelist[index].getAbsolutePath();
	}
}
