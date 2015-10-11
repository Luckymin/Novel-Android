package com.startsmake.novel.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * User:Shine
 * Date:2015-08-11
 * Description:小说分类实体类
 */
public class BookClassificationBean implements Parcelable {

    /**
     * ok : true
     * female : [{"bookCount":51417,"name":"都市言情"},{"bookCount":34325,"name":"幻想言情"},{"bookCount":38510,"name":"古代言情"},{"bookCount":30846,"name":"穿越"},{"bookCount":29566,"name":"总裁"},{"bookCount":6564,"name":"宫廷"},{"bookCount":21150,"name":"校园"},{"bookCount":1724,"name":"种田"},{"bookCount":4023,"name":"女尊"},{"bookCount":13917,"name":"悬疑"},{"bookCount":30913,"name":"耽美"},{"bookCount":25035,"name":"同人美文"}]
     * male : [{"bookCount":112194,"name":"玄幻"},{"bookCount":18056,"name":"奇幻"},{"bookCount":44355,"name":"仙侠"},{"bookCount":10880,"name":"武侠"},{"bookCount":86125,"name":"都市"},{"bookCount":4724,"name":"军事"},{"bookCount":9565,"name":"科幻"},{"bookCount":20279,"name":"历史"},{"bookCount":24352,"name":"网游"},{"bookCount":2265,"name":"体育"},{"bookCount":13917,"name":"悬疑"},{"bookCount":840,"name":"机甲"},{"bookCount":7412,"name":"末世"},{"bookCount":6018,"name":"无限"},{"bookCount":2481,"name":"系统"},{"bookCount":518,"name":"重生"},{"bookCount":21684,"name":"同人"},{"bookCount":1589,"name":"轻小说"}]
     */
    private boolean ok;
    private List<BookCatsEntity> female;
    private List<BookCatsEntity> male;

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public void setFemale(List<BookCatsEntity> female) {
        this.female = female;
    }

    public void setMale(List<BookCatsEntity> male) {
        this.male = male;
    }

    public boolean isOk() {
        return ok;
    }

    public List<BookCatsEntity> getFemale() {
        return female;
    }

    public List<BookCatsEntity> getMale() {
        return male;
    }

    public static class BookCatsEntity implements Parcelable {
        /**
         * bookCount : 51417
         * name : 都市言情
         */
        private String bookCount;
        private String name;

        public void setBookCount(String bookCount) {
            this.bookCount = bookCount;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBookCount() {
            return bookCount;
        }

        public String getName() {
            return name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.bookCount);
            dest.writeString(this.name);
        }

        public BookCatsEntity() {
        }

        protected BookCatsEntity(Parcel in) {
            this.bookCount = in.readString();
            this.name = in.readString();
        }

        public static final Creator<BookCatsEntity> CREATOR = new Creator<BookCatsEntity>() {
            public BookCatsEntity createFromParcel(Parcel source) {
                return new BookCatsEntity(source);
            }

            public BookCatsEntity[] newArray(int size) {
                return new BookCatsEntity[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(ok ? (byte) 1 : (byte) 0);
        dest.writeList(this.female);
        dest.writeList(this.male);
    }

    public BookClassificationBean() {
    }

    protected BookClassificationBean(Parcel in) {
        this.ok = in.readByte() != 0;
        this.female = new ArrayList<BookCatsEntity>();
        in.readList(this.female, List.class.getClassLoader());
        this.male = new ArrayList<BookCatsEntity>();
        in.readList(this.male, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<BookClassificationBean> CREATOR = new Parcelable.Creator<BookClassificationBean>() {
        public BookClassificationBean createFromParcel(Parcel source) {
            return new BookClassificationBean(source);
        }

        public BookClassificationBean[] newArray(int size) {
            return new BookClassificationBean[size];
        }
    };
}

