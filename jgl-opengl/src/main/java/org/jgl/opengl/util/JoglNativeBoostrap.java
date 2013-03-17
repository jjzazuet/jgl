package org.jgl.opengl.util;

import org.jgl.os.NativeBootstrap;

import com.jogamp.common.jvm.JNILibLoaderBase;

public class JoglNativeBoostrap {

	public static void joglBootstrap() throws Exception {
		load("gluegen-rt");
		load("nativewindow_win32");
		load("nativewindow_awt");	
		load("jogl_desktop");
	}
	
	public static void load(String libName) throws Exception {
		NativeBootstrap.loadNativeLibrary(libName);
		JNILibLoaderBase.addLoaded(libName);
	}
}
