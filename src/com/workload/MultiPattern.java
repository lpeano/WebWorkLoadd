package com.workload;

import java.util.ArrayList;

public class MultiPattern {
	private boolean ciclic=false;
	ArrayList<PatternSpecs> Pattern_Specs;
	public MultiPattern() {
		// TODO Auto-generated constructor stub
		this.Pattern_Specs=new ArrayList<PatternSpecs>();
		
	}
	/**
	 * @return the ciclic
	 */
	public boolean isCiclic() {
		return ciclic;
	}
	/**
	 * @param ciclic the ciclic to set
	 */
	public void setCiclic(boolean ciclic) {
		this.ciclic = ciclic;
	}
	public void addPatternSpec(PatternSpecs Pattern_Spec) {
		this.Pattern_Specs.add(Pattern_Spec);
		this.Pattern_Specs.forEach((PatternSpecs X)->{
			System.out.println(X.getPatternName());
		});
		
	}
	
	public static class Builder {
		public MultiPattern build() {
			MultiPattern multiPattern=new MultiPattern();
			return multiPattern;
			
		}
	}

	
}
