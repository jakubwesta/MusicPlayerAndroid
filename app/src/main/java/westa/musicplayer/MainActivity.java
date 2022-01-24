package westa.musicplayer;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<Song> musicListItems = new ArrayList<>();
    private MusicListAdapter musicListAdapter;
    private final int FILE_CHOOSER_ACTIVITY_ID = 1;
    private int currentSongId = 0;
    private boolean isPlaying = false;
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private AudioManager audioManager;
    Button addNewSongBtn;
    Button removeAllSongsBtn;
    ListView musicList;
    ImageButton playSongBtn;
    ImageButton previousSongBtn;
    ImageButton nextSongBtn;
    SeekBar volumeBar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fullscreen
        View view = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOption);

        // Elementy UI
        addNewSongBtn = findViewById(R.id.add_new);
        removeAllSongsBtn = findViewById(R.id.remove_all);
        musicList = findViewById(R.id.music_list);
        playSongBtn = findViewById(R.id.play_song);
        previousSongBtn = findViewById(R.id.previous);
        nextSongBtn = findViewById(R.id.next);
        volumeBar = findViewById(R.id.volume_bar);
        progressBar = findViewById(R.id.progress_bar);

        // Adapter listy piosenek
        musicListAdapter = new MusicListAdapter(this, R.layout.music_list_item, musicListItems);
        musicList.setAdapter(musicListAdapter);

        // Otwieranie menu wyboru plików
        addNewSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent()
                        .setType("audio/*") // "audio/*" lub "*/*"
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Wybierz piosenki do dodania"), FILE_CHOOSER_ACTIVITY_ID);
            }
        });

        // Czyszczenie listy piosenek
        removeAllSongsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO PopUp potwierdzjący
                musicListItems.clear();
                musicListAdapter.notifyDataSetChanged();
                currentSongId = 0;
                isPlaying = false;
                stopSong();
                playSongBtn.setImageResource(R.drawable.play);
            }
        });

        // Granie piosenki
        playSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    isPlaying = false;
                    stopSong();
                    playSongBtn.setImageResource(R.drawable.pause);
                } else {
                    isPlaying = true;
                    playSong();
                    playSongBtn.setImageResource(R.drawable.play);
                }
            }
        });

        // Kolejna piosenka
        nextSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicListItems.size() - 1 != currentSongId) {
                    currentSongId += 1;
                } else {
                    currentSongId = 0;
                }
            }
        });

        // Poprzenia piosenka
        previousSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSongId != 0) {
                    currentSongId -= 1;
                } else {
                    currentSongId = musicListItems.size() - 1;
                }
            }
        });

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        // Zmiany dźwięku SeekBarem
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Rejestrowanie zmian dźwięku przez przyciski na telefonie
        getApplicationContext().getContentResolver()
                .registerContentObserver(Settings.System.CONTENT_URI,
                        true,
                        new AudioContentObserver(new Handler()));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_ACTIVITY_ID && resultCode == RESULT_OK) { // Dodawanie wybranych plików do aplikacji
            if (data.getData() != null) { // Pojedyńczy plik
                Uri fileUri = data.getData();
                addSongFile(fileUri);

            } else if (data.getClipData() != null) { // Wiele plików
                ClipData fileUris = data.getClipData();
                for (int i = 0; i < fileUris.getItemCount(); i++) {
                    ClipData.Item itemUri = fileUris.getItemAt(i);
                    addSongFile(itemUri.getUri());
                }
            }
        }
    }

    private void addSongFile(Uri uri) {
        // Używanie Cursora żeby dostać nazwę i rozmiar pliku pod uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
        cursor.moveToFirst();

        String fileName = cursor.getString(nameIndex);
        Long fileSize = cursor.getLong(sizeIndex);
        String MIMEtype = getContentResolver().getType(uri);
        Song song = new Song(uri, fileName, fileSize, MIMEtype, this);

        System.out.println(fileSize);
        System.out.println(song.getMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        System.out.println(song.getTitle());
        System.out.println(song.getMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
        System.out.println(song.getMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        System.out.println(song.getMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        musicListAdapter.add(song);
    }

    private void playSong() {
        System.out.println(musicListItems.get(currentSongId).getUri());
        try {
            mediaPlayer.setDataSource(MainActivity.this ,musicListItems.get(currentSongId).getUri());
            mediaPlayer.prepare();
            progressBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            musicList.setSelection(currentSongId);
            startCountdown(3000);
            //progressBar.postDelayed(updateProgressBar, 15); // 1000/60 (fps) = 15ms
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopSong() {
        mediaPlayer.stop();
    }

    private Song getCurrentSong() {
        return musicListItems.get(currentSongId);
    }

    // Wykrywanie amiany głośności urządzenia
    class AudioContentObserver extends ContentObserver {
        public AudioContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
    }

    private final Runnable updateProgressBar = new Runnable() {
        @Override
        public void run() {
            long currentDuration = mediaPlayer.getCurrentPosition();
            progressBar.setProgress((int) currentDuration);
            progressBar.postDelayed(this, 15); // 1000/60 (fps) = 15ms
        }
    };

    private void startCountdown(long coundownTime) {
        CountDownTimer coundownTimer = new CountDownTimer(coundownTime, 10) {
            @Override
            public void onTick(long l) {
                long currentDuration = mediaPlayer.getCurrentPosition();
                progressBar.setProgress((int) currentDuration);
            }

            @Override
            public void onFinish() {

            }
        };
        coundownTimer.start();
    }
}