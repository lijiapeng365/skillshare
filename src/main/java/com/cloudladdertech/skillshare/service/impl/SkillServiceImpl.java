package com.cloudladdertech.skillshare.service.impl;

import com.cloudladdertech.skillshare.dto.CreateSkillDTO;
import com.cloudladdertech.skillshare.dto.SkillQueryDTO;
import com.cloudladdertech.skillshare.dto.UpdateSkillDTO;
// 实体类
import com.cloudladdertech.skillshare.entity.Skill; // 核心技能实体
import com.cloudladdertech.skillshare.entity.SkillOfferingTagMapping; // Skill 与 Tag 映射
import com.cloudladdertech.skillshare.entity.User;
import com.cloudladdertech.skillshare.entity.SkillCategory;
import com.cloudladdertech.skillshare.entity.SkillTag;
// Mapper 接口
import com.cloudladdertech.skillshare.mapper.SkillMapper;
import com.cloudladdertech.skillshare.mapper.SkillOfferingTagMappingMapper;
import com.cloudladdertech.skillshare.mapper.UserMapper;
import com.cloudladdertech.skillshare.mapper.SkillCategoryMapper;
import com.cloudladdertech.skillshare.mapper.SkillTagMapper;
import com.cloudladdertech.skillshare.mapper.FavoriteMapper; // 收藏功能 Mapper (可选)
// 服务接口和 VO/DTO
import com.cloudladdertech.skillshare.service.SkillService;
import com.cloudladdertech.skillshare.vo.PagedResult;
import com.cloudladdertech.skillshare.vo.SkillDetailVO;
import com.cloudladdertech.skillshare.vo.SkillVO;
import com.cloudladdertech.skillshare.vo.UserVO;
// 自定义异常 (需要创建)
import com.cloudladdertech.skillshare.exception.ResourceNotFoundException;
import com.cloudladdertech.skillshare.exception.ForbiddenAccessException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Map; // 引入 Map

/**
 * 技能服务实现类
 * (仿照 UserServiceImpl 结构，围绕 Skill 实体和相关 Mapper 构建)
 */
@Service
public class SkillServiceImpl implements SkillService {

    private static final Logger log = LoggerFactory.getLogger(SkillServiceImpl.class);

    // --- 依赖注入 ---
    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SkillCategoryMapper categoryMapper;

    @Autowired
    private SkillTagMapper tagMapper;

    @Autowired
    private SkillOfferingTagMappingMapper skillOfferingTagMappingMapper;

    @Autowired(required = false) // 收藏功能可选
    private FavoriteMapper favoriteMapper;


    @Override
    public PagedResult<SkillVO> getSkills(SkillQueryDTO queryDTO) {
        log.info("获取技能列表，查询条件: {}", queryDTO);

        int page = queryDTO.getPageOrDefault();
        int pageSize = queryDTO.getPageSizeOrDefault();
        int offset = (page - 1) * pageSize;

        // 调用 Mapper 获取分页数据和总数
        List<Skill> skillEntities = skillMapper.findPaged(
                queryDTO.getCategoryId(),
                queryDTO.getKeyword(),
                queryDTO.getSortBy(),
                queryDTO.getSortOrder(),
                pageSize,
                offset
        );
        long totalItems = skillMapper.countTotal(
                queryDTO.getCategoryId(),
                queryDTO.getKeyword()
        );

        // 映射为 VO 列表 (包含 User 和 Category 信息)
        List<SkillVO> skillVOs = mapSkillListToSkillVOList(skillEntities);

        // 构建并返回分页结果
        PagedResult<SkillVO> pagedResult = new PagedResult<>();
        PagedResult.Pagination pagination = new PagedResult.Pagination();
        pagination.setCurrentPage(page);
        pagination.setPageSize(pageSize);
        pagination.setTotalItems(totalItems);
        pagination.setTotalPages((int) Math.ceil((double) totalItems / pageSize));
        pagedResult.setPagination(pagination);
        pagedResult.setList(skillVOs);

        return pagedResult;
    }

