package net.tribe7.opengl;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL3.*;
import static javax.media.opengl.GL4.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GLConstants {

	public static final Set<Integer> GLTEXTURE_PIXEL_DATA_FORMAT = new HashSet<Integer>(
			Arrays.asList(new Integer[] { GL_RED, GL_RG, GL_RGB, GL_BGR, GL_RGBA,
			GL_BGRA, GL_DEPTH_COMPONENT, GL_DEPTH_STENCIL }));

	public static final Set<Integer> GL_TEXTURE_PIXEL_DATA_TYPE = new HashSet<Integer>(
			Arrays.asList(new Integer[] { GL_UNSIGNED_BYTE, GL_BYTE,
					GL_UNSIGNED_SHORT, GL_SHORT, GL_UNSIGNED_INT, GL_INT,
					GL_FLOAT, GL_UNSIGNED_BYTE_3_3_2,
					GL_UNSIGNED_BYTE_2_3_3_REV, GL_UNSIGNED_SHORT_5_6_5,
					GL_UNSIGNED_SHORT_5_6_5_REV, GL_UNSIGNED_SHORT_4_4_4_4,
					GL_UNSIGNED_SHORT_4_4_4_4_REV, GL_UNSIGNED_SHORT_5_5_5_1,
					GL_UNSIGNED_SHORT_1_5_5_5_REV, GL_UNSIGNED_INT_8_8_8_8,
					GL_UNSIGNED_INT_8_8_8_8_REV, GL_UNSIGNED_INT_10_10_10_2,
					GL_UNSIGNED_INT_2_10_10_10_REV }));

	public static final Set<Integer> GL_TEXTURE_BASE_INTERNAL_FORMAT = new HashSet<Integer>(
			Arrays.asList(new Integer[] { GL_DEPTH_COMPONENT, GL_DEPTH_STENCIL,
					GL_RED, GL_RG, GL_RGB, GL_RGBA }));

	public static final Set<Integer> GL_TEXTURE_SIZED_INTERNAL_FORMAT = new HashSet<Integer>(
			Arrays.asList(new Integer[] { GL_R8, GL_R8_SNORM, GL_R16,
					GL_R16_SNORM, GL_RG8, GL_RG8_SNORM, GL_RG16, GL_RG16_SNORM,
					GL_R3_G3_B2, GL_RGB4, GL_RGB5, GL_RGB8, GL_RGB8_SNORM,
					GL_RGB10, GL_RGB12, GL_RGB16_SNORM, GL_RGBA2, GL_RGBA4,
					GL_RGB5_A1, GL_RGBA8, GL_RGBA8_SNORM, GL_RGB10_A2,
					GL_RGB10_A2UI, GL_RGBA12, GL_RGBA16, GL_SRGB8,
					GL_SRGB8_ALPHA8, GL_R16F, GL_RG16F, GL_RGB16F, GL_RGBA16F,
					GL_R32F, GL_RG32F, GL_RGB32F, GL_RGBA32F,
					GL_R11F_G11F_B10F, GL_RGB9_E5, GL_R8I, GL_R8UI, GL_R16I,
					GL_R16UI, GL_R32I, GL_R32UI, GL_RG8I, GL_RG8UI, GL_RG16I,
					GL_RG16UI, GL_RG32I, GL_RG32UI, GL_RGB8I, GL_RGB8UI,
					GL_RGB16I, GL_RGB16UI, GL_RGB32I, GL_RGB32UI, GL_RGBA8I,
					GL_RGBA8UI, GL_RGBA16I, GL_RGBA16UI, GL_RGBA32I,
					GL_RGBA32UI }));

	public static final Set<Integer> GL_TEXTURE_COMPRESSED_INTERNAL_FORMAT = new HashSet<Integer>(
			Arrays.asList(new Integer[] { GL_COMPRESSED_RED, GL_COMPRESSED_RG,
					GL_COMPRESSED_RGB, GL_COMPRESSED_RGBA, GL_COMPRESSED_SRGB,
					GL_COMPRESSED_SRGB_ALPHA, GL_COMPRESSED_RED_RGTC1,
					GL_COMPRESSED_SIGNED_RED_RGTC1, GL_COMPRESSED_RG_RGTC2,
					GL_COMPRESSED_SIGNED_RG_RGTC2,
			/* GL_COMPRESSED_RGBA_BPTC_UNORM, GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM,
			 * GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT, GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT 
			 * TODO undefined constants? */
			}));

	public static final Set<Integer> GL_TEXTURE_TARGET = new HashSet<Integer>(Arrays.asList(new Integer[] { 
			GL_TEXTURE_1D, GL_TEXTURE_2D, GL_TEXTURE_3D,
			GL_TEXTURE_1D_ARRAY, GL_TEXTURE_2D_ARRAY, GL_TEXTURE_RECTANGLE, 
			GL_TEXTURE_CUBE_MAP, GL_TEXTURE_CUBE_MAP_ARRAY, GL_TEXTURE_BUFFER,
			GL_TEXTURE_2D_MULTISAMPLE, GL_TEXTURE_2D_MULTISAMPLE_ARRAY }));

	public static final Set<Integer> GL_TEXTURE_PARAMETER = new HashSet<Integer>(
			Arrays.asList(new Integer[] {
					GL_DEPTH_STENCIL_TEXTURE_MODE,
					GL_TEXTURE_BASE_LEVEL, GL_TEXTURE_COMPARE_FUNC,
					GL_TEXTURE_COMPARE_MODE, GL_TEXTURE_LOD_BIAS,
					GL_TEXTURE_MIN_FILTER, GL_TEXTURE_MAG_FILTER,
					GL_TEXTURE_MIN_LOD, GL_TEXTURE_MAX_LOD,
					GL_TEXTURE_MAX_LEVEL, GL_TEXTURE_SWIZZLE_R,
					GL_TEXTURE_SWIZZLE_G, GL_TEXTURE_SWIZZLE_B,
					GL_TEXTURE_SWIZZLE_A, GL_TEXTURE_WRAP_S, GL_TEXTURE_WRAP_T,
					GL_TEXTURE_WRAP_R }));

	public static final Set<Integer> GL_FRAMEBUFFER_TARGET = new HashSet<Integer>(
			Arrays.asList(new Integer[] { GL_DRAW_FRAMEBUFFER,
					GL_READ_FRAMEBUFFER, GL_FRAMEBUFFER }));

	public static final Set<Integer> GL_BUFFER_USAGE_HINT = new HashSet<Integer>(
			Arrays.asList(new Integer[] { GL_STREAM_DRAW, GL_STREAM_READ,
					GL_STREAM_COPY, GL_STATIC_DRAW, GL_STATIC_READ,
					GL_STATIC_COPY, GL_DYNAMIC_DRAW, GL_DYNAMIC_READ,
					GL_DYNAMIC_COPY }));

	public static final Set<Integer> GL_BIND_BUFFER_BASE_TARGET = new HashSet<Integer>(
			Arrays.asList(new Integer[] { GL_ATOMIC_COUNTER_BUFFER,
					GL_TRANSFORM_FEEDBACK_BUFFER, GL_UNIFORM_BUFFER,
					GL_SHADER_STORAGE_BUFFER }));
}
