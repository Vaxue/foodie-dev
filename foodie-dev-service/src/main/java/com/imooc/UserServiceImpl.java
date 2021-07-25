package com.imooc;

import com.imooc.enums.SexEnum;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);

        Users result = usersMapper.selectOneByExample(example);

        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) throws Exception {
        Users user = new Users();
        String userId = Sid.nextShort();
        user.setId(userId);

        user.setUsername(userBO.getUsername());
        user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        // 默认用户昵称通用户名
        user.setNickname(userBO.getUsername());
        // 设置默认头像
        user.setFace(USER_FACE);
        // 设置生日
        user.setBirthday(DateUtil.stringToDate("1998-03-28"));
        // 设置性别 默认性别为保密
        user.setSex(SexEnum.secret.type);

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);

        Users result = usersMapper.selectOneByExample(example);

        return result;
    }

}
