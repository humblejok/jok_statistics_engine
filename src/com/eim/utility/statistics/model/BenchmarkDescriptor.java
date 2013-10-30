package com.eim.utility.statistics.model;


/**
 * Object class that describes a benchmark.<BR/>
 * Benchmarks do not depend on the entity type they are linked to.
 * @author sdj
 *
 */
public class BenchmarkDescriptor {

	/**
	 * The benchmark name
	 */
	private String name;
	/**
	 * The benchmark id
	 */
	private String id;
	
	/**
	 * Is the benchmark a risk one
	 */
	private boolean rfr;
	
	/**
	 * Default empty constructor
	 */
	public BenchmarkDescriptor() {
	}
	
	/**
	 * Constructor
	 * @param name The name
	 * @param id The id
	 * @param rfr Is it a risk free benchmark
	 */
	public BenchmarkDescriptor(String name, String id, boolean rfr) {
		super();
		this.name = name;
		this.id = id;
		this.rfr = rfr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean getRfr() {
		return rfr;
	}

	public void setRfr(boolean rfr) {
		this.rfr = rfr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (rfr ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BenchmarkDescriptor other = (BenchmarkDescriptor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rfr != other.rfr)
			return false;
		return true;
	}
	
}
