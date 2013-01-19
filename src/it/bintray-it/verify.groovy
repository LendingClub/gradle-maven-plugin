
// This file will check that test.txt was created by gradle

File f = new File(basedir,"output.txt");

if (!f.exists()) {
	throw new FileNotFoundException("File not found: "+f.getAbsolutePath())
}