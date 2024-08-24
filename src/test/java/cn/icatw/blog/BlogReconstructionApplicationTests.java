package cn.icatw.blog;

import cn.icatw.blog.domain.dto.CoverDTO;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogReconstructionApplicationTests {
    @Autowired
    RestTemplate restTemplate;

    @Test
    void contextLoads() {
        System.out.println("Hello World!");
    }

    @Test
    void getRandomCover() {
        System.out.println("getRandomCover");
        String jsonStr = restTemplate.getForObject("https://www.dmoe.cc/random.php?return=json", String.class);
        CoverDTO coverDTO = JSON.parseObject(jsonStr, CoverDTO.class);
        System.out.println(jsonStr);
    }
}
