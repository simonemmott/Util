package com.k2.Util;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtilExample {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtilExample.class);
	
	public static void main(String[] args) {
		
		File testingLocation = new File("example"+File.separatorChar+"root");
		FileUtil.deleteCascade(testingLocation);
		
		logger.info("Creating directory example/new");
		testingLocation.mkdir();
		
		logger.info("Building file tree");
		FileUtil.buildTree(testingLocation,
				Paths.get("dir1"),
				Paths.get("dir2"),
				Paths.get("dir2", "childDir1"),
				Paths.get("dir2", "childDir2")
				);

		
	}

}
