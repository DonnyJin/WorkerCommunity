package com.donny.community;

import com.donny.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveTest {

    @Autowired
    SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveWords() {
        String text = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        System.out.println(sensitiveFilter.filter(text));

    }
}
