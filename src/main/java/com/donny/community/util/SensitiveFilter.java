package com.donny.community.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SensitiveFilter {

    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode root = new TrieNode();

    @PostConstruct
    public void init() {
        try  {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 敏感词添加到前缀树
                this.addKeyWord(keyword);
            }
        } catch (IOException e) {
            log.error("加载敏感词文件失败: " + e.getMessage());
        }

    }

    private void addKeyWord(String keyword) {
        TrieNode temp = root;
        for (int i = 0 ; i < keyword.length() ; i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = temp.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                temp.addSubNode(c, subNode);
            }
            // 指向子节点, 进入下一轮循环
            temp = subNode;

            // 设置结束标识
            if (i == keyword.length() - 1) {
                temp.setKeyWordEnd(true);
            }


        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String sensitiveFilter(String text) {
        if (!StringUtils.hasText(text)) return null;

        TrieNode temp = root;
        int begin = 0;
        int position = 0;
        StringBuilder sb  = new StringBuilder();

        while( position < text.length()) {
            char c = text.charAt(position);
            // 跳过符号
            if (isSymbol(c)) {
                //若指针1处于根节点,将此节点计入结果,让指针2向下走一步
                if (temp == root) {
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间，指针3都向下走一步
                position++;
                continue;
            }
            // 检查下级
            temp = temp.getSubNode(c);
            if (temp == null) {
                // 以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                begin++;
                position = begin;
                temp = root;
            } else if (temp.isKeyWordEnd()) {
                // 发现敏感词, 将begin-position的字符串替换掉
                sb.append(REPLACEMENT);
                position++;
                begin = position;
                temp = root;
            } else {
                position++;
            }


        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    /**
     * 判断是否为符号
     * @param c
     * @return
     */
    private boolean isSymbol(Character c) {
        // 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    // 前缀树
    private class TrieNode {
        // 关键词结束标识
        private boolean isKeyWordEnd = false;

        private Map<Character, TrieNode> subNodes = new HashMap<>();

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }
    }

}
