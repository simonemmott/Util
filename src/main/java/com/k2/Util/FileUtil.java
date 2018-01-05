package com.k2.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.invoke.MethodHandles;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Util.exceptions.FileLockedException;
import com.k2.Util.exceptions.UtilityError;

/**
 * This static utility provides methods for handling Files
 * 
 * @author simon
 *
 */
public class FileUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
		
	/**
	 * This method creates a directory tree structure as defined by the given paths
	 * 
	 * @param root	The root directory in which to create the directory tree. If this location is not a directory a UtilityError will be thrown
	 * @param paths	An array of paths defining all the leaf nodes required in the resultant directory tree
	 * @return	Null if the root path is null otherwise a File representing the root of the created directory tree.
	 */
	public static File buildTree(File root, Path ... paths) {
		if (root == null) return null;
		if (!root.exists()) {
		}
		if (root.isFile()) {
			UtilityError err = new UtilityError("The given root directory '{}' is not a directory", root.getAbsolutePath()) ;
			logger.error("The root is not a directory", err);
			throw err;
		}
		
		for (Path path : paths) {
			File f = new File(root.getAbsolutePath()+File.separatorChar+path.toString());
			if (!f.exists()) {
				logger.debug("Building path '{}'", f.getAbsolutePath());
				f.mkdirs();
			}
		}
		
		return root;
	}
	
	/**
	 * This method returns the file with the given name from the given directory if it exists
	 * @param dir	The directory to search for the file
	 * @param search		The name of the file to find
	 * @return	The file in the directory with the given name if it exists or null
	 */
	public static File fetch(File dir, String search) {
		if (!dir.isDirectory()) return null;
		if (!dir.canRead()) return null;
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.equals(search);
			}
		});
		if (files.length > 0) return files[0];
		return null;
	}
	/**
	 * This method returns a list of file from the given directory for the given extension
	 * @param dir	The directory to search for files with the given extension
	 * @param exts	The list of extensions to compare to files in the directory with or without the period '.'
	 * @return	A list of files in the given directory that end with any of the given file extensions
	 */
	public static List<File> listForExtension(File dir, String ... exts) {
		if (!dir.isDirectory()) return null;
		if (!dir.canRead()) return null;
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				for (String ext : exts) {
					if (!ext.startsWith(".")) ext = "."+ext;
					if (name.toLowerCase().endsWith(ext.toLowerCase())) return true;
				}
				return false;
			}
		});
		return new ArrayList<File>(Arrays.asList(files));
	}
	/**
	 * This method deletes the given file and if it is a directory recursively deletes all its contents
	 * @param f	The file or directory to delete
	 */
	public static void deleteCascade(File f) {
		if (f == null) return;
		
		if (!f.exists()) return;
		
		if (f.isFile()) {
			f.delete();
			return;
		}
		
		if (f.isDirectory()) {
			for (File child : f.listFiles()) {
				deleteCascade(child);
				child.delete();
			}
			f.delete();
			return;
		}
	}
	
	/**
	 * This method attempts to lock the given file using the FileLock API and returns the FileLock that locked the file
	 * @param f	The file to lock
	 * @return	The FileLock that is locking the file
	 * @throws FileLockedException	If the file is already locked
	 */
	public static FileLock lock(File f) throws FileLockedException {
		if (f.exists()) {
			RandomAccessFile raf = null;
			FileChannel fc = null;
			FileLock lock = null;
			try {
				try {
					raf = new RandomAccessFile(f, "rwd");
				} catch (FileNotFoundException e) {
					logger.error("This shouldn't happen!", e);
				}
				fc = raf.getChannel();
				
				lock = fc.tryLock();
				if (lock != null) {
					return lock;
				} else {
					throw new FileLockedException(f);
				}
				
			} catch (IOException e) {
				throw new FileLockedException(f);
			} 
//			finally {
//				if (raf != null)
//					try {
//						raf.close();
//					} catch (IOException e) {
//						UtilsLogger.error("Unable to close '"+f.getAbsolutePath()+"'", e);
//					}
//			}
		}
		throw new FileLockedException(f);
	}
	/**
	 * This method checks whether the given file is locked using the FileLock API
	 * @param f	The file to check if it is locked
	 * @return	True if the file is already locked
	 */
	public static boolean isLocked(File f) {
		if (f.exists()) {
			RandomAccessFile raf = null;
			FileChannel fc = null;
			FileLock lock = null;
			try {
				raf = new RandomAccessFile(f, "rwd");
				fc = raf.getChannel();
				
				lock = fc.tryLock();
				if (lock != null) return false;
				
			} catch (OverlappingFileLockException e) {
				return true;
			} catch (Exception e) {
				logger.error("Error checking file lock", e);
			} finally {
				if (lock != null)
					try {
						lock.release();
					} catch (IOException e) {
						logger.error("Unable to release lock on '"+f.getAbsolutePath()+"'", e);
					}
				if (fc != null)
					try {
						fc.close();
					} catch (IOException e) {
						logger.error("Unable to close file channel on '"+f.getAbsolutePath()+"'", e);
					}
				if (raf != null)
					try {
						raf.close();
					} catch (IOException e) {
						logger.error("Unable to close '"+f.getAbsolutePath()+"'", e);
					}
			}
		}
		return false;
	}

}
