package com.timss.pms.service.core;

import static org.junit.Assert.*;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.timss.pms.service.SequenceService;
import com.yudean.itc.manager.support.ISequenceManager;
import com.yudean.mvc.testunit.TestUnit;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})

public class SequeceServiceImplTest extends TestUnit{

	@Autowired
	SequenceService sequenceService;
	@Autowired
	ISequenceManager iSequenceManager;
	@Test
	@Transactional
	public void test() {

//		String idString="TEST_B_ID";
//		String code=iSequenceManager.getSequenceId(idString);
//		System.out.println(code);
//		iSequenceManager.getGeneratedId(idString);
//		code=iSequenceManager.getSequenceId(idString);
//		System.out.println(code);
		String code=sequenceService.createContractSequenceService("","");
		System.out.println(code);
		sequenceService.updateContractSequence(code);
		System.out.println(sequenceService.createContractSequenceService("",""));
	}

}
