package net.tribe7.opengl.util;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Scanner;

import static net.tribe7.common.base.Throwables.*;
import static net.tribe7.common.base.Preconditions.*;

public class GLIOUtils {

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
}
