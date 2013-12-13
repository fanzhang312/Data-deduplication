public class RunDedup {

	/**
	 * Main function: Require user input type of chunking and the directory
	 * 
	 * @author Fan Zhang, Zhiqi Chen
	 * 
	 */
	public static void main(String[] args) {
		int length = args.length;
		FixedSizeChunking fix = null;
		FileBasedChunking file = null;
		RabinKarpRollingHash rabin = null;
		try {
			if (length == 0) {
				throw new RuntimeException("Keyword is needed.");
			} else if (length == 1) {
				throw new RuntimeException("Two keywords are needed");
			} else if (length == 2) {
				switch (Integer.parseInt(args[0])) {
				case 1:
					fix = new FixedSizeChunking(args[1]);
					fix.setAll(fix.list.filelist);
					break;
				case 2:
					file = new FileBasedChunking(args[1]);
					file.setAll(file.list.filelist);
					break;
				case 3:
					rabin = new RabinKarpRollingHash(args[1]);
					rabin.setAll(rabin.list.filelist);
					break;
				default:
					throw new RuntimeException(
							"Please enter 1 : Fixed size chunking; 2 : File based chunking; 3 : Content defined chunking");
				}
			} else {
				throw new RuntimeException(
						"Too many parameters, only chunking type and directory are needed.");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
	}

}
