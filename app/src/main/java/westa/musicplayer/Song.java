package westa.musicplayer;

import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.File;

public class Song {
    private final Uri uri;
    private final File file;
    private final String title;
    private final Long size; // W bajtach
    private final String MIMEtype;
    private final MediaMetadataRetriever metadataRetriever;

    public Song(Uri uri, String fileName, Long fileSize, String MIMEtype, MainActivity mainActivity) {
        this.uri = uri;
        this.file = new File(uri.getPath());
        this.title = fileName.substring(0, fileName.lastIndexOf("."));
        this.size = fileSize;
        this.MIMEtype = MIMEtype;

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
            } else {
                return "Nie ma danych";
            }
        }
    }

    public int getDuration() {
        return Integer.parseInt(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    public Uri getUri() {
        return uri;
    }

    public File getFile() {
        return file;
    }

    public String getTitle() {
        return title;
    }

    public Long getSize() {
        return size;
    }

    public String getMIMEtype() {
        return MIMEtype;
    }
}
