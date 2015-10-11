package com.startsmake.novel.bean;

import java.util.List;

/**
 * User:Shine
 * Date:2015-08-13
 * Description:小说信息实体类
 */
public class NovelInfoBean {

    /**
     * wordCount : 10878682
     * gender : ["male"]
     * author : 禹枫
     * latelyFollower : 665
     * title : 异世灵武天下
     * lastChapter : 后续五：大结局终章
     * tags : ["重生","转世重生","穿越","玄幻","架空","异世","废柴流","传奇"]
     * cover : /agent/http://images.zhulang.com/book_cover/image/18/98/189843.jpg
     * isSerial : false
     * serializeWordCount : -1
     * cat : 玄幻
     * retentionRatio : 73.21
     * creater : iPhone 4
     * postCount : 881
     * longIntro : 穿越后，成为已死的废柴少爷，遇上了神秘老者南叔。
     为亲情，为红颜，为身边最亲的人，陆少游从废柴一步步踏上强者之路。
     强者之路，一路荆棘遍布，却也阻挡不住一颗强者之心。
     醉卧美人膝，醒掌天下权，传言武道巅峰，灵道极致，便能踏碎虚空。灵武双修，不世霸枭，既然来了，就要留下一世传奇。
     【小禹公众微信平台已经开通，搜索‘禹枫YF’就可添加。】。
     * _id : 5106099abb1c67cf28000016
     * followerCount : 5164
     * updated : 2014-07-18T01:22:54.015Z
     */
    private int wordCount;
    private List<String> gender;
    private String author;
    private int latelyFollower;
    private String title;
    private String lastChapter;
    private List<String> tags;
    private String cover;
    private boolean isSerial;
    private int serializeWordCount;
    private String cat;
    private String retentionRatio;
    private String creater;
    private int postCount;
    private String longIntro;
    private String _id;
    private int followerCount;
    private String updated;

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLatelyFollower(int latelyFollower) {
        this.latelyFollower = latelyFollower;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setIsSerial(boolean isSerial) {
        this.isSerial = isSerial;
    }

    public void setSerializeWordCount(int serializeWordCount) {
        this.serializeWordCount = serializeWordCount;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setRetentionRatio(String retentionRatio) {
        this.retentionRatio = retentionRatio;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public void setLongIntro(String longIntro) {
        this.longIntro = longIntro;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public int getWordCount() {
        return wordCount;
    }

    public List<String> getGender() {
        return gender;
    }

    public String getAuthor() {
        return author;
    }

    public int getLatelyFollower() {
        return latelyFollower;
    }

    public String getTitle() {
        return title;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getCover() {
        return cover;
    }

    public boolean isIsSerial() {
        return isSerial;
    }

    public int getSerializeWordCount() {
        return serializeWordCount;
    }

    public String getCat() {
        return cat;
    }

    public String getRetentionRatio() {
        return retentionRatio;
    }

    public String getCreater() {
        return creater;
    }

    public int getPostCount() {
        return postCount;
    }

    public String getLongIntro() {
        return longIntro;
    }

    public String get_id() {
        return _id;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public String getUpdated() {
        return updated;
    }
}
