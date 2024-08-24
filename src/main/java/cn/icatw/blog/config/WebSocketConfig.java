package cn.icatw.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocketConfig
 *
 * @author 王顺
 * @date 2024/04/10
 */
@Configuration
public class WebSocketConfig {

    /**
     * 注入一个ServerEndpointExporter，该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     *
     * @return {@link ServerEndpointExporter}
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
