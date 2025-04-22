package com.cloudladdertech.skillshare.service;

import com.cloudladdertech.skillshare.dto.CreateSkillDTO;
import com.cloudladdertech.skillshare.dto.SkillQueryDTO;
import com.cloudladdertech.skillshare.dto.UpdateSkillDTO;
import com.cloudladdertech.skillshare.vo.SkillVO;
import com.cloudladdertech.skillshare.vo.SkillDetailVO;
import com.cloudladdertech.skillshare.vo.PagedResult;

import java.util.List;

/**
 * 技能服务接口，定义技能相关的业务逻辑
 */
public interface SkillService {

    /**
     * 获取技能列表（分页、筛选、排序）
     * @param queryDTO 查询参数 DTO
     * @return 分页后的技能列表 VO
     */
    PagedResult<SkillVO> getSkills(SkillQueryDTO queryDTO);

    /**
     * 发布新技能
     * @param userId 发布用户ID
     * @param createSkillDTO 创建技能数据 DTO
     * @return 新创建技能的详细信息 VO
     */
    SkillDetailVO createSkill(Integer userId, CreateSkillDTO createSkillDTO);

    /**
     * 根据ID获取技能详情
     * @param id 技能ID
     * @param currentUserId 当前登录用户ID (可能为null，用于判断收藏状态等)
     * @return 技能详细信息 VO，如果未找到则可能返回null或抛出异常
     */
    SkillDetailVO getSkillById(Integer id, Integer currentUserId);

    /**
     * 修改技能信息
     * @param id 技能ID
     * @param userId 执行操作的用户ID (用于权限校验)
     * @param updateSkillDTO 更新技能数据 DTO
     * @return 更新后的技能详细信息 VO
     * @throws RuntimeException 如果技能不存在或用户无权修改，可以抛出自定义异常
     */
    SkillDetailVO updateSkill(Integer id, Integer userId, UpdateSkillDTO updateSkillDTO);

    /**
     * 删除技能
     * @param id 技能ID
     * @param userId 执行操作的用户ID (用于权限校验)
     * @throws RuntimeException 如果技能不存在或用户无权删除，可以抛出自定义异常
     */
    void deleteSkill(Integer id, Integer userId);

    /**
     * 获取热门技能列表
     * @param limit 返回数量限制
     * @return 热门技能列表 VO
     */
    List<SkillVO> getHotSkills(Integer limit);

    /**
     * 获取推荐技能列表
     * @param limit 返回数量限制
     * @param currentUserId 当前登录用户ID (可能为null，用于个性化推荐)
     * @return 推荐技能列表 VO
     */
    List<SkillVO> getRecommendedSkills(Integer limit, Integer currentUserId);

}
