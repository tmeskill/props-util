package com.espn.esd.propsutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ResultsDTO {

	List<Property> fileOneDuplicates = new ArrayList<Property>();
	List<Property> fileTwoDuplicates = new ArrayList<Property>();
	Set<Property> fileOneDeficiencies = new TreeSet<Property>();
	Set<Property> fileTwoDeficiencies = new TreeSet<Property>();

	public List<Property> getFileOneDuplicates() {
		return fileOneDuplicates;
	}

	public void setFileOneDuplicates(List<Property> fileOneDuplicates) {
		this.fileOneDuplicates = fileOneDuplicates;
	}

	public List<Property> getFileTwoDuplicates() {
		return fileTwoDuplicates;
	}

	public void setFileTwoDuplicates(List<Property> fileTwoDuplicates) {
		this.fileTwoDuplicates = fileTwoDuplicates;
	}

	public Set<Property> getFileOneDeficiencies() {
		return fileOneDeficiencies;
	}

	public void setFileOneDeficiencies(Set<Property> fileOneDeficiencies) {
		this.fileOneDeficiencies = fileOneDeficiencies;
	}

	public Set<Property> getFileTwoDeficiencies() {
		return fileTwoDeficiencies;
	}

	public void setFileTwoDeficiencies(Set<Property> fileTwoDeficiencies) {
		this.fileTwoDeficiencies = fileTwoDeficiencies;
	}

}
