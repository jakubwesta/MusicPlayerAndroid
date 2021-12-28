package westa.musicplayer;

import android.net.Uri;

import java.io.File;

public class Song {
    private final Uri uri;
    private final String title;

    public Song(Uri uri) {
        this.uri = uri;
        this.title = new File(uri.getPath()).getName();
    }

    public Uri getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }
}
