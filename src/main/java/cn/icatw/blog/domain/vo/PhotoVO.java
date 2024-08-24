package cn.icatw.blog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 照片
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "照片")
public class PhotoVO {

    /**
     * 相册id
     */
    @NotNull(message = "相册id不能为空")
    @Schema(name = "id", description = "相册id")
    private Integer albumId;

    /**
     * 照片url列表
     */
    @Schema(name = "photoUrlList", description = "照片列表")
    private List<String> photoUrlList;

    /**
     * 照片id列表
     */
    @Schema(name = "photoIdList", description = "照片id列表")
    private List<Integer> photoIdList;

}
