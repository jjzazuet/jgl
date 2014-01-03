package net.tribe7.opengl.platform;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.tribe7.common.base.Throwables.*;
import static net.tribe7.common.base.Preconditions.*;
import static net.tribe7.math.Preconditions.*;

public class GLIOUtils {

	private static final Logger log = LoggerFactory.getLogger(GLIOUtils.class);

	public static void copyToFile(InputStream in, File out) throws Exception {
		copyToFile(in, new FileOutputStream(out));
	}

	public static void copyTofile(URL in, File out) throws Exception {
		copyToFile(in.openStream(), new FileOutputStream(out));
	}

	public static void copyToFile(InputStream in, OutputStream out) throws Exception {

		checkNotNull(in);
		checkNotNull(out);

		ReadableByteChannel bi = null;
		WritableByteChannel bo = null;

		try {
			bi = Channels.newChannel(in);
			bo = Channels.newChannel(out);
			channelCopy(bi, bo);
		} finally {
			closeChannel(bi);
			closeChannel(bo);
		}
		
	}

	public static void channelCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {

		checkNotNull(src);
		checkNotNull(dest);

		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

		while (src.read(buffer) != -1) {
			buffer.flip();
			dest.write(buffer);
			buffer.compact();
		}

		buffer.flip();

		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}

	public static void closeChannel(Channel c) {
		if (c != null) {
			try { c.close(); } 
			catch (Exception e) { propagate(e) ; }
		}
	}

	public static void closeScanner(Scanner s) {
		if (s != null) {
			try { s.close(); }
			catch (Exception e) { propagate(e) ; }
		}
	}

	public static void closeJar(JarFile j) {
		if (j != null) {
			try { j.close(); }
			catch (Exception e) { propagate(e) ; }
		}
	}

	public static String readUrl(URL u, Charset c) {

		Scanner s = null;
		String result = "";

		try {
			s = new Scanner(u.openStream(), c.name()).useDelimiter("\\A");
			result = s.next();
		} 
		catch (Exception e) { propagate(e); } 
		finally { closeScanner(s); }
		return result;
	}

	public static File extractJarEntry(String jarPath, String jarEntryName,
			String targetDirName, String targetFileName) throws Exception {

		checkNoNulls(jarPath, jarEntryName, targetDirName);

		if (targetFileName == null) { targetFileName = jarEntryName; }

		if (log.isTraceEnabled()) {
			log.trace("Jar: [{}], entry [{}]", jarPath, jarEntryName);
		}

		JarFile jf = null;

		try {
			jf = new JarFile(jarPath);
			JarEntry je = jf.getJarEntry(jarEntryName);
			File targetDir = new File(targetDirName);
			File targetFile = new File(targetDir, targetFileName);

			checkState(je != null, "Jar entry not found: [%s]", jarEntryName);
			checkState(targetDir.exists(), "Invalid target dir: [%s]", targetDirName);

			if (log.isTraceEnabled()) {
				log.trace("Target file: [{}]", targetFile.getAbsolutePath());
				log.trace("Jar entry size: [{}] bytes", je.getSize());
				if (targetFile.exists()) { log.trace("Existing file: [{}] bytes", targetFile.length()); }
			}

			boolean copyFile = (!targetFile.exists())
					|| (targetFile.exists() && targetFile.length() != je.getSize());

			if (copyFile && targetFile.createNewFile()) {
				copyToFile(jf.getInputStream(je), targetFile);
			}

			checkState(targetFile.exists(), "Target file [%s] not written.", targetFile.getAbsolutePath());
			return targetFile;

		} finally { closeJar(jf); }
	}
}
