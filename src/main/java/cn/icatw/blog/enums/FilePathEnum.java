package cn.icatw.blog.enums;

import cn.icatw.blog.excetion.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件路径枚举
 *
 * @author 王顺
 * @date 2024/04/07
 */
@Getter
@AllArgsConstructor
public enum FilePathEnum {
    /**
     * 头像路径
     */
    AVATAR("avatar/", "头像路径", "avatar"),
    /**
     * 文章图片路径
     */
    ARTICLE("articles/", "文章图片路径", "article"),
    /**
     * 音频路径
     */
    VOICE("voice/", "音频路径", "voice"),
    /**
     * 照片路径
     */
    PHOTO("photos/", "相册路径", "photo"),
    /**
     * 配置图片路径
     */
    CONFIG("config/", "配置图片路径", "config"),
    /**
     * 说说图片路径
     */
    TALK("talks/", "说说图片路径", "talk"),
    /**
     * md文件路径
     */
    MD("markdown/", "md文件路径", "md"),
    PDF("pdf/", "pdf文件路径", "pdf");

    /**
     * 路径
     */
    private final String path;

    /**
     * 描述
     */
    private final String desc;
    /**
     * 类型
     */
    private final String type;

    /**
     * 获取文件路径
     *
     * @param type 类型
     * @return {@link FilePathEnum} 文件路径
     */
    public static FilePathEnum getFilePath(String type) {
        for (FilePathEnum value : FilePathEnum.values()) {
            if (value.getType().equalsIgnoreCase(type)) {
                return value;
            }
        }
        throw new BizException("文件路径不存在");
    }
}
