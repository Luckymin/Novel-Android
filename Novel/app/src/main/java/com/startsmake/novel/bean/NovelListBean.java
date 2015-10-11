package com.startsmake.novel.bean;

import java.util.List;

/**
 * User:Shine
 * Date:2015-08-12
 * Description:小说列表实体类
 */
public class NovelListBean {
    /**
     * books : [{"cover":"/agent/http://image.cmfu.com/books/2494758/2494758.jpg","site":"qidian","author":"莫默","cat":"玄幻","retentionRatio":79.71,"latelyFollower":1558,"_id":"50bee5172033d09b2f00001b","title":"武炼巅峰","lastChapter":"第两千四百一十二章 紫雨","shortIntro":"武之巅峰，是孤独，是寂寞，是漫漫求索，是高处不胜寒 逆境中成长，绝地里求生，不屈不饶，才能堪破武之极道。 凌霄阁试炼弟子兼扫地小厮杨开偶获一本无字黑书，从此踏上...","tags":["玄幻","热血","架空","巅峰","奇遇","升级练功","东方玄幻"]},{"cover":"/agent/http://static.zongheng.com/upload/cover/2014/11/1416425191645.jpg","site":"zongheng","author":"火星引力","cat":"玄幻","retentionRatio":79.67,"latelyFollower":950,"_id":"542a5838a5ae10f815039a7f","title":"逆天邪神","lastChapter":"第447章 小铭铭，要点碧莲","shortIntro":"掌天毒之珠，承邪神之血，修逆天之力，一代邪神，君临天下！【添加微信公众号：火星引力】【我们的YY频道：49554】","tags":["热血","异界","争霸","东方玄幻"]},{"cover":"/agent/http://image.cmfu.com/books/3258971/3258971.jpg","site":"qidian","author":"厌笔萧生","cat":"玄幻","retentionRatio":79.28,"latelyFollower":2645,"_id":"53e56ee335f79bb626a496c9","title":"帝霸","lastChapter":"第1136章横推万敌","shortIntro":"千万年前，李七夜栽下一株翠竹。 八百万年前，李七夜养了一条鲤鱼。 五百万年前，李七夜收养一个小女孩。 \u2026\u2026\u2026\u2026\u2026\u2026\u2026\u2026\u2026\u2026 今天，李七夜一觉醒来，翠竹修练成神灵...","tags":["玄幻","热血","架空","巅峰","传奇","东方玄幻"]},{"cover":"/agent/http://image.cmfu.com/books/2407162/2407162.jpg","site":"qidian","author":"傅啸尘","cat":"玄幻","retentionRatio":79.25,"latelyFollower":488,"_id":"50937e348d834c0f1900012b","title":"武神空间","lastChapter":"第三千一百二十二章 一击动天地","shortIntro":"叶希文本只是地球上一个普通的大学生，却意外穿越到了一个名为真武界的世界！ 在这个世界中，强大的武者能翻山倒海，毁天灭地！ 本是资质平凡的他，因为得到了一个神秘的...","tags":["重生","玄幻","热血","架空","升级练功","修炼","东方玄幻"]},{"cover":"/agent/http://image.cmfu.com/books/3173393/3173393.jpg","site":"qidian","author":"永恒之火","cat":"玄幻","retentionRatio":79.14,"latelyFollower":1358,"_id":"5373898f1032be0155019e73","title":"儒道至圣","lastChapter":"第898章 三月十九","shortIntro":"这是一个读书人掌握天地之力的世界。 才气在身，诗可杀敌，词能灭军，文章安天下。 秀才提笔，纸上谈兵；举人杀敌，出口成章；进士一怒，唇枪舌剑。 圣人驾临，口诛笔伐...","tags":["谋权","玄幻","热血","架空","战争","奇遇","东方玄幻"]}]
     * ok : true
     */
    private List<BooksEntity> books;
    private boolean ok;

    public void setBooks(List<BooksEntity> books) {
        this.books = books;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<BooksEntity> getBooks() {
        return books;
    }

    public boolean isOk() {
        return ok;
    }

    public static class BooksEntity {
        /**
         * cover : /agent/http://image.cmfu.com/books/2494758/2494758.jpg
         * site : qidian
         * author : 莫默
         * cat : 玄幻
         * retentionRatio : 79.71
         * latelyFollower : 1558
         * _id : 50bee5172033d09b2f00001b
         * title : 武炼巅峰
         * lastChapter : 第两千四百一十二章 紫雨
         * shortIntro : 武之巅峰，是孤独，是寂寞，是漫漫求索，是高处不胜寒 逆境中成长，绝地里求生，不屈不饶，才能堪破武之极道。 凌霄阁试炼弟子兼扫地小厮杨开偶获一本无字黑书，从此踏上...
         * tags : ["玄幻","热血","架空","巅峰","奇遇","升级练功","东方玄幻"]
         */
        private String cover;
        private String site;
        private String author;
        private String cat;
        private double retentionRatio;
        private int latelyFollower;
        private String _id;
        private String title;
        private String lastChapter;
        private String shortIntro;
        private List<String> tags;

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setCat(String cat) {
            this.cat = cat;
        }

        public void setRetentionRatio(double retentionRatio) {
            this.retentionRatio = retentionRatio;
        }

        public void setLatelyFollower(int latelyFollower) {
            this.latelyFollower = latelyFollower;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setLastChapter(String lastChapter) {
            this.lastChapter = lastChapter;
        }

        public void setShortIntro(String shortIntro) {
            this.shortIntro = shortIntro;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getCover() {
            return cover;
        }

        public String getSite() {
            return site;
        }

        public String getAuthor() {
            return author;
        }

        public String getCat() {
            return cat;
        }

        public double getRetentionRatio() {
            return retentionRatio;
        }

        public int getLatelyFollower() {
            return latelyFollower;
        }

        public String get_id() {
            return _id;
        }

        public String getTitle() {
            return title;
        }

        public String getLastChapter() {
            return lastChapter;
        }

        public String getShortIntro() {
            return shortIntro;
        }

        public List<String> getTags() {
            return tags;
        }
    }
}
