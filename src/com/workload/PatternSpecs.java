package com.workload;

import java.util.HashMap;
import java.util.Map;

public class PatternSpecs {
	private double Elapsed;
	private String PatternName;
	/**
	 * @return the elapsed
	 */
	private Map<String,String> PatternVariable;
	
	public PatternSpecs(String PatternName,double Elapsed) {
		this.Elapsed=Elapsed;
		this.PatternName=PatternName;
	}
	
	public PatternSpecs(Builder builder) {
		// TODO Auto-generated constructor stub
		this.PatternName=builder.getPatternName();
		this.Elapsed=builder.getElapsed();
		this.PatternVariable=builder.PatternVariable;
	}

	public double getElapsed() {
		return Elapsed;
	}

	/**
	 * @param elapsed the elapsed to set
	 */
	public void setElapsed(double elapsed) {
		Elapsed = elapsed;
	}

	/**
	 * @return the patternName
	 */
	public String getPatternName() {
		return PatternName;
	}

	/**
	 * @param patternName the patternName to set
	 */
	public void setPatternName(String patternName) {
		PatternName = patternName;
	}

	/**
	 * @return the patternVariable
	 */
	public Map<String,String> getPatternVariable() {
		return PatternVariable;
	}

	/**
	 * @param patternVariable the patternVariable to set
	 */
	public void setPatternVariable(Map<String,String> patternVariable) {
		PatternVariable = patternVariable;
	}
	public static class Builder {
		private double Elapsed;
		private String PatternName;
		public Map<String,String> PatternVariable=new HashMap<String,String>();
		
		public PatternSpecs  build() {
			PatternSpecs patternSpecs=new PatternSpecs(this);
			return patternSpecs;
			
		}
		/**
		 * @return the elapsed
		 */
		public double getElapsed() {
			return Elapsed;
		}
		/**
		 * @param elapsed the elapsed to set
		 */
		public Builder setElapsed(double elapsed) {
			Elapsed = elapsed;
			return this;
		}
		/**
		 * @return the patternName
		 */
		public String getPatternName() {
			return PatternName;
		}
		/**
		 * @param patternName the patternName to set
		 */
		public Builder setPatternName(String patternName) {
			this.PatternName = patternName;
			return this;
		}
		public Builder setPatternVariable(String Name,String Value) {
			this.PatternVariable.put(Name, Value);
			return this;
		}
		
	}
	
}
