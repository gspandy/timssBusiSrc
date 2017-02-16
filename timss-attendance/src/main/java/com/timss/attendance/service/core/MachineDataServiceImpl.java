package com.timss.attendance.service.core;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.timss.attendance.service.MachineDataService;

@Service("machineDataService")
public class MachineDataServiceImpl extends MachineDataBaseServiceImpl implements MachineDataService {
    private Logger log = Logger.getLogger( MachineDataServiceImpl.class );
    
}
