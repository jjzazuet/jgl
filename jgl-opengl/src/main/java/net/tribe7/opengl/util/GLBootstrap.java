package net.tribe7.opengl.util;

import static java.lang.String.format;
import static net.tribe7.opengl.util.GLIOUtils.*;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import java.util.jar.*;

import jogamp.common.os.PlatformPropsImpl;
import org.slf4j.*;
import com.jogamp.common.jvm.JNILibLoaderBase.LoaderAction;

public class GLBootstrap implements LoaderAction {

	private Map<String, String> platformNativeIndex = new HashMap<String, String>();

	public static final String JAVA_TMP_DIR = "java.io.tmpdir";
	public static final String JAVA_CLASSPATH = "java.class.path";
	public static final String JAVA_SEPARATOR = "path.separator";
	public static final String JAVA_META_INF = "META-INF";
	public static final String NATIVES = "natives";

	private static final Logger log = LoggerFactory.getLogger(GLBootstrap.class);

	public GLBootstrap() throws Exception {

		System.setProperty("jogamp.gluegen.UseTempJarCache", "false");

		log.info(format("Initializing native JOGL jar dependencies for platform [%s]. Temp jar cache disabled.", 
				PlatformPropsImpl.os_and_arch));

		String nativeJarName = String.format("%s-%s", NATIVES, PlatformPropsImpl.os_and_arch);
		String [] classpathEntries = System.getProperty(JAVA_CLASSPATH).split(System.getProperty(JAVA_SEPARATOR));

		for (String jarPath : classpathEntries) {

			if (jarPath.contains(nativeJarName)) {

				if (log.isDebugEnabled()) {
					log.debug(format("Applicable platform jar: [%s]", jarPath));
				}

				JarFile jf = new JarFile(jarPath);

				try {
					Enumeration<JarEntry> jarEntries = jf.entries();

					while (jarEntries.hasMoreElements()) {

						JarEntry je = jarEntries.nextElement();

						if (!je.isDirectory() && !je.getName().contains(JAVA_META_INF)) {
							if (log.isDebugEnabled()) {
								log.debug(format("Mapping jar entry [%s] -> [%s]", je.getName(), jarPath));
							}
							if (log.isDebugEnabled() && platformNativeIndex.containsKey(je.getName())) {
								log.debug(format("Duplicate jar entry: [%s]", je.getName()));
								log.debug(format("Mapped at: [%s]", platformNativeIndex.get(je.getName())));
								log.debug(format("Also at: [%s]", jarPath));
							}
							platformNativeIndex.put(je.getName(), jarPath);
						}
					}
				} finally { jf.close(); }
			}
		}
	}

	@Override
	public boolean loadLibrary(String libname, boolean ignoreError, ClassLoader cl) {		
		try {
			for (Entry<String, String> nativeEntry : platformNativeIndex.entrySet()) {
				if (nativeEntry.getKey().contains(libname)) {
					if (log.isDebugEnabled()) {
						log.debug(format("Loading mapped entry: [%s] [%s] [%s]", 
								libname, nativeEntry.getKey(), nativeEntry.getValue()));
					}

					JarFile jf = new JarFile(nativeEntry.getValue());
					JarEntry je = jf.getJarEntry(nativeEntry.getKey());
					File tempDir = new File(System.getProperty(JAVA_TMP_DIR));
					File temp = new File(tempDir, format("%s.jogl", libname));

					try {
						if (log.isDebugEnabled()) {
							log.debug(format("Extracting to file [%s]", temp.getAbsolutePath()));
						}
						if (temp.createNewFile()) {
							copyToFile(jf.getInputStream(je), temp);
						}
						System.load(temp.getAbsolutePath());
						return true;
					} finally { jf.close(); }
				}
			}
		} catch (Exception e) {
			log.error(format("Unable to load native library [%s]", libname), e);
		}

		if (log.isDebugEnabled()) {
			log.debug(format("No mapped library match for [%s]", libname));
		}
		return false;
	}

	@Override
	public void loadLibrary(String libname, String[] preload, boolean preloadIgnoreError, ClassLoader cl) {
		if (null != preload) {
			for (int i = 0; i < preload.length; i++) {
				loadLibrary(preload[i], preloadIgnoreError, cl);
			}
		}
		loadLibrary(libname, false, cl);
	}
}
