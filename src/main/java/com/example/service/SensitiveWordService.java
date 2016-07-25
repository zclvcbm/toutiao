package com.example.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 2016/7/23.
 */
@Service
public class SensitiveWordService {

    public static void main(String[] args){
        SensitiveWordService sws = new SensitiveWordService();

        String str = sws.filterSentitiveWord("Hello zcsmart! My name is zhang.",new String[]{"zcsmart","zhang"});
        System.out.println(str);

        String str1 = sws.filterSentitiveWord(null,new String[]{"zcsmart","zhang"});
        System.out.println(str1);

        String str2 = sws.filterSentitiveWord("Hello zcsmart! My name is zhang.",null);
        System.out.println(str2);

        String str3 = sws.filterSentitiveWord("zcsmart",new String[]{"zcsmart1","zhang"});
        System.out.println(str3);

    }

    public String filterSentitiveWord(String str, String[] words){
        if(str==null)
            return null;
        if(words==null || words.length==0)
            return str;
        // 通过字符串数组建立Trie树
        TrieNode root = new TrieNode(false);
        TrieNode p;
        for(int i=0;i<words.length;i++){
            p = root;                   //对每一个关键词是都从第一个根节点进行建树
            String s = words[i];
            int wlen = s.length();
            for(int j=0;j<wlen;j++){
                if(p.childNode.containsKey(s.charAt(j))){   //字符已经出现过，直接获得该节点
                    p = p.childNode.get(s.charAt(j));
                }else{
                    if(j==wlen-1) {                         //关键字结尾
                        TrieNode node = new TrieNode(true);
                        p.childNode.put(s.charAt(j),node);
                        p = node;
                    }else{
                        TrieNode node = new TrieNode(false);
                        p.childNode.put(s.charAt(j),node);
                        p = node;
                    }
                }
            }
        }

        // 对字符串进行过滤
        int i;
        int j;
        p = root ;
        for(i=0;i<str.length();i++){
            j=i;
            while(j<str.length() && p.childNode.containsKey(str.charAt(j))){
                p = p.childNode.get(str.charAt(j));
                if(p.isEnding){
                    //System.out.println(i+" "+j);
                    //实现对i~j字符替换
                    StringBuilder sb = new StringBuilder(str);
                    StringBuilder replace = new StringBuilder();
                    for(int k=i;k<=j;k++){
                        replace.append("*");
                    }
                    sb.replace(i,j+1,new String(replace));
                    str = new String(sb);
                    i = j;  //找到匹配关键字，调整下次查找位置
                    p = root; //从Trie树根节点开始遍历起
                }
                j++;
            }
        }
        return str;
    }
}

class TrieNode{
    boolean isEnding;
    Map<Character,TrieNode> childNode;

    public TrieNode(boolean isEnding){
        this.isEnding = isEnding;
        childNode = new HashMap<>();
    }
}