    @Override
    @Transactional
    public SkillDetailVO createSkill(Integer userId, CreateSkillDTO createSkillDTO) {
        log.info("为用户ID: {} 创建技能，数据: {}", userId, createSkillDTO);

        // 验证用户和分类是否存在
        validateUserExists(userId);
        validateCategoryExists(createSkillDTO.getCategoryId());

        // 创建并保存 Skill 实体
        Skill skill = mapCreateDTOToSkill(userId, createSkillDTO);
        skillMapper.insert(skill); // 假设回填了 skillId
        Integer skillId = skill.getSkillId();
        if (skillId == null) {
             throw new RuntimeException("创建技能失败，未能获取到技能ID");
        }
        log.info("技能创建成功，ID: {}", skillId);

        // 处理并保存技能标签映射
        saveSkillTags(skillId, skill.getCategoryId(), createSkillDTO.getTags());

        // 处理图片 (省略，根据实际需求实现)
        // saveSkillImages(skillId, createSkillDTO.getImages());

        // 查询并返回完整的 SkillDetailVO
        return getSkillById(skillId, userId);
    }

    @Override
    public SkillDetailVO getSkillById(Integer id, Integer currentUserId) {
        log.info("获取技能详情，ID: {}, 当前用户ID: {}", id, currentUserId);

        // 查询 Skill 实体
        Skill skill = skillMapper.findById(id);
        if (skill == null) {
            throw new ResourceNotFoundException("技能不存在: " + id);
        }

        // (可选) 增加浏览次数
        // skillMapper.incrementViewCount(id);

        // 查询关联数据: User, Category, Tags
        User user = userMapper.findById(skill.getUserId());
        SkillCategory category = categoryMapper.findById(skill.getCategoryId());
        List<SkillDetailVO.TagVO> tagVOs = getSkillTags(id);

        // 映射为 SkillDetailVO
        SkillDetailVO detailVO = mapSkillToDetailVO(skill, user, category, tagVOs);

        // 查询并设置收藏状态
        if (currentUserId != null && favoriteMapper != null) {
            detailVO.setIsFavorited(favoriteMapper.exists(currentUserId, id));
        } else {
            detailVO.setIsFavorited(false);
        }

        return detailVO;
    }

    @Override
    @Transactional
    public SkillDetailVO updateSkill(Integer id, Integer userId, UpdateSkillDTO updateSkillDTO) {
        log.info("更新技能 ID: {}, 用户ID: {}, 数据: {}", id, userId, updateSkillDTO);

        // 查询并验证技能所有权
        Skill skill = findAndValidateOwnership(id, userId);

        // 更新 Skill 实体字段
        boolean updated = updateSkillFieldsFromDTO(skill, updateSkillDTO);

        // 更新标签 (如果提供了)
        if (updateSkillDTO.getTags() != null) {
             updateSkillTags(id, skill.getCategoryId(), updateSkillDTO.getTags());
             updated = true;
        }

        // 更新图片 (省略)
        // updateSkillImages(id, updateSkillDTO.getImages());

        // 如果有更新，则保存到数据库
        if (updated) {
            skill.setUpdatedAt(LocalDateTime.now());
            int affectedRows = skillMapper.update(skill);
            if (affectedRows == 0) {
                 throw new RuntimeException("更新技能失败");
            }
             log.info("技能 ID: {} 更新成功", id);
        }

        // 返回更新后的 SkillDetailVO
        return getSkillById(id, userId);
    }

    @Override
    @Transactional
    public void deleteSkill(Integer id, Integer userId) {
        log.info("删除技能 ID: {}, 用户ID: {}", id, userId);

        // 查询并验证技能所有权
        Skill skill = findAndValidateOwnership(id, userId);

        // 删除关联数据
        deleteSkillRelatedData(id);

        // 删除 Skill 实体
        int affectedRows = skillMapper.deleteById(id);
        if (affectedRows == 0) {
            log.warn("删除技能 ID: {} 失败，可能已被删除", id);
        } else {
             log.info("技能 ID: {} 已被用户 ID: {} 删除", id, userId);
        }
    }

    @Override
    public List<SkillVO> getHotSkills(Integer limit) {
        log.info("获取{}个热门技能", limit);
        List<Skill> hotSkillEntities = skillMapper.findHot(limit != null ? limit : 10); // 提供默认值
        return mapSkillListToSkillVOList(hotSkillEntities);
    }

