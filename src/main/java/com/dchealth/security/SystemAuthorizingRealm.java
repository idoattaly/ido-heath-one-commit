/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.dchealth.security;
import com.dchealth.entity.common.YunUsers;
import com.dchealth.facade.security.UserFacade;
import com.dchealth.util.UserUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2013-5-29
 */
@Service
public class SystemAuthorizingRealm extends AuthorizingRealm {

//	private SystemService systemService;



	@Autowired
	private UserFacade userFacade ;
	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		try {
			YunUsers yunUsers = userFacade.getYunUsersByLoginName(token.getUsername());
			if(yunUsers!=null){
				if(!"R".equals(yunUsers.getLoginFlags())){
					throw new AuthenticationException("用户授权失败，请等待审核");
				}
				return  new SimpleAuthenticationInfo(new Principal(yunUsers),token.getCredentials(),getName()) ;
			}else{
				throw new UnknownAccountException("不存在的用户");
				//return null ;
			}
		}  catch (Exception e) {
			e.printStackTrace();
			if(e instanceof AuthenticationException){
				throw (AuthenticationException) e;
			}else{
				throw new UnknownAccountException(e.getMessage());
			}
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermission("user:list");
		try {
			YunUsers yunUsers = userFacade.getYunUsersByUserId(principal.getLoginName());
			UserUtils.putCache("user",yunUsers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
//		User user = getSystemService().getUserByLoginName(principal.getLoginName());
//		if (user != null) {
//			UserUtils.putCache("user", user);
//
//			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//			List<Menu> list = UserUtils.getMenuList();
//			for (Menu menu : list){
//				if (StringUtils.isNotBlank(menu.getPermission())){
//					// 添加基于Permission的权限信息
//					for (String permission : StringUtils.split(menu.getPermission(),",")){
//						info.addStringPermission(permission);
//					}
//				}
//			}
//			// 更新登录IP和时间
//			getSystemService().updateUserLoginInfo(user.getId());
//			return null;
//		} else {
//			return null;
//		}
		return  info ;
	}
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
        HisCredentialsMatcher matcher = new HisCredentialsMatcher() ;
        matcher.setUserFacade(userFacade);

		setCredentialsMatcher(matcher);
	}
	
	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String id;
		private String loginName;
		private String name;
		private String salt ;
		private String dbPassword;
		private Map<String, Object> cacheMap;

		public String getId() {
			return id;
		}

		public Principal(YunUsers yunUsers){
			this.id = String.valueOf(yunUsers.getId());
			this.loginName = yunUsers.getUserId();
			this.name = yunUsers.getUserName();
			this.salt = yunUsers.getSalt();
			this.dbPassword = yunUsers.getPassword();
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public Map<String, Object> getCacheMap() {
			if (cacheMap==null){
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}

		public String getSalt() {
			return salt;
		}

		public void setSalt(String salt) {
			this.salt = salt;
		}

		public String getDbPassword() {
			return dbPassword;
		}

		public void setDbPassword(String dbPassword) {
			this.dbPassword = dbPassword;
		}
	}
}
