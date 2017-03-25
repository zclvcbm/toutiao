package com.example.splider.core;

import com.example.splider.bean.LinkTypeData;
import com.example.splider.rule.Rule;
import com.example.splider.rule.RuleException;
import com.example.splider.utils.TextUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/7/24.
 */
public class ExtractService {
    /**
     * @param rule
     * @return
     */
    public static List<LinkTypeData> extract(Rule rule){
        // 进行对rule的必要验证
        validateRule(rule);

        List<LinkTypeData> datas = new ArrayList<LinkTypeData>();
        LinkTypeData data = null;
        try{
            /**
             * 解析rule
             */
            String url = rule.getUrl();
            String[] params = rule.getParams();
            String[] values = rule.getValues();
            String resultTagName = rule.getResultTypeName();
            int type = rule.getType();
            int requestType = rule.getRequestMethod();

            Connection connection = Jsoup.connect(url);

            // 设置查询的参数

            if(params != null) {
                for(int i=0;i<params.length;i++){
                    connection.data(params[i],values[i]);
                }
            }

            // 设置请求类型
            Document doc = null;
            switch(requestType)
            {
                case Rule.GET:
                    doc = connection.timeout(100000).get();
                    break;
                case Rule.POST:
                    doc = connection.timeout(100000).post();
                    break;
            }

            // 处理返回数据
            Elements results = new Elements();
            switch (type){
                case Rule.CLASS:
                    results = doc.getElementsByClass(resultTagName);
                    break;
                case Rule.ID:
                    Element result = doc.getElementById(resultTagName);
                    results.add(result);
                    break;
                case Rule.SELECTION:
                    results = doc.select(resultTagName);
                    break;
                default:
                    //当resultTagName为空时默认去body标签
                    if(TextUtil.isEmpty(resultTagName)){
                        results = doc.getElementsByTag("body");
                    }
            }

            for(Element result :results){
                /*
                Elements links = result.getElementsByTag("a");
                for(Element link : links){
                    //必要的筛选
                    String linkHref = link.attr("href");
                    String linkImage = "";
                    if(link.children().size()>0)
                        linkImage =  link.child(0).attr("_src");
                    String linkText = link.text();

                    data = new LinkTypeData();
                    data.setLinkHref(linkHref);
                    data.setLinkText(linkText);
                    data.setSrc(linkImage);

                    datas.add(data);
                }

            */
                Element link = result.getElementsByTag("a").get(1);
                //必要的筛选
                String linkHref = link.attr("href");
                String linkText = link.text();

                Element image = result.getElementsByTag("img").get(0);
                String imageSrc = image.attr("_src")==""?image.attr("src"):image.attr("_src");

                data = new LinkTypeData();
                data.setLinkHref(linkHref);
                data.setLinkText(linkText);
                data.setSrc(imageSrc);

                datas.add(data);

            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return datas;
    }

    /**
     * 对传入的参数进行必要的验证
     */
    private static void validateRule(Rule rule){
        String url = rule.getUrl();
        if(TextUtil.isEmpty(url)){
            throw new RuleException();
        }
        if(!url.startsWith("http://")){
            throw new RuleException("url的格式不正确！");
        }
        if(rule.getParams()!=null && rule.getValues()!=null){
            if(rule.getParams().length != rule.getValues().length)
            {
                throw new RuleException("参数的键值对个数不匹配！");
            }
        }
    }
}
