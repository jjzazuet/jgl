package org.jgl.opengl.util;

import org.jgl.os.NativeBootstrap;

import com.jogamp.common.jvm.JNILibLoaderBase;

public class JoglNativeBoostrap {

	public static void joglBootstrap() throws Exception {
		load("nativewindow_awt"); // TODO on some machines this library fails with UnsatisfiedLinkError	
		load("gluegen-rt");
		load("nativewindow_win32"); // TODO determine correct OS libraries to load at runtime.
		load("jogl_desktop"); 
	}
	
	public static void load(String libName) throws Exception {
		NativeBootstrap.loadNativeLibrary(libName);
		JNILibLoaderBase.addLoaded(libName);
	}
}