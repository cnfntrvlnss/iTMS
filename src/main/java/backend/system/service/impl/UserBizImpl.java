package backend.system.service.impl;

import backend.entity.Role;
import backend.entity.User;
import backend.entity.custom.UserVo;
import backend.system.dao.RoleDao;
import backend.system.dao.UserDao;
import backend.system.service.RoleBiz;
import backend.system.service.UserBiz;
import backend.util.PasswordHelper;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by c0de8ug on 16-2-9.
 */

@Service
public class UserBizImpl implements UserBiz {

    @Resource
    UserDao userDao;

    @Resource
    RoleDao roleDao;

    //@Resource
    //StaffDao staffDao;

    @Resource
    private PasswordHelper passwordHelper;
    @Resource(name = "roleBizImpl")
    private RoleBiz roleBiz;

    @Override
    public List<UserVo> findAll() throws InvocationTargetException, IllegalAccessException {
        List<UserVo> userVoList = new ArrayList<>();
        List userList = userDao.findAll();


        Iterator iterator = userList.iterator();

        while (iterator.hasNext()) {
            StringBuilder s = new StringBuilder();
            User user = (User) iterator.next();
            List<Long> roleIds = user.getRoleIds();

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(userVo, user);

            if (roleIds != null) {
                int i = 0;
                int size = roleIds.size();
                for (; i < size - 1; i++) {
                    Role role = roleDao.findOne(roleIds.get(i));

                    s.append(role.getDescription());
                    s.append(",");
                }
                Role role = roleDao.findOne(roleIds.get(i));
                s.append(role.getDescription());
                userVo.setRoleIdsStr(s.toString());
            }


            userVoList.add(userVo);

        }

        return userVoList;
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    public void add(User user) {
        
        passwordHelper.encryptPassword(user);
        userDao.add(user);
 //       String id = user.getUserId();
        //String teacherRoleId = roleDao.findByDescription("鑰佸笀").getId().toString();
 //       if (user.getRoleIdsStr().indexOf(teacherRoleId) != -1) {
//            Staff staff = new Staff();
//            staff.setStaffId(id);
//            staff.setStaffName(id);
            //staffDao.add(staff);
  //      }


    }

    //TODO 鍒犻櫎staff鍜宻tudent杩炲甫鐨勫姛鑳芥湭瀹屾垚
    @Transactional
    @Override
    public void delete(String id) {
        userDao.delete(id);
    }

    @Override
    public void changePassword(String userId, String newPassword) {
        User user = userDao.findById(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.updatePas(user);

    }

    @Override
    public User findByUsername(String username) {
        return userDao.findById(username);
    }

    @Override
    public Set<String> findRoles(String username) {
        User user = findByUsername(username);
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleBiz.findRoles(user.getRoleIds().toArray(new Long[0]));
    }

    @Override
    public Set<String> findPermissions(String username) {
        User user = findByUsername(username);
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleBiz.findPermissions(user.getRoleIds().toArray(new Long[0]));
    }

	@Override
	public void updateLeader(User user) {
		// TODO Auto-generated method stub
		 userDao.updateLeader(user);
	}

	@Override
	public List<User> findAllUser(String leader) {
		// TODO Auto-generated method stub
		return  userDao.findAllUser(leader);
	}

	@Override
	public void deleteNumber(String userId) {
		// TODO Auto-generated method stub
		userDao.deleteNumber(userId);
	}

	
}

