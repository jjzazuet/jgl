package net.tribe7.math;

import static net.tribe7.common.base.Preconditions.*;

public class Preconditions {

	/**
	 * Ensures that object references passed as parameters to the calling
	 * method are not null.
	 * @param values an array of object references
	 * @throws NullPointerException
	 *             if {@code values}, or any of its references are null
	 */
	public static <T> void checkNoNulls(T... values) {
		checkNotNull(values);
		for (T t : values) {
			checkNotNull(t);
		}
	}
}
