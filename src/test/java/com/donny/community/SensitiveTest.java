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
        String text1 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text2 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text3 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text4 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text5 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text6 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text7 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text8 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text9 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text10 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text11 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text12 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text13 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text14 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text16 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text17 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text18 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text19 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text20 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text21 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text22 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text23 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text24 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text25 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";
        String text26 = "我叫程子233,我喜欢赌博,嫖娼, 吸@&#*%*&毒,haha";

        System.out.println(sensitiveFilter.filter(text));

    }
}
