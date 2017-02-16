package com.timss.finance.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.timss.finance.service.FinanceMainDetailService;

@Controller
@RequestMapping(value = "finance/financeDetailController")
public class FinanceDetailController {
    @Autowired
    FinanceMainDetailService financeMainDetailService;

    public FinanceMainDetailService getFinanceMainDetailService() {
        return financeMainDetailService;
    }

    public void setFinanceMainDetailService(FinanceMainDetailService financeMainDetailService) {
        this.financeMainDetailService = financeMainDetailService;
    }
    
    @RequestMapping(value = "/insertFinanceDetail")
    public Map<String, Object> insertFinanceDetail() throws Exception {

        return null;
    }

    @RequestMapping(value = "/deleteFinanceDetail")
    public Map<String, Object> deleteFinanceDetail() throws Exception {

        return null;
    }

    @RequestMapping(value = "/updateFinanceDetail")
    public Map<String, Object> updateFinanceDetail() throws Exception {

        return null;
    }
}
