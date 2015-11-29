package de.shutterstock.android.shutterstock.content.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emanuele on 29/11/15.
 */
public class Contributor implements Parcelable {

    public static class SocialMedia implements Parcelable {

        @SerializedName("facebook")
        @Expose
        public String facebook;
        @SerializedName("google_plus")
        @Expose
        public String googlePlus;
        @SerializedName("linkedin")
        @Expose
        public String linkedin;
        @SerializedName("pinterest")
        @Expose
        public String pinterest;
        @SerializedName("tumblr")
        @Expose
        public String tumblr;
        @SerializedName("twitter")
        @Expose
        public String twitter;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.facebook);
            dest.writeString(this.googlePlus);
            dest.writeString(this.linkedin);
            dest.writeString(this.pinterest);
            dest.writeString(this.tumblr);
            dest.writeString(this.twitter);
        }

        public SocialMedia() {
        }

        protected SocialMedia(Parcel in) {
            this.facebook = in.readString();
            this.googlePlus = in.readString();
            this.linkedin = in.readString();
            this.pinterest = in.readString();
            this.tumblr = in.readString();
            this.twitter = in.readString();
        }

        public static final Parcelable.Creator<SocialMedia> CREATOR = new Parcelable.Creator<SocialMedia>() {
            public SocialMedia createFromParcel(Parcel source) {
                return new SocialMedia(source);
            }

            public SocialMedia[] newArray(int size) {
                return new SocialMedia[size];
            }
        };
    }

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("display_name")
    @Expose
    public String displayName;
    @SerializedName("about")
    @Expose
    public String about;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("equipment")
    @Expose
    public List<String> equipment = new ArrayList<String>();
    @SerializedName("contributor_type")
    @Expose
    public List<String> contributorType = new ArrayList<String>();
    @SerializedName("styles")
    @Expose
    public List<String> styles = new ArrayList<String>();
    @SerializedName("subjects")
    @Expose
    public List<String> subjects = new ArrayList<String>();
    @SerializedName("website")
    @Expose
    public String website;
    @SerializedName("portfolio_url")
    @Expose
    public String portfolioUrl;
    @SerializedName("social_media")
    @Expose
    public SocialMedia socialMedia;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.displayName);
        dest.writeString(this.about);
        dest.writeString(this.location);
        dest.writeStringList(this.equipment);
        dest.writeStringList(this.contributorType);
        dest.writeStringList(this.styles);
        dest.writeStringList(this.subjects);
        dest.writeString(this.website);
        dest.writeString(this.portfolioUrl);
        dest.writeParcelable(this.socialMedia, 0);
    }

    public Contributor() {
    }

    protected Contributor(Parcel in) {
        this.id = in.readString();
        this.displayName = in.readString();
        this.about = in.readString();
        this.location = in.readString();
        this.equipment = in.createStringArrayList();
        this.contributorType = in.createStringArrayList();
        this.styles = in.createStringArrayList();
        this.subjects = in.createStringArrayList();
        this.website = in.readString();
        this.portfolioUrl = in.readString();
        this.socialMedia = in.readParcelable(SocialMedia.class.getClassLoader());
    }

    public static final Parcelable.Creator<Contributor> CREATOR = new Parcelable.Creator<Contributor>() {
        public Contributor createFromParcel(Parcel source) {
            return new Contributor(source);
        }

        public Contributor[] newArray(int size) {
            return new Contributor[size];
        }
    };
}
