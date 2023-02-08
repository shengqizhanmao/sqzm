package com.lin.sqzmHtgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lin.common.Result;
import com.lin.sqzmHtgl.pojo.User;
import com.lin.sqzmHtgl.mapper.UserMapper;
import com.lin.sqzmHtgl.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.common.utils.Md5Utils;
import com.lin.common.utils.MinioUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource(name="minioUtils")
    private MinioUtils minioUtils;
    @Override
    public Result addUser(User user) {
        String password = user.getPassword();
        String username = user.getUsername();
        if (StringUtils.isEmpty(username)){
            return Result.fail("登陆名不能为空");
        }
        if (StringUtils.isEmpty(password)){
            password="123456";
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        try{
            User user1 = userMapper.selectOne(lambdaQueryWrapper);
            if (user1!=null){
                return Result.fail("添加失败,用户已存在");
            }
        }catch (Exception e){
            log.error("普通用户添加失败,原因是"+e);
            return Result.fail("添加失败");
        }
        String salt = Md5Utils.CretaeMd5();
        String password2 = Md5Utils.md5Encryption(password, salt);
        user.setCreateDate(new Date());
        user.setEnableFlag("1");
        user.setSalt(salt);
        user.setPassword(password2);
        try {
            int insert = userMapper.insert(user);
            if (insert == 0) {
                return Result.fail("添加用户失败");
            }
            return Result.succ("添加用户成功");
        }catch (Exception e){
            log.error("普通用户添加失败,原因是"+e);
            return Result.fail("添加失败");
        }
    }

    @Override
    public Result updateUser(User user) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        String password2 = Md5Utils.md5Encryption(user.getPassword(), user.getSalt());
        lambdaUpdateWrapper.eq(User::getId,user.getId())
                .set(User::getNickname,user.getNickname())
                .set(User::getEmail,user.getEmail())
                .set(User::getPassword,password2)
                .set(User::getGender,user.getGender());
        try{
            int update = userMapper.update(user, lambdaUpdateWrapper);
            if (update==0){
                Result.fail("用户修改失败");
            }
            return Result.succ("用户修改成功");
        }catch (Exception e){
            log.error("用户修改失败,原因是"+e);
            return Result.fail("用户修改失败");
        }
    }

    @Override
    public Result deleteUser(String id) {
        if(id==null){
            return Result.fail("id参数不能为空");
        }
        try{
            User user1 = userMapper.selectById(id);
            if (user1==null){
                return Result.fail("用户删除失败,用户不存在");
            }
        }catch (Exception e){
            log.error("用户删除失败,原因是"+e);
            return Result.fail("用户删除失败");
        }
        try {
            int i = userMapper.deleteById(id);
            if (i == 0) {
                return Result.fail("删除失败");
            }
            return Result.succ("删除成功");
        }catch (Exception e){
            log.error("用户删除失败,原因是"+e);
            return Result.fail("用户删除失败");
        }
    }

    //修改用户头像
    public Result upAvatar(MultipartFile file, String id) {
        //获取上传文件的文件名
        String fileName = file.getOriginalFilename();
        // 为了避免文件名重复，使用UUID重命名文件，将横杠去掉
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newFileName = uuid + fileName;
        try{
            String s = minioUtils.putObject(file.getInputStream(), newFileName, file.getContentType());
            // 返回文件名
            String fileName2=s+"/"+newFileName;
            User user = userMapper.selectById(id);
            String avatar = user.getAvatar();
            user.setAvatar(fileName2);
            //进行修改头像
            try {
                userMapper.updateById(user);
                minioUtils.removeObject(avatar.split("/")[avatar.split("/").length-1]);
            } catch (Exception e) {
                System.out.println("修改头像错误:" + e);
            }
            return Result.succ("修改头像成功");
        }catch (Exception e){
            log.error("上传图片失败，原因是"+e);
            return Result.fail(20001,"上传图片失败");
        }
    }
}
