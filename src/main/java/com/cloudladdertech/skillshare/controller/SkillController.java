package com.cloudladdertech.skillshare.controller;  

import com.cloudladdertech.skillshare.dto.CreateSkillDTO;  
import com.cloudladdertech.skillshare.dto.SkillQueryDTO;  
import com.cloudladdertech.skillshare.dto.UpdateSkillDTO;  
import com.cloudladdertech.skillshare.service.SkillService;  
import com.cloudladdertech.skillshare.vo.Result;  
import com.cloudladdertech.skillshare.vo.SkillDetailVO;  
import com.cloudladdertech.skillshare.vo.SkillVO;  
import com.cloudladdertech.skillshare.vo.PagedResult; // 假设PagedResult VO用于列表响应  
import io.swagger.annotations.Api;  
import io.swagger.annotations.ApiImplicitParam;  
import io.swagger.annotations.ApiImplicitParams;  
import io.swagger.annotations.ApiOperation;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.bind.annotation.*;  
import org.springframework.http.HttpStatus; // 导入HttpStatus  
import org.springframework.security.core.Authentication; // 添加导入
import org.springframework.security.core.context.SecurityContextHolder; // 添加导入
import com.cloudladdertech.skillshare.config.security.UserDetailsImpl; // 添加导入
import com.cloudladdertech.skillshare.exception.UnauthorizedException; // 假设有这个异常类，用于处理未认证

import java.util.List; // 假设列表用于热门/推荐技能  
import java.util.Collections; 
import java.util.Map;

/**  
 * 技能管理控制器  
 */  
@RestController  
@RequestMapping("/api/v1/skills")  
@Api(tags = "技能管理接口", description = "包括技能的发布、查询、修改、删除以及获取热门和推荐技能等")  
public class SkillController {  

    @Autowired  
    private SkillService skillService; // 取消注释并创建SkillService  

