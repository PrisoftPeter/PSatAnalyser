package gla.prisoft.shared;

import java.util.Arrays;

public class ArrayCleaner {

	public static String[] clean(String[] v) {
		int r, w;
		final int n = r = w = v.length;
		while (r > 0) {
			final String s = v[--r];
			if (s!=null && !s.equals("null")) {
				v[--w] = s;
			}
		}
		return Arrays.copyOfRange(v, w, n);
	}
	
	public static AssertionInstance[] clean(AssertionInstance[] v) {
		
		int r, w;
		final int n = r = w = v.length;
		while (r > 0) {
			final AssertionInstance s = v[--r];
			if (s!=null && !s.getAssertion().equals("null")) {
				v[--w] = s;
			}
		}
		return Arrays.copyOfRange(v, w, n);
	}
}
