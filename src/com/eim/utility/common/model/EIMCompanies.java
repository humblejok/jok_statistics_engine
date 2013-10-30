package com.eim.utility.common.model;

/**
 * An enumeration that lists the available frequencies
 * @author sdj
 *
 */
public enum EIMCompanies {
	EIM_SWITZERLAND, EIM_UK, EIM_PAPER_TRADED;

	@Override
	public String toString() {
		switch (this) {
			case EIM_SWITZERLAND:
				return "EIM SWITZERLAND";
			case EIM_UK:
				return "EIM UK";
			case EIM_PAPER_TRADED:
				return "EIM PAPER TRADED";
		}
		return "";
	};
}
