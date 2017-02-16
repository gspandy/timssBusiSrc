package com.timss.pms.service;

import java.util.List;

public interface SupplierService {
	List queryFuzzyByName(String name);
	
	Object querySupplierById(String id);
}
