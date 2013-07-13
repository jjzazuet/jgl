package net.tribe7.mchochlik.test;

import static javax.media.opengl.GL.*;
import static org.junit.Assert.*;
import net.tribe7.opengl.GLBufferMetadata;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BufferMetadataTest {

	private static final Logger log = LoggerFactory.getLogger(BufferMetadataTest.class);
	
	@Test
	public void bufferMatedataTests() {
		
		log.info("Simple linear array of floats...");
		GLBufferMetadata md = new GLBufferMetadata(new int [] { 1 }, GL_FLOAT);
		
		assertTrue(md.getComponentCount() == 1);
		assertTrue(md.getComponentUnitSize(0) == 1);
		assertTrue(md.getComponentByteSize(0) == 4);
		assertTrue(md.getComponentByteOffset(0) == 0);
		assertTrue(md.getTotalComponentByteSize() == 4);
		
		log.info("Simple linear array of floats in chunks of three...");
		GLBufferMetadata float3 = new GLBufferMetadata(new int [] {3}, GL_FLOAT);
		
		assertTrue(float3.getComponentCount() == 1);
		assertTrue(float3.getComponentUnitSize(0) == 3);
		assertTrue(float3.getComponentByteSize(0) == 12);
		assertTrue(float3.getComponentByteOffset(0) == 0);
		assertTrue(float3.getTotalComponentByteSize() == 12);
		
		log.info("Linear array of floats, subdivided in chunks of float(3, vertex), float(2, texcoords)...");
		GLBufferMetadata floatV = new GLBufferMetadata(new int [] { 3, 2 }, GL_FLOAT);
		
		assertTrue(floatV.getComponentCount() == 2);
		assertTrue(floatV.getComponentUnitSize(0) == 3);
		assertTrue(floatV.getComponentUnitSize(1) == 2);
		assertTrue(floatV.getComponentByteSize(0) == 12);
		assertTrue(floatV.getComponentByteSize(1) == 8);
		assertTrue(floatV.getComponentByteOffset(0) == 0);
		assertTrue(floatV.getComponentByteOffset(1) == 12);
		assertTrue(floatV.getTotalComponentByteSize() == 20);
		
		log.info("Linear array of ints...");
		GLBufferMetadata intl = new GLBufferMetadata(new int [] {1}, GL_UNSIGNED_INT);
		
		assertTrue(intl.getComponentCount() == 1);
		assertTrue(intl.getComponentUnitSize(0) == 1);
		assertTrue(intl.getComponentByteSize(0) == 4);
		assertTrue(intl.getComponentByteOffset(0) == 0);	
	}
}
