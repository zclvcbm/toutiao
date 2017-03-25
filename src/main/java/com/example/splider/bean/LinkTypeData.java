package com.example.splider.bean;

/**
 * Created by Administrator on 2016/7/24.
 */
public class LinkTypeData {
    private int id;

    /**
     * 链接的地址
     */
    private String linkHref;

    /**
     * 链接的标题
     */
    private String linkText;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片链接
     * @return
     */
    private String src;

    public String getSrc(){
        return src;
    }

    public void setSrc(String src){
        this.src = src;
    }

    public String getLinkHref() {
        return linkHref;
    }

    public void setLinkHref(String linkHref) {
        this.linkHref = linkHref;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LinkTypeData [id="+id+",linkHref="+linkHref
                +", linkText="+linkText+", summary="+summary
                +", content="+content+", src="+src+"]";
    }

}
