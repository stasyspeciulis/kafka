package javatechtask.kafka.dataloader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager {
	private static Logger log = LoggerFactory.getLogger(FileManager.class);

	public static List<File> prepareFiles(String sourceDiretoryFullName, String batchName,
			List<String> endsWith, List<String> startsWith) {
		List<File> files = getFiles(sourceDiretoryFullName, endsWith, startsWith);
		if (files == null || files.isEmpty()) {
			return null;
		}

		String batchPath = createDirectory(sourceDiretoryFullName, batchName);

		List<File> movedFiles = moveFiles(files, batchPath);

		return movedFiles;
	}

	private static String createDirectory(String parentDirectoryName, String name) {
		Path path = Paths.get(parentDirectoryName, name);
		try {
			Files.createDirectory(path);
		} catch (IOException e) {
			throw new RuntimeException("Can not create directory " + path.toString(), e);
		}
		return path.toString();
	}

	private static List<File> getFiles(String sourceDiretoryFullName, List<String> endsWith, List<String> startsWith) {
		File sourceDirectory = new File(sourceDiretoryFullName);
		File[] files = sourceDirectory.listFiles(new CustomFileFilter(endsWith, startsWith));
		return Arrays.asList(files);
	}


	private static List<File> moveFiles(List<File> files, String destinationDirectoryFullName) {
		if (files == null || files.isEmpty()) {
			return null;
		}

		List<File> result = new ArrayList<>();

		for (File file : files) {
			try {
				File destinationFile = new File(destinationDirectoryFullName, file.getName());
				Files.move(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				result.add(destinationFile);
			} catch (IOException e) {
				log.error("Unable to move file {} to the directory {}", file.getPath(), destinationDirectoryFullName);
				log.error("{}", e);
			}
		}

		return result;
	}

	public static List<String> readLines(File file, Charset charset) {
		List<String> result = new ArrayList<>();

		try (Stream<String> stream = Files.lines(file.toPath(), charset)) {
			// add not empty lines and discard the others
			stream.map(String::trim).filter(v -> Strings.isNotEmpty(v)).forEach(result::add);
		} catch (IOException e) {
			log.error("Unable to read file {}", file.getPath());
			log.error("{}", e);
			return null;
		}

		return result;
	}
}
