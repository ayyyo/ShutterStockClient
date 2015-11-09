package de.shutterstock.android.shutterstock.content.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emanuele on 06/11/15.
 */
public class Image {

    public static class Assets {
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
    }


    public static class Category {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public static class Contributor {
        @SerializedName("id")
        @Expose
        public String id;
    }

    public static class Datum {
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
    }

    public class Thumbnail {

        @SerializedName("height")
        @Expose
        public int height;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("width")
        @Expose
        public int width;
    }

    public class ImageSizeDetails {

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
}
