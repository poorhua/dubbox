package com.telecomjs.mapper;

import com.telecomjs.entities.EopAppOnline;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@SuppressWarnings("UnnecessaryInterfaceModifier")
public interface EopAppOnlineMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EOP_APP_ONLINE
     *
     * @mbggenerated
     */

    public int insert(EopAppOnline record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table EOP_APP_ONLINE
     *
     * @mbggenerated
     */
    int insertSelective(EopAppOnline record);

    //@Cacheable(value = "auth",key = "'EopAppOnline#getAppOnLineByToken#' + #p0")
    public EopAppOnline getAppOnLineByToken(String token);

    //@CacheEvict(value = "auth",key = "'getAppOnLineByToken#' + #a0.token")
    public int updateAppOnLineByToken(EopAppOnline bean);

    public int updateArchiveAppOnLineByToken(String token);
    public int archiveOnlineToken(String token);
    public int deleteOnlineToken(String token);
}