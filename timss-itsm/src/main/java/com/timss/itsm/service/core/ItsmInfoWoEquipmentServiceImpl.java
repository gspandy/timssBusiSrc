package com.timss.itsm.service.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timss.itsm.bean.ItsmInfoWoEquipment;
import com.timss.itsm.dao.ItsmInfoWoEquipmentDao;
import com.timss.itsm.service.ItsmInfoWoEquipmentService;
import com.yudean.itc.dto.Page;

@Service
public class ItsmInfoWoEquipmentServiceImpl implements ItsmInfoWoEquipmentService {
    @Autowired
    private ItsmInfoWoEquipmentDao itsmInfoWoEquipmentDao;
    
    private static final Logger LOG = Logger.getLogger( ItsmInfoWoEquipmentServiceImpl.class );

    @Override
    public void deleteItsmInfoWoEquipment(String infoWoId) {
        itsmInfoWoEquipmentDao.deleteItsmInfoWoEquipment( infoWoId );
    }

    @Override
    public int insertItsmInfoWoEquipment(ItsmInfoWoEquipment itsmInfoWoEquipment) {
        return itsmInfoWoEquipmentDao.insertItsmInfoWoEquipment( itsmInfoWoEquipment );
    }

    @Override
    public List<ItsmInfoWoEquipment> queryItsmInfoWoEquipmentList(String infoWoId) {
        List<ItsmInfoWoEquipment> list = itsmInfoWoEquipmentDao.queryItsmInfoWoEquipmentList( infoWoId );
        return list;
    }

    @Override
    public void updateItsmInfoWoEquipment(ItsmInfoWoEquipment infoWoEquipment ) {
        itsmInfoWoEquipmentDao.updateItsmInfoWoEquipment( infoWoEquipment );
    }

    
}
