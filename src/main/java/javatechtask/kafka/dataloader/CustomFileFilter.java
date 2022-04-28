package javatechtask.kafka.dataloader;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomFileFilter implements FileFilter {
	private List<String> endsWith;
	private List<String> startsWith;
	

	@Override
	public boolean accept(File arg0) {
	    if (arg0.isFile() && 
	    		isEndsWith(arg0.getName()) &&
	    		isStartsWith(arg0.getName())) {
	    	return true;
	    }
		return false;
	}


	private boolean isStartsWith(String name) {
		if (startsWith == null || startsWith.isEmpty()) {
			return true;
		}
		
		for (String item : startsWith) {
			if (name.toLowerCase().startsWith(item.toLowerCase())) {
				return true;
			}
		}
		
		return false;
	}


	private boolean isEndsWith(String name) {
		if (endsWith == null || endsWith.isEmpty()) {
			return true;
		}
		
		for (String item : endsWith) {
			if (name.toLowerCase().endsWith(item.toLowerCase())) {
				return true;
			}
		}
		
		return false;
	}

}
