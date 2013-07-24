package net.tribe7.demos.mchochlik.t030CubeMapping;

import java.util.ArrayList;
import java.util.List;

import net.tribe7.math.vector.Vector3;

public class MakeCubeOffsets {

	public List<Vector3> apply(double distance, int cubes_per_side) {

		int instance_count = 2 * cubes_per_side * cubes_per_side + 2
				* cubes_per_side * (cubes_per_side - 2) + 2
				* (cubes_per_side - 2) * (cubes_per_side - 2);

		List<Vector3> offsets = new ArrayList<Vector3>(instance_count);

		int cpf = cubes_per_side * cubes_per_side;
		int cpe = cubes_per_side - 2;
		int cpl = 2 * cubes_per_side + 2 * cpe;
		int cpi = cpl * cpe;

		for (int i = 0; i != instance_count; ++i) {

			int id = i;
			float omax = (float) ((cubes_per_side - 1) * 0.5);
			float imax = (float) ((cpe - 1) * 0.5);
			float dx, dy, dz;

			if (id < cpf) {
				dx = -omax;
				dy = (id / cubes_per_side) - omax;
				dz = (id % cubes_per_side) - omax;
			} else if (id < cpf + cpi) {
				id -= cpf;
				dx = (id / cpl) - imax;
				id = id % cpl;
				if (id < cubes_per_side) {
					dy = -omax;
					dz = id - omax;
				} else if (id < cubes_per_side + cpe * 2) {
					id -= cubes_per_side;
					dy = -imax + id / 2;
					dz = (id % 2 == 1) ? omax : -omax;
				} else {
					id -= cubes_per_side + cpe * 2;
					dy = omax;
					dz = id - omax;
				}
			} else {
				id -= cpf + cpi;
				dx = omax;
				dy = (id / cubes_per_side) - omax;
				dz = (id % cubes_per_side) - omax;
			}
			offsets.add(i, new Vector3(dx * distance, dy * distance, dz * distance));
		}
		return offsets;
	}
}
