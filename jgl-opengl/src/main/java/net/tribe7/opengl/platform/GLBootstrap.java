package net.tribe7.opengl.platform;

import static net.tribe7.opengl.platform.GLIOUtils.*;

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
	public static final String JAWT = "jawt";
	private static final Logger log = LoggerFactory.getLogger(GLBootstrap.class);

	public GLBootstrap() throws Exception {

		System.setProperty("jogamp.gluegen.UseTempJarCache", "false");

		log.info("Initializing native JOGL jar dependencies for platform [{}]. Temp jar cache disabled.", 
				PlatformPropsImpl.os_and_arch);

		String nativeJarName = String.format("%s-%s", NATIVES, PlatformPropsImpl.os_and_arch);
		String [] classpathEntries = System.getProperty(JAVA_CLASSPATH).split(System.getProperty(JAVA_SEPARATOR));

		for (String jarPath : classpathEntries) {

			if (jarPath.contains(nativeJarName)) {

				if (log.isDebugEnabled()) {
					log.debug("Applicable platform jar: [{}]", jarPath);
				}

				JarFile jf = new JarFile(jarPath);

				try {
					Enumeration<JarEntry> jarEntries = jf.entries();

					while (jarEntries.hasMoreElements()) {

						JarEntry je = jarEntries.nextElement();

						if (!je.isDirectory() && !je.getName().contains(JAVA_META_INF)) {
							if (log.isDebugEnabled()) {
								log.debug("Mapping jar entry [{}] -> [{}]", je.getName(), jarPath);
							}
							if (log.isDebugEnabled() && platformNativeIndex.containsKey(je.getName())) {
								log.debug("Duplicate jar entry: [{}]", je.getName());
								log.debug("Mapped at: [{}]", platformNativeIndex.get(je.getName()));
								log.debug("Also at: [{}]", jarPath);
							}
							platformNativeIndex.put(je.getName(), jarPath);
						}
					}
				} finally { closeJar(jf); }
			}
		}
	}

	@Override
	public boolean loadLibrary(String libname, boolean ignoreError, ClassLoader cl) {		
		try {
			for (Entry<String, String> nativeEntry : platformNativeIndex.entrySet()) {
				if (nativeEntry.getKey().contains(libname)) {
					if (log.isDebugEnabled()) {
						log.debug("Loading mapped entry: [{}] [{}] [{}]", 
								libname, nativeEntry.getKey(), nativeEntry.getValue());
					}
					File nativeLibCopy = extractJarEntry(
							nativeEntry.getValue(), 
							nativeEntry.getKey(), 
							System.getProperty(JAVA_TMP_DIR), String.format("%s.jni", libname));
					System.load(nativeLibCopy.getAbsolutePath());
					return true;
				}
			}
		} catch (Exception e) {
			log.error("Unable to load native library [{}] - {}", libname, e);
		}

		if (log.isDebugEnabled()) {
			log.debug("No mapped library match for [{}]", libname);
		}
		return false;
	}

	@Override
	public void loadLibrary(String libname, String[] preload, boolean preloadIgnoreError, ClassLoader cl) {

		if (JAWT.compareTo(libname) == 0) {
			Runtime.getRuntime().loadLibrary("jawt");
			return;
		}

		if (null != preload) {
			for (int i = 0; i < preload.length; i++) {
				loadLibrary(preload[i], preloadIgnoreError, cl);
			}
		}
		loadLibrary(libname, false, cl);
	}
}
