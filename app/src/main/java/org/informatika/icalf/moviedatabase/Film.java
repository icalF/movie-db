package org.informatika.icalf.moviedatabase;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by icalF on 5/5/2016.
 */
public class Film implements Parcelable {
  private int id;
  private String poster_url;
  private String overview;
  private int year;
  private String title;
  private float vote;
  private byte runtime;
  private String[] trailers_url;

  public Film(Film film) {
    film.trailers_url = trailers_url;
    film.id = id;
    film.poster_url = poster_url;
    film.overview = overview;
    film.year = year;
    film.title = title;
    film.vote = vote;
    film.runtime = runtime;
  }

  public Film(String[] trailers_url, int id, String poster_url, String overview, int year, String title, float vote, byte runtime) {
    this.trailers_url = trailers_url;
    this.id = id;
    this.poster_url = poster_url;
    this.overview = overview;
    this.year = year;
    this.title = title;
    this.vote = vote;
    this.runtime = runtime;
  }

  protected Film(Parcel in) {
    this.trailers_url = (String[]) in.readArray(String.class.getClassLoader());
    this.id = in.readInt();
    this.poster_url = in.readString();
    this.overview = in.readString();
    this.year = in.readInt();
    this.title = in.readString();
    this.vote = in.readFloat();
    this.runtime = in.readByte();
  }

  public static final Creator<Film> CREATOR = new Creator<Film>() {
    @Override
    public Film createFromParcel(Parcel in) {
      return new Film(in);
    }

    @Override
    public Film[] newArray(int size) {
      return new Film[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeArray(this.trailers_url);
    dest.writeInt(this.id);
    dest.writeString(this.poster_url);
    dest.writeString(this.overview);
    dest.writeInt(this.year);
    dest.writeString(this.title);
    dest.writeFloat(this.vote);
    dest.writeByte(this.runtime);
  }
}
