package com.timss.inventory.service;

import java.util.Map;

import com.timss.inventory.vo.InvMatSnapshotDetailVO;
import com.timss.inventory.vo.InvMatSnapshotVO;
import com.yudean.itc.dto.Page;
import com.yudean.mvc.bean.userinfo.UserInfoScope;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatSnapshotService.java
 * @author: 890166
 * @createDate: 2014-11-20
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatSnapshotService {

    /**
     * @description: 库存快照主单查询
     * @author: 890166
     * @createDate: 2014-11-24
     * @param userInfo
     * @param imsv
     * @return
     * @throws Exception:
     */
    Page<InvMatSnapshotVO> queryMatSnapshotList(UserInfoScope userInfo, InvMatSnapshotVO imsv) throws Exception;

    /**
     * @description: 库存快照子单查询(快速查询)
     * @author: 890166
     * @createDate: 2014-11-24
     * @param userInfo
     * @param imsvd
     * @return
     * @throws Exception:
     */
    Page<InvMatSnapshotDetailVO> quickMatSnapshotSearch(UserInfoScope userInfo, InvMatSnapshotDetailVO imsdv,
            String quickSearch, String imsid) throws Exception;

    /**
     * @description:库存快照子单查询
     * @author: 890166
     * @createDate: 2014-11-24
     * @param userInfo
     * @param imsdv
     * @return
     * @throws Exception:
     */
    Page<InvMatSnapshotDetailVO> queryMatSnapshotDetailList(UserInfoScope userInfo, InvMatSnapshotDetailVO imsdv,
            String imsid) throws Exception;

    /**
     * @description:保存当前库存信息
     * @author: 890166
     * @createDate: 2014-11-20
     * @return
     * @throws Exception:
     */
    boolean saveAsSnapshot(UserInfoScope userInfo, Map<String, Object> paramMap) throws Exception;

}
