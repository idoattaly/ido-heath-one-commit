/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.dchealth.util;

import com.dchealth.entity.common.YunUsers;
import com.dchealth.facade.security.UserFacade;
import com.dchealth.security.SystemAuthorizingRealm;
import com.google.common.collect.Maps;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 用户工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
@Component
public class UserUtils{

	@Autowired
	private  UserFacade userFacade ;
	public static  UserUtils userUtils ;

	@PostConstruct
	public void init(){
		userUtils = this ;
		this.userFacade = userFacade ;
	}



	public static final String CACHE_USER = "user";

	public static YunUsers getYunUsers() throws Exception {
		YunUsers user = (YunUsers)getCache(CACHE_USER);
		if (user == null){
			try{
				Subject subject = SecurityUtils.getSubject();
				SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal)subject.getPrincipal();
				if (principal!=null){
					YunUsers users = userUtils.userFacade.getYunUsersByUserId(principal.getLoginName());
					putCache(CACHE_USER, users);
					user=users ;
				}
			}catch (UnavailableSecurityManagerException e) {
				e.printStackTrace();
			}catch (InvalidSessionException e){
				e.printStackTrace();
			}
		}
		if (user == null){
			user = new YunUsers();
			try{
				SecurityUtils.getSubject().logout();
			}catch (UnavailableSecurityManagerException e) {
				
			}catch (InvalidSessionException e){
				
			}
		}
		return user;
	}


	public static YunUsers getYunUsers(boolean isRefresh) throws Exception {
		if (isRefresh){
			removeCache(CACHE_USER);
		}
		return getYunUsers();
	}






	// ============== YunUsers Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}
	
	public static Map<String, Object> getCacheMap(){
		Map<String, Object> map = Maps.newHashMap();
		try{
			Subject subject = SecurityUtils.getSubject();
			SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal)subject.getPrincipal();
			return principal!=null?principal.getCacheMap():map;
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return map;
	}


}
