package westa.musicplayer;

import android.media.MediaMetadataRetriever;
import android.net.Uri;

public class Song {
    private final Uri uri;
    private final String title;
    private final MediaMetadataRetriever metadataRetriever;

    public Song(Uri uri, String fileName, MainActivity mainActivity) {
        this.uri = uri;
        this.title = fileName.substring(0, fileName.lastIndexOf("."));

        this.metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(mainActivity, uri);
    }

    public String getMetadata(int metadataKey) {
        // MediaMetadataRetriever.KEY
        return metadataRetriever.extractMetadata((metadataKey));
    }

    public String getAuthor() {
        String author = getMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
        String artist = getMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (author != null) {
            return author;
        } else {
            if (artist != null) {
                return artist;
            }
        }
        return null;
    }

    public Uri getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }
}
