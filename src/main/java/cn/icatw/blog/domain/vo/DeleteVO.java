package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 逻辑删除
 *
 * @author 王顺
 * @date 2024/03/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "逻辑删除")
public class DeleteVO {

    /**
     * id列表
     */
    @NotNull(message = "id不能为空")
    @Schema(name = "idList", description = "id列表")
    private List<Integer> idList;

    /**
     * 状态值
     */
    @NotNull(message = "状态值不能为空")
    @Schema(name = "isDelete", description = "删除状态")
    private Integer isDelete;

}
