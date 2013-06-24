package org.jgl.opengl.util;

import static org.jgl.os.SystemSpec.*;
import org.jgl.os.NativeBootstrap;

import com.jogamp.common.jvm.JNILibLoaderBase;

public class JoglNativeBoostrap {

	public static void joglBootstrap() throws Exception {	
		
		load("gluegen-rt");
		
		if (isLinux()) {
			load("nativewindow_x11");
		} else if (isWindows()) {
			load("nativewindow_win32");
		}

		load("jogl_desktop"); 
	}

	public static void load(String libName) throws Exception {
		NativeBootstrap.loadNativeLibrary(libName);
		JNILibLoaderBase.addLoaded(libName);
	}
}