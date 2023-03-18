package com.lin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.Result;
import com.lin.common.mapper.AnnouncementMapper;
import com.lin.common.pojo.Announcement;
import com.lin.common.pojo.Modulars;
import com.lin.common.pojo.Vo.AnnouncementVo;
import com.lin.common.service.AnnouncementService;
import com.lin.common.service.ModularsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-25
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Resource
    private AnnouncementMapper announcementMapper;

    @Resource
    private ModularsService modularsService;

    @Override
    public Result getByPalteIdAndModularsId(String palteId, String modularsId) {
        LambdaQueryWrapper<Announcement> announcementLambdaQueryWrapper = new LambdaQueryWrapper<>();
        announcementLambdaQueryWrapper.eq(Announcement::getPalteId, palteId);
        announcementLambdaQueryWrapper.eq(Announcement::getModularsId, modularsId);
        try {
            Announcement announcement = announcementMapper.selectOne(announcementLambdaQueryWrapper);
            if (announcement == null) {
                return Result.fail("无公告");
            }
            return Result.succ("获取公告成功", announcement);
        } catch (Exception e) {
            return Result.fail(500, "获取公告失败");
        }
    }

    @Override
    public Result listAnnouncementVo(String palteId) {
        LambdaQueryWrapper<Announcement> announcementLambdaQueryWrapper = new LambdaQueryWrapper<>();
        announcementLambdaQueryWrapper.eq(Announcement::getPalteId, palteId);
        List<Announcement> announcements = announcementMapper.selectList(announcementLambdaQueryWrapper);
        List<AnnouncementVo> copy = Copy(announcements);
        return Result.succ("获取公告成功", copy);
    }

    @Override
    public Result saveAnnouncement(Announcement announcement) {
        LambdaQueryWrapper<Announcement> announcementLambdaQueryWrapper = new LambdaQueryWrapper<>();
        announcementLambdaQueryWrapper.eq(Announcement::getPalteId, announcement.getPalteId())
                .eq(Announcement::getModularsId, announcement.getModularsId());
        Announcement announcement1 = announcementMapper.selectOne(announcementLambdaQueryWrapper);
        if (announcement1 != null) {
            return Result.fail("添加失败,已有公告");
        }
        int insert = announcementMapper.insert(announcement);
        if (insert != 1) {
            return Result.fail("添加失败");
        }
        return Result.succ("添加成功");
    }

    private List<AnnouncementVo> Copy(List<Announcement> announcements) {
        List<AnnouncementVo> announcementVos = new ArrayList<>();
        for (Announcement announcement : announcements) {
            AnnouncementVo announcementVo = new AnnouncementVo();
            BeanUtils.copyProperties(announcement, announcementVo);
            if (announcement.getModularsId().equals("0")) {
                Modulars modulars = new Modulars();
                modulars.setId("0");
                modulars.setName("该板块公告");
                modulars.setPalteId(announcement.getPalteId());
                announcementVo.setModulars(modulars);
                announcementVos.add(announcementVo);
                continue;
            }
            Modulars byId = modularsService.getById(announcement.getModularsId());
            announcementVo.setModulars(byId);
            announcementVos.add(announcementVo);
        }
        return announcementVos;
    }
}