    /**  
     * 1. 获取技能列表  
     */  
    @GetMapping  
    @ApiOperation(value = "获取技能列表", notes = "获取技能列表，支持分页、筛选和排序")  
    @ApiImplicitParams({  
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "1", dataTypeClass = Integer.class, paramType = "query"),  
            @ApiImplicitParam(name = "pageSize", value = "每页数量", defaultValue = "10", dataTypeClass = Integer.class, paramType = "query"),  
            @ApiImplicitParam(name = "categoryId", value = "技能分类ID", dataTypeClass = Integer.class, paramType = "query"),  
            @ApiImplicitParam(name = "userId", value = "用户ID", dataTypeClass = Integer.class, paramType = "query"),  
            @ApiImplicitParam(name = "school", value = "学校名称", dataTypeClass = String.class, paramType = "query"),  
            @ApiImplicitParam(name = "keyword", value = "搜索关键词", dataTypeClass = String.class, paramType = "query"),  
            @ApiImplicitParam(name = "sortBy", value = "排序字段 (createdAt, rating, exchangeCount, viewCount)", defaultValue = "createdAt", dataTypeClass = String.class, paramType = "query"),  
            @ApiImplicitParam(name = "order", value = "排序方式 (asc, desc)", defaultValue = "desc", dataTypeClass = String.class, paramType = "query")  
    })  
    public Result<PagedResult<SkillVO>> getSkills(SkillQueryDTO queryDTO) {  
        // 使用skillService实现技能列表逻辑  
        PagedResult<SkillVO> pagedSkills = skillService.getSkills(queryDTO);  
        return Result.success(pagedSkills);  
    }  

    /**  
     * 2. 发布新技能  
     */  
    @PostMapping  
    @ResponseStatus(HttpStatus.CREATED) // 设置响应状态为201 Created  
    @ApiOperation(value = "发布新技能", notes = "用户发布一个新的技能")  
    @ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, dataTypeClass = String.class, paramType = "header")  
    public Result<Object> createSkill(@RequestBody CreateSkillDTO createSkillDTO) {  
         // 从Authorization令牌获取用户ID（例如使用Spring Security Context）  
         Integer userId = getCurrentUserId(); // 获取用户ID  
         SkillDetailVO newSkill = skillService.createSkill(userId, createSkillDTO);  
         Map<String, Integer> responseData = Collections.singletonMap("skillId", newSkill.getSkillId());  
         return Result.created(responseData); // 使用Result.created表示201状态  
    }  

    /**  
     * 3. 获取技能详情  
     */  
    @GetMapping("/{id}")  
    @ApiOperation(value = "获取技能详情", notes = "获取指定ID的技能详细信息")  
    @ApiImplicitParams({  
            @ApiImplicitParam(name = "id", value = "技能ID", required = true, dataTypeClass = Integer.class, paramType = "path"),  
            @ApiImplicitParam(name = "Authorization", value = "Bearer Token (可选, 用于获取收藏状态)", dataTypeClass = String.class, paramType = "header")  
    })  
    public Result<SkillDetailVO> getSkillById(@PathVariable Integer id) {  
        // 如果存在，可选地从Authorization令牌获取用户ID  
        Integer currentUserId = getCurrentUserIdOrNull(); // 获取用户ID或null  
        SkillDetailVO skillDetail = skillService.getSkillById(id, currentUserId);  
        // Note: Service layer should throw ResourceNotFoundException which will be handled globally  
        return Result.success(skillDetail);  
    }  

    /**  
     * 4. 修改技能信息  
     */  
    @PutMapping("/{id}")  
    @ApiOperation(value = "修改技能信息", notes = "修改用户自己发布的技能信息")  
    @ApiImplicitParams({  
            @ApiImplicitParam(name = "id", value = "技能ID", required = true, dataTypeClass = Integer.class, paramType = "path"),  
            @ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, dataTypeClass = String.class, paramType = "header")  
    })  
    public Result<Void> updateSkill(@PathVariable Integer id, @RequestBody UpdateSkillDTO updateSkillDTO) {  
        // 从Authorization令牌获取用户ID  
        Integer userId = getCurrentUserId(); // 获取用户ID  
        skillService.updateSkill(id, userId, updateSkillDTO); // Service should handle exceptions  
        return Result.success("技能信息修改成功", null);  
    }  

    /**  
     * 5. 删除技能  
     */  
    @DeleteMapping("/{id}")  
    @ApiOperation(value = "删除技能", notes = "删除用户自己发布的技能")  
    @ApiImplicitParams({  
            @ApiImplicitParam(name = "id", value = "技能ID", required = true, dataTypeClass = Integer.class, paramType = "path"),  
            @ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, dataTypeClass = String.class, paramType = "header")  
    })  
    public Result<Void> deleteSkill(@PathVariable Integer id) {  
        // 从Authorization令牌获取用户ID  
        Integer userId = getCurrentUserId(); // 获取用户ID  
        skillService.deleteSkill(id, userId); // Service should handle exceptions  
        return Result.success("技能删除成功", null);  
    }  

    /**  
     * 6. 获取热门技能  
     */  
    @GetMapping("/hot")  
    @ApiOperation(value = "获取热门技能", notes = "获取热门技能列表")  
    @ApiImplicitParam(name = "limit", value = "返回数量", defaultValue = "4", dataTypeClass = Integer.class, paramType = "query")  
    public Result<List<SkillVO>> getHotSkills(@RequestParam(defaultValue = "4") Integer limit) {  
        // 使用skillService实现热门技能逻辑  
        List<SkillVO> hotSkills = skillService.getHotSkills(limit);  
        return Result.success(hotSkills);  
    }  

    /**  
     * 7. 获取推荐技能  
     */  
    @GetMapping("/recommended")  
    @ApiOperation(value = "获取推荐技能", notes = "获取推荐技能列表（可能需要认证以提供个性化推荐）")  
    @ApiImplicitParams({  
            @ApiImplicitParam(name = "limit", value = "返回数量", defaultValue = "10", dataTypeClass = Integer.class, paramType = "query"),  
            @ApiImplicitParam(name = "Authorization", value = "Bearer Token (可选, 用于个性化推荐)", dataTypeClass = String.class, paramType = "header")  
    })  
    public Result<List<SkillVO>> getRecommendedSkills(@RequestParam(defaultValue = "10") Integer limit) {  
        // 可选地从Authorization令牌获取用户ID  
        Integer currentUserId = getCurrentUserIdOrNull(); // 获取用户ID或null  
        List<SkillVO> recommendedSkills = skillService.getRecommendedSkills(limit, currentUserId);  
        return Result.success(recommendedSkills);  
    }  

     // --- 辅助方法 ---  

     private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) authentication.getPrincipal()).getId();
        } else {
            // 或者可以抛出一个更具体的未认证异常
            throw new UnauthorizedException("用户未认证或无法获取用户信息"); 
        }
        // return 1; // 移除占位符
     }
    
     private Integer getCurrentUserIdOrNull() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetailsImpl) {
              return ((UserDetailsImpl) authentication.getPrincipal()).getId();
              // return 1; // 移除占位符 
         }
         return null; 
     }

}  