package com.lin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.common.Result;
import com.lin.common.pojo.Announcement;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-25
 */
public interface AnnouncementService extends IService<Announcement> {

    Result getByPalteIdAndModularsId(String palteId, String modularsId);

    Result listAnnouncementVo(String palteId);

    Result saveAnnouncement(Announcement announcement);
}
