package com.topology.primitives;

import com.topology.primitives.exception.properties.PropertyException;

public enum NetworkLayer {
	PHYSICAL(0, "Physical Connections"),
	OTS(1, "Optical Transmission Section"),
	IP(2, "IP Link");

	//Integer value for storing enum in database
	private final int id;
	
	//String description for enum
	private final String desc;
	
	NetworkLayer(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}
	
	public int getIntValue() {
		return id;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public NetworkLayer getLayerFromID(int id) throws PropertyException {
		for (NetworkLayer layer: NetworkLayer.values()) {
			if (layer.getIntValue()==id) {
				return layer;
			}
		}
		//No network layer found with specified id
		throw new PropertyException("Network Layer with ID: " + id + " not found in list of network layers");
	}
}
