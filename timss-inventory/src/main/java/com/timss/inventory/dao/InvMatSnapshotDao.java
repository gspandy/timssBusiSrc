package com.timss.inventory.dao;

import java.util.List;
import java.util.Map;

import com.timss.inventory.bean.InvMatSnapshot;
import com.timss.inventory.vo.InvMatSnapshotDetailVO;
import com.timss.inventory.vo.InvMatSnapshotVO;
import com.yudean.itc.dto.Page;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatSnapshotDao.java
 * @author: 890166
 * @createDate: 2014-11-20
 * @updateUser: 890166
 * @version: 1.0
 */
public interface InvMatSnapshotDao {

    /**
     * @description:保存当前库存信息
     * @author: 890166
     * @createDate: 2014-11-20
     * @return
     * @throws Exception:
     */
    int insertInvMatSnapshot(InvMatSnapshot ims);

    /**
     * @description:保存当前库存信息(子表)
     * @author: 890166
     * @createDate: 2014-11-24
     * @param map
     * @return:
     */
    int insertInvMatSnapshotDetail(Map<String, Object> map);

    /**
     * @description: 查询主表数据
     * @author: 890166
     * @createDate: 2014-11-24
     * @param page
     * @return:
     */
    List<InvMatSnapshotVO> queryMatSnapshotList(Page<InvMatSnapshotVO> page);

    /**
     * @description:快速查询
     * @author: 890166
     * @createDate: 2014-11-24
     * @param page
     * @return:
     */
    List<InvMatSnapshotDetailVO> quickMatSnapshotSearch(Page<InvMatSnapshotDetailVO> page);

    /**
     * @description: 查询子表数据
     * @author: 890166
     * @createDate: 2014-11-24
     * @param page
     * @return:
     */
    List<InvMatSnapshotDetailVO> queryMatSnapshotDetailList(Page<InvMatSnapshotDetailVO> page);

}
