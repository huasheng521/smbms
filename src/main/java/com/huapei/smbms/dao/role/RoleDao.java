package com.huapei.smbms.dao.role;


import com.huapei.smbms.pojo.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Connection;
import java.util.List;

public interface RoleDao {
	@Select("select * from smbms_role")
	public List<Role> getRoleList()throws Exception;
	@Select("select * from smbms_role where id = #{id}")
	Role QueryRoleByRID(@Param("id") Integer userRole);
}
