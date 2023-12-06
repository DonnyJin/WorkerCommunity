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

        System.out.println(sensitiveFilter.filter(text3));
        System.out.println(sensitiveFilter.filter(text4));
        System.out.println(sensitiveFilter.filter(text5));
        System.out.println(sensitiveFilter.filter(text6));
        System.out.println(sensitiveFilter.filter(text7));
        System.out.println(sensitiveFilter.filter(text8));
        System.out.println(sensitiveFilter.filter(text9));
        System.out.println(sensitiveFilter.filter(text10));
        System.out.println(sensitiveFilter.filter(text11));
        System.out.println(sensitiveFilter.filter(text12));
        System.out.println(sensitiveFilter.filter(text13));
        System.out.println(sensitiveFilter.filter(text14));
        System.out.println(sensitiveFilter.filter(text16));
        System.out.println(sensitiveFilter.filter(text16));
        System.out.println(sensitiveFilter.filter(text17));
        System.out.println(sensitiveFilter.filter(text18));
        System.out.println(sensitiveFilter.filter(text19));
        System.out.println(sensitiveFilter.filter(text20));
        System.out.println(sensitiveFilter.filter(text21));
        System.out.println(sensitiveFilter.filter(text22));
        System.out.println(sensitiveFilter.filter(text23));
        System.out.println(sensitiveFilter.filter(text24));
        System.out.println(sensitiveFilter.filter(text25));
        System.out.println(sensitiveFilter.filter(text26));
        System.out.println(sensitiveFilter.filter(text2));
        System.out.println(sensitiveFilter.filter(text3));
        System.out.println(sensitiveFilter.filter(text3));
        System.out.println(sensitiveFilter.filter(text3));
        System.out.println(sensitiveFilter.filter(text3));
        System.out.println(sensitiveFilter.filter(text3));
        System.out.println(sensitiveFilter.filter(text3));
        System.out.println(sensitiveFilter.filter(text3));
        System.out.println(sensitiveFilter.filter(text3));

    }
}