    @Override
    public List<SkillVO> getRecommendedSkills(Integer limit, Integer currentUserId) {
        log.info("为用户ID: {} 获取{}个推荐技能", currentUserId, limit);
        // 简化推荐逻辑：返回热门技能
        // 实际应用中需要更复杂的推荐算法
         log.warn("getRecommendedSkills: 推荐逻辑未完全实现，暂时返回热门技能");
        List<Skill> recommendedEntities = skillMapper.findHot(limit != null ? limit : 10);
        return mapSkillListToSkillVOList(recommendedEntities);
    }

    // --- Helper Methods ---

    private void validateUserExists(Integer userId) {
        if (userMapper.findById(userId) == null) {
            throw new ResourceNotFoundException("用户不存在: " + userId);
        }
    }

    private void validateCategoryExists(Integer categoryId) {
         if (categoryId == null) {
             throw new IllegalArgumentException("必须提供技能分类ID");
         }
        if (categoryMapper.findById(categoryId) == null) {
            throw new ResourceNotFoundException("技能分类不存在: " + categoryId);
        }
    }

     private Skill mapCreateDTOToSkill(Integer userId, CreateSkillDTO dto) {
        Skill skill = new Skill();
        BeanUtils.copyProperties(dto, skill); // 复制 title, description, teachingMethod, exchangeExpectation, coverImageUrl etc.
        skill.setUserId(userId);
        skill.setRating(0.0);
        skill.setViewCount(0);
        skill.setExchangeCount(0); // 初始化交换次数
        skill.setCreatedAt(LocalDateTime.now());
        skill.setUpdatedAt(LocalDateTime.now());
        skill.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true); // 默认激活
        return skill;
    }

    private void saveSkillTags(Integer skillId, Integer categoryId, List<String> tagNames) {
         if (CollectionUtils.isEmpty(tagNames)) return;

         for (String tagName : tagNames) {
             SkillTag tag = tagMapper.findByName(tagName);
             if (tag == null) {
                 // 选择：创建新标签或忽略
                 // 创建示例:
                 // tag = createNewTag(tagName, categoryId);
                 // if (tag == null) continue; // 创建失败则跳过
                 log.warn("创建技能 ID: {} 时，标签 '{}' 不存在，已跳过。", skillId, tagName);
                 continue;
             }
             // 创建映射关系
             SkillOfferingTagMapping mapping = new SkillOfferingTagMapping();
             mapping.setSkillId(skillId);
             mapping.setTagId(tag.getTagId());
             skillOfferingTagMappingMapper.insert(mapping);
         }
    }

     // private SkillTag createNewTag(String tagName, Integer categoryId) { ... } // 可选：创建新标签的逻辑

    private List<SkillDetailVO.TagVO> getSkillTags(Integer skillId) {
        List<SkillOfferingTagMapping> mappings = skillOfferingTagMappingMapper.findBySkillId(skillId);
        if (CollectionUtils.isEmpty(mappings)) {
            return Collections.emptyList();
        }
        List<Integer> tagIds = mappings.stream().map(SkillOfferingTagMapping::getTagId).collect(Collectors.toList());
        List<SkillTag> tags = tagMapper.findByIds(tagIds); // 需实现 findByIds
        return tags.stream().map(tag -> {
            SkillDetailVO.TagVO vo = new SkillDetailVO.TagVO();
            BeanUtils.copyProperties(tag, vo); // 复制 tagId, name
            return vo;
        }).collect(Collectors.toList());
    }


     private SkillDetailVO mapSkillToDetailVO(Skill skill, User user, SkillCategory category, List<SkillDetailVO.TagVO> tagVOs) {
        SkillDetailVO detailVO = new SkillDetailVO();
        BeanUtils.copyProperties(skill, detailVO);

        if (user != null) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO, "passwordHash"); // 排除密码哈希
            detailVO.setUser(userVO);
             detailVO.setUserId(user.getUserId()); // 确保 userId 也设置了
        }
        if (category != null) {
            detailVO.setCategoryName(category.getName());
             detailVO.setCategoryId(category.getCategoryId()); // 确保 categoryId 也设置了
        }
        detailVO.setTags(tagVOs);
        // detailVO.setImages(...); // 设置图片列表

        return detailVO;
    }

     private Skill findAndValidateOwnership(Integer skillId, Integer userId) {
        Skill skill = skillMapper.findById(skillId);
        if (skill == null) {
            throw new ResourceNotFoundException("技能不存在: " + skillId);
        }
        if (!skill.getUserId().equals(userId)) {
            throw new ForbiddenAccessException("无权操作此技能");
        }
        return skill;
    }

     private boolean updateSkillFieldsFromDTO(Skill skill, UpdateSkillDTO dto) {
        boolean updated = false;
        if (dto.getTitle() != null) { skill.setTitle(dto.getTitle()); updated = true; }
        if (dto.getDescription() != null) { skill.setDescription(dto.getDescription()); updated = true; }
        if (dto.getCategoryId() != null) {
             validateCategoryExists(dto.getCategoryId()); // 验证新分类
             skill.setCategoryId(dto.getCategoryId()); updated = true;
        }
        if (dto.getCoverImageUrl() != null) { skill.setCoverImageUrl(dto.getCoverImageUrl()); updated = true; }
        if (dto.getIsActive() != null) { skill.setIsActive(dto.getIsActive()); updated = true; }
         if (dto.getTeachingMethod() != null) { skill.setTeachingMethod(dto.getTeachingMethod()); updated = true; }
         if (dto.getExchangeExpectation() != null) { skill.setExchangeExpectation(dto.getExchangeExpectation()); updated = true; }
        // ... 更新其他字段 ...
        return updated;
    }

     private void updateSkillTags(Integer skillId, Integer categoryId, List<String> tagNames) {
         // 先删除旧的映射
         skillOfferingTagMappingMapper.deleteBySkillId(skillId);
         // 再添加新的映射 (复用 saveSkillTags 逻辑)
         saveSkillTags(skillId, categoryId, tagNames);
     }

     private void deleteSkillRelatedData(Integer skillId) {
         // 删除 Skill 与 Tag 的映射
         skillOfferingTagMappingMapper.deleteBySkillId(skillId);
         // 删除收藏记录
         if (favoriteMapper != null) {
             favoriteMapper.deleteBySkillId(skillId);
         }
         // 删除其他关联，如图片、评论、交换记录等...
     }

    /**
     * 将 Skill 实体列表映射为 SkillVO 列表 (批量优化)
     */
    private List<SkillVO> mapSkillListToSkillVOList(List<Skill> skills) {
        if (CollectionUtils.isEmpty(skills)) {
            return Collections.emptyList();
        }
        // 批量获取关联 User 和 Category
        List<Integer> userIds = skills.stream().map(Skill::getUserId).distinct().collect(Collectors.toList());
        List<Integer> categoryIds = skills.stream().map(Skill::getCategoryId).distinct().collect(Collectors.toList());

        Map<Integer, User> userMap = userIds.isEmpty() ? Collections.emptyMap() :
                userMapper.findByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, u -> u));
        Map<Integer, SkillCategory> categoryMap = categoryIds.isEmpty() ? Collections.emptyMap() :
                categoryMapper.findByIds(categoryIds).stream().collect(Collectors.toMap(SkillCategory::getCategoryId, c -> c));

        // 映射
        return skills.stream().map(skill -> {
            SkillVO vo = new SkillVO();
            BeanUtils.copyProperties(skill, vo); // 复制基础属性

            User user = userMap.get(skill.getUserId());
            if (user != null) {
                vo.setUserId(user.getUserId()); // userId is now Integer
                SkillVO.UserInfo userInfo = new SkillVO.UserInfo(); // Create UserInfo object
                userInfo.setUsername(user.getUsername());
                userInfo.setAvatarUrl(user.getAvatarUrl());
                userInfo.setSchool(user.getSchool()); // Also copy school if available
                vo.setUser(userInfo); // Set the UserInfo object
            }

            SkillCategory category = categoryMap.get(skill.getCategoryId());
            if (category != null) {
                vo.setCategoryId(category.getCategoryId()); // categoryId is now Integer
                vo.setCategoryName(category.getName());
            }
            return vo;
        }).collect(Collectors.toList());
    }
}