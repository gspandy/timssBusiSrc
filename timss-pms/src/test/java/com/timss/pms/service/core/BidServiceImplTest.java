package com.timss.pms.service.core;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Comment;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.yudean.mvc.testunit.TestUnit;
import com.yudean.workflow.web.ProcessInstController;
import com.timss.pms.bean.Bid;
import com.timss.pms.bean.BidCon;
import com.timss.pms.bean.BidMethod;
import com.timss.pms.bean.BidResult;
import com.timss.pms.service.BidService;
@ContextConfiguration(locations={"classpath:config/context/applicationContext-workflow.xml"})
@Transactional
public class BidServiceImplTest extends TestUnit{
    @Autowired
    BidService bidService;
    @Test
	public void test() {
    	Bid bid=new Bid();
    	bid.setProjectId(1068);
    	bid.setName("招标测试 站点信息");
    	bidService.insertBid(bid,null);
    	BidMethod bidMethod=new BidMethod();
    	bidMethod.setBidId(bid.getBidId());
    	bidMethod.setCommand("评标测试 站点信息");
    	bidService.insertBidMethod(bidMethod);
    	BidResult bidResult=new BidResult();
    	bidResult.setBidId(bid.getBidId());
    	bidResult.setCommand("招标结果 站点信息");
    	bidService.insertBidResult(bidResult,null);
    	
	}

}
