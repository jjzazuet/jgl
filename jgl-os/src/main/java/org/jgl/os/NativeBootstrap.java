package org.jgl.os;

import static java.lang.String.*;
import static org.jgl.os.SystemSpec.*;
import static org.jgl.os.io.IoUtils.*;

import java.io.File;
import java.net.URL;

public class NativeBootstrap {

	public static final String ERROR_UNSUPPORTED_OS = "native.unsupported.os";
	
	public static void loadNativeLibrary(String rawName) throws Exception {

		String libFileName = null;

		if (isWindows()) {
			libFileName = format(FORMAT_WIN_LIB, rawName);
		} else if (isLinux()) {
			libFileName = format(FORMAT_NIX_LIB, rawName);
		} else throw new IllegalStateException(ERROR_UNSUPPORTED_OS);

		URL location = NativeBootstrap.class.getClassLoader().getResource(libFileName);
		File tmpDir = new File(TMP_DIR);
		File libTempFile = new File(tmpDir, libFileName);

		copyTofile(location, libTempFile);
		System.load(libTempFile.getAbsolutePath());
	}
}
