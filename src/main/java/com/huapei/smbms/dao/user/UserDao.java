package com.huapei.smbms.dao.user;


import com.huapei.smbms.pojo.User;
import org.apache.ibatis.annotations.*;

import java.sql.Connection;
import java.util.List;

public interface UserDao {

	/**
	 * 增加用户信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Insert("INSERT INTO smbms_user(userCode,userName,userPassword,gender,birthday,phone,address,userRole,createdBy,creationDate,idPicPath)" +
			" VALUES(#{userCode},#{userName},#{userPassword},#{gender},#{birthday},#{phone},#{address},#{userRole},#{createdBy},#{creationDate},#{idPicPath})")
	public int add(User user)throws Exception;

	/**
	 * 通过userCode和userPassword获取User
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT * FROM smbms_user WHERE userCode = #{userCode} AND userPassword = #{userPassword}")
	public User getLoginUser(@Param("userCode") String userCode, @Param("userPassword") String userPassword)throws Exception;


	/**
	 * 通过条件查询-userList
	 * @param userName
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	@Select({"<script> select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id " ,
			" <if test='userName != null'>" ,
			"        AND userName like concat('%',#{userName} ,'%')" ,
			"    </if>" ,
			"<if test= 'userRole >0 '> " ,
			"	 and userRole = #{userRole} ",
			"</if>",
			"</script>"
			})
	public List<User> getUserList(@Param("userName") String userName, @Param("userRole") Integer userRole)throws Exception;
	/**
	 * 通过条件查询-用户表记录数
	 * @param connection
	 * @param userName
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	public int getUserCount(Connection connection, String userName, int userRole)throws Exception;
	
	/**
	 * 通过userId删除user
	 * @param delId
	 * @return
	 * @throws Exception
	 */
	@Delete("delete from smbms_user where id =#{id}")
	public int deleteUserById(@Param("id") Integer delId)throws Exception;
	
	
	/**
	 * 通过userId获取user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Select("select * from smbms_user where id =  #{id}")
	public User getUserById( Integer id)throws Exception;
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Update("update smbms_user set userName=#{userName},gender=#{gender},birthday=#{birthday},phone=#{phone},address=#{address},userRole=#{userRole},modifyBy=#{modifyBy},modifyDate=#{modifyDate} where id = #{id} ")
	public int modify( User user)throws Exception;
	
	
	/**
	 * 修改当前用户密码
	 * @param connection
	 * @param id
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public int updatePwd(Connection connection, int id, String pwd)throws Exception;

    /***
     * 通过usercode 查询是否重复
     * @param userCode
     */
    @Select("select * from smbms_user where userCode = #{userCode}")
    User queryExistByUserCode(String userCode);

}
