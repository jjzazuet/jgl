package net.tribe7.opengl.util;

import jogamp.common.os.PlatformPropsImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.jvm.*;
import com.jogamp.common.jvm.JNILibLoaderBase.LoaderAction;

public class GLBootstrap implements LoaderAction {

	public static final String JAVA_CLASSPATH = "java.class.path";
	public static final String JAVA_SEPARATOR = "path.separator";
	public static final String NATIVES = "natives";

	private static final Logger log = LoggerFactory.getLogger(GLBootstrap.class);

	public void joglBootstrap() throws Exception {
		System.setProperty("jogamp.gluegen.UseTempJarCache", "false");
		JNILibLoaderBase.setLoadingAction(this);
	}

	@Override
	public boolean loadLibrary(String libname, boolean ignoreError,
			ClassLoader cl) {

		String nativeJarName = String.format("%s-%s", NATIVES, PlatformPropsImpl.os_and_arch);
		String [] classpathEntries = System.getProperty(JAVA_CLASSPATH).split(System.getProperty(JAVA_SEPARATOR));

		// TODO create classpath jar index for library querying
		for (String jarEntry : classpathEntries) {
			if (jarEntry.contains(libname) && jarEntry.contains(nativeJarName)) {
				log.info(jarEntry);
			}
		}
		
		return false;
	}

	@Override
	public void loadLibrary(String libname, String[] preload,
			boolean preloadIgnoreError, ClassLoader cl) {
		// TODO Auto-generated method stub
		log.info(libname);
	}
}
