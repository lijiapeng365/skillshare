package com.cloudladdertech.skillshare.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页结果视图对象
 * @param <T> 列表项类型
 */
@Data
@ApiModel(value = "分页结果", description = "包含分页信息和数据列表的结果集")
public class PagedResult<T> {
    
    @ApiModelProperty(value = "数据列表")
    private List<T> list;
    
    @ApiModelProperty(value = "分页信息")
    private Pagination pagination;
    
    @Data
    @ApiModel(value = "分页信息", description = "包含当前页码、每页数量、总条目数和总页数")
    public static class Pagination {
        
        @ApiModelProperty(value = "当前页码", example = "1")
        private Integer currentPage;
        
        @ApiModelProperty(value = "每页数量", example = "10")
        private Integer pageSize;
        
        @ApiModelProperty(value = "总条目数", example = "100")
        private Long totalItems;
        
        @ApiModelProperty(value = "总页数", example = "10")
        private Integer totalPages;
    }
}

