package net.tribe7.demos.mchochlik.t027DepthOfField;

import static net.tribe7.math.matrix.MatrixOps.*;

import java.util.*;
import net.tribe7.geom.transform.ModelTransform;
import net.tribe7.math.matrix.io.BufferedMatrix4;

public class MakeCubeMatrices {

	public static final float RAND_MAX = 0x7fff;
	private Random r = new Random(59039);

	public List<BufferedMatrix4> apply(int count, float max_dist) {

		ModelTransform mt = new ModelTransform();
		List<BufferedMatrix4> offsets = new ArrayList<BufferedMatrix4>(count);

		for(int i=0; i!=count; ++i) {

			float x = r.nextFloat();
			float y = r.nextFloat();
			float z = r.nextFloat();
			float sx = r.nextInt() % 2 == 0 ? -1.0f: 1.0f;
			float sy = r.nextInt() % 2 == 0 ? -1.0f: 1.0f;
			float sz = r.nextInt() % 2 == 0 ? -1.0f: 1.0f;

			mt.getTranslation().set(
					sx*(1.0f + Math.pow(x, 0.9f) * max_dist),
					sy*(1.0f + Math.pow(y, 1.5f) * max_dist),
					sz*(1.0f + Math.pow(z, 0.7f) * max_dist));

			mt.getRotationZ().setRightAngles(r.nextInt()/RAND_MAX);
			mt.getRotationY().setRightAngles(r.nextInt()/RAND_MAX);
			mt.getRotationX().setRightAngles(r.nextInt()/RAND_MAX);

			BufferedMatrix4 offset = new BufferedMatrix4();

			copy(mt.getModelMatrix(), offset);
			offsets.add(i, offset);
		}

		return offsets;
	}
}
