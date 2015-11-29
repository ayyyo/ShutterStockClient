package de.shutterstock.android.shutterstock.content.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emanuele on 06/11/15.
 */
public class Image implements Parcelable {

    public static class Assets implements Parcelable {
        @SerializedName("huge_jpg")
        @Expose
        public ImageSizeDetails hugeJpg;
        @SerializedName("medium_jpg")
        @Expose
        public ImageSizeDetails mediumJpg;
        @SerializedName("huge_tiff")
        @Expose
        public ImageSizeDetails hugeTiff;
        @SerializedName("supersize_jpg")
        @Expose
        public ImageSizeDetails supersizeJpg;
        @SerializedName("supersize_tiff")
        @Expose
        public ImageSizeDetails supersizeTiff;
        @SerializedName("vector_eps")
        @Expose
        public ImageSizeDetails vectorEps;
        @SerializedName("small_jpg")
        @Expose
        public ImageSizeDetails smallJpg;


        @SerializedName("preview_1000")
        @Expose
        public Thumbnail preview1000;
        @SerializedName("preview")
        @Expose
        public Thumbnail preview;
        @SerializedName("small_thumb")
        @Expose
        public Thumbnail smallThumb;
        @SerializedName("large_thumb")
        @Expose
        public Thumbnail largeThumb;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.hugeJpg, 0);
            dest.writeParcelable(this.mediumJpg, 0);
            dest.writeParcelable(this.hugeTiff, 0);
            dest.writeParcelable(this.supersizeJpg, 0);
            dest.writeParcelable(this.supersizeTiff, 0);
            dest.writeParcelable(this.vectorEps, 0);
            dest.writeParcelable(this.smallJpg, 0);
            dest.writeParcelable(this.preview1000, 0);
            dest.writeParcelable(this.preview, 0);
            dest.writeParcelable(this.smallThumb, 0);
            dest.writeParcelable(this.largeThumb, 0);
        }

        public Assets() {
        }

        protected Assets(Parcel in) {
            this.hugeJpg = in.readParcelable(ImageSizeDetails.class.getClassLoader());
            this.mediumJpg = in.readParcelable(ImageSizeDetails.class.getClassLoader());
            this.hugeTiff = in.readParcelable(ImageSizeDetails.class.getClassLoader());
            this.supersizeJpg = in.readParcelable(ImageSizeDetails.class.getClassLoader());
            this.supersizeTiff = in.readParcelable(ImageSizeDetails.class.getClassLoader());
            this.vectorEps = in.readParcelable(ImageSizeDetails.class.getClassLoader());
            this.smallJpg = in.readParcelable(ImageSizeDetails.class.getClassLoader());
            this.preview1000 = in.readParcelable(Thumbnail.class.getClassLoader());
            this.preview = in.readParcelable(Thumbnail.class.getClassLoader());
            this.smallThumb = in.readParcelable(Thumbnail.class.getClassLoader());
            this.largeThumb = in.readParcelable(Thumbnail.class.getClassLoader());
        }

        public static final Creator<Assets> CREATOR = new Creator<Assets>() {
            public Assets createFromParcel(Parcel source) {
                return new Assets(source);
            }

            public Assets[] newArray(int size) {
                return new Assets[size];
            }
        };
    }


    public static class Category implements Parcelable {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
        }

        public Category() {
        }

        protected Category(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
        }

        public static final Creator<Category> CREATOR = new Creator<Category>() {
            public Category createFromParcel(Parcel source) {
                return new Category(source);
            }

            public Category[] newArray(int size) {
                return new Category[size];
            }
        };
    }

    public static class Contributor implements Parcelable {
        @SerializedName("id")
        @Expose
        public String id;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
        }

        public Contributor() {
        }

        protected Contributor(Parcel in) {
            this.id = in.readString();
        }

        public static final Creator<Contributor> CREATOR = new Creator<Contributor>() {
            public Contributor createFromParcel(Parcel source) {
                return new Contributor(source);
            }

            public Contributor[] newArray(int size) {
                return new Contributor[size];
            }
        };
    }

    public static class Datum implements Parcelable {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("added_date")
        @Expose
        public String addedDate;
        @SerializedName("aspect")
        @Expose
        public Double aspect;
        @SerializedName("assets")
        @Expose
        public Assets assets;
        @SerializedName("categories")
        @Expose
        public List<Category> categories = new ArrayList<Category>();
        @SerializedName("contributor")
        @Expose
        public Contributor contributor;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("image_type")
        @Expose
        public String imageType;
        @SerializedName("is_adult")
        @Expose
        public Boolean isAdult;
        @SerializedName("is_illustration")
        @Expose
        public Boolean isIllustration;
        @SerializedName("keywords")
        @Expose
        public List<String> keywords = new ArrayList<String>();
        @SerializedName("media_type")
        @Expose
        public String mediaType;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.addedDate);
            dest.writeValue(this.aspect);
            dest.writeParcelable(this.assets, flags);
            dest.writeList(this.categories);
            dest.writeParcelable(this.contributor, flags);
            dest.writeString(this.description);
            dest.writeString(this.imageType);
            dest.writeValue(this.isAdult);
            dest.writeValue(this.isIllustration);
            dest.writeStringList(this.keywords);
            dest.writeString(this.mediaType);
        }

        public Datum() {
        }

        protected Datum(Parcel in) {
            this.id = in.readString();
            this.addedDate = in.readString();
            this.aspect = (Double) in.readValue(Double.class.getClassLoader());
            this.assets = in.readParcelable(Assets.class.getClassLoader());
            this.categories = new ArrayList<Category>();
            in.readList(this.categories, List.class.getClassLoader());
            this.contributor = in.readParcelable(Contributor.class.getClassLoader());
            this.description = in.readString();
            this.imageType = in.readString();
            this.isAdult = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.isIllustration = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.keywords = in.createStringArrayList();
            this.mediaType = in.readString();
        }

        public static final Creator<Datum> CREATOR = new Creator<Datum>() {
            public Datum createFromParcel(Parcel source) {
                return new Datum(source);
            }

            public Datum[] newArray(int size) {
                return new Datum[size];
            }
        };
    }

    public static class Thumbnail implements Parcelable {

        @SerializedName("height")
        @Expose
        public int height;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("width")
        @Expose
        public int width;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.height);
            dest.writeString(this.url);
            dest.writeInt(this.width);
        }

        public Thumbnail() {
        }

        protected Thumbnail(Parcel in) {
            this.height = in.readInt();
            this.url = in.readString();
            this.width = in.readInt();
        }

        public static final Creator<Thumbnail> CREATOR = new Creator<Thumbnail>() {
            public Thumbnail createFromParcel(Parcel source) {
                return new Thumbnail(source);
            }

            public Thumbnail[] newArray(int size) {
                return new Thumbnail[size];
            }
        };
    }

    public static class ImageSizeDetails implements Parcelable {

        @SerializedName("display_name")
        @Expose
        public String displayName;
        @SerializedName("dpi")
        @Expose
        public Integer dpi;
        @SerializedName("file_size")
        @Expose
        public Integer fileSize;
        @SerializedName("format")
        @Expose
        public String format;
        @SerializedName("height")
        @Expose
        public Integer height;
        @SerializedName("is_licensable")
        @Expose
        public Boolean isLicensable;
        @SerializedName("width")
        @Expose
        public Integer width;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.displayName);
            dest.writeValue(this.dpi);
            dest.writeValue(this.fileSize);
            dest.writeString(this.format);
            dest.writeValue(this.height);
            dest.writeValue(this.isLicensable);
            dest.writeValue(this.width);
        }

        public ImageSizeDetails() {
        }

        protected ImageSizeDetails(Parcel in) {
            this.displayName = in.readString();
            this.dpi = (Integer) in.readValue(Integer.class.getClassLoader());
            this.fileSize = (Integer) in.readValue(Integer.class.getClassLoader());
            this.format = in.readString();
            this.height = (Integer) in.readValue(Integer.class.getClassLoader());
            this.isLicensable = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.width = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public static final Creator<ImageSizeDetails> CREATOR = new Creator<ImageSizeDetails>() {
            public ImageSizeDetails createFromParcel(Parcel source) {
                return new ImageSizeDetails(source);
            }

            public ImageSizeDetails[] newArray(int size) {
                return new ImageSizeDetails[size];
            }
        };
    }

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("added_date")
    @Expose
    public String addedDate;
    @SerializedName("aspect")
    @Expose
    public float aspect;
    @SerializedName("assets")
    @Expose
    public Assets assets;
    @SerializedName("categories")
    @Expose
    public List<Category> categories = new ArrayList<>();
    @SerializedName("contributor")
    @Expose
    public Contributor contributor;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("image_type")
    @Expose
    public String imageType;
    @SerializedName("is_adult")
    @Expose
    public Boolean isAdult;
    @SerializedName("is_illustration")
    @Expose
    public Boolean isIllustration;
    @SerializedName("keywords")
    @Expose
    public List<String> keywords = new ArrayList<>();
    @SerializedName("media_type")
    @Expose
    public String mediaType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.addedDate);
        dest.writeFloat(this.aspect);
        dest.writeParcelable(this.assets, flags);
        dest.writeList(this.categories);
        dest.writeParcelable(this.contributor, flags);
        dest.writeString(this.description);
        dest.writeString(this.imageType);
        dest.writeValue(this.isAdult);
        dest.writeValue(this.isIllustration);
        dest.writeStringList(this.keywords);
        dest.writeString(this.mediaType);
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.id = in.readString();
        this.addedDate = in.readString();
        this.aspect = in.readFloat();
        this.assets = in.readParcelable(Assets.class.getClassLoader());
        this.categories = new ArrayList<>();
        in.readList(this.categories, Category.class.getClassLoader());
        this.contributor = in.readParcelable(Contributor.class.getClassLoader());
        this.description = in.readString();
        this.imageType = in.readString();
        this.isAdult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isIllustration = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.keywords = in.createStringArrayList();
        this.mediaType = in.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
