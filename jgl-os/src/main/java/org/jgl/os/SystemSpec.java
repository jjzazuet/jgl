package org.jgl.os;

public class SystemSpec {

	public static final String FORMAT_WIN_LIB = "%s.dll";
	public static final String FORMAT_NIX_LIB = "lib%s.so";
	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");

	protected static final String osName = System.getProperty("os.name").toLowerCase();
	protected static final String osArch = System.getProperty("os.arch").toLowerCase();
	protected static final boolean windows = osName.contains("windows");
	protected static final boolean linux = osName.contains("linux");
	protected static final boolean bits32 = osArch.contains("86");
	protected static final boolean bits64 = osArch.contains("64");
	
	public static String getOsname() { return osName; }
	public static String getOsarch() { return osArch; }
	
	public static boolean isWindows() { return windows; }
	public static boolean isLinux() { return linux; }
	public static boolean isBits32() { return bits32; }
	public static boolean isBits64() { return bits64; }
}
