package gla.prisoft.shared;

import java.util.ArrayList;
import java.util.Arrays;

import gla.prisoft.server.kernel.knowledge.worlds.World;

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
	
//	public static AssertionInstance[] clean(AssertionInstance[] v) {
//		
//		int r, w;
//		final int n = r = w = v.length;
//		while (r > 0) {
//			final AssertionInstance s = v[--r];
//			if (s!=null && !s.getAssertion().equals("null")) {
//				v[--w] = s;
//			}
//		}
//		return Arrays.copyOfRange(v, w, n);
//	}
	
	public static AssertionInstance[] clean(AssertionInstance[] v) {
		ArrayList<AssertionInstance> ainstances = new ArrayList<AssertionInstance>();

		for(AssertionInstance i: v){
			if(i !=null){
				boolean contained = false;

				for(AssertionInstance a:ainstances){
					if(a.getAssertion().equals(i.getAssertion())){
						contained = true;
						break;
					}
				}
				if(!contained){
					ainstances.add(i);
				}
			}
			
		}
		
		AssertionInstance [] aainstances = new AssertionInstance[ainstances.size()];
		aainstances = ainstances.toArray(aainstances);
		
		return aainstances;
	}
	
	public static World[] clean(World[] v) {
		ArrayList<World> ainstances = new ArrayList<World>();

		for(World i: v){
			boolean contained = false;

			for(World a:ainstances){
				if(a.toString().equals(i.toString())){
					contained = true;
					break;
				}
			}
			if(!contained){
				ainstances.add(i);
			}
		}
		
		World [] aainstances = new World[ainstances.size()];
		aainstances = ainstances.toArray(aainstances);
		
		return aainstances;
	}
	
	
	
	
	
}
