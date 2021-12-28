package westa.musicplayer;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<Song> musicListItems = new ArrayList<>();
    private MusicListAdapter musicListAdapter;
    private final int FILE_CHOOSER_ACTIVITY_ID = 1;
    private int currentSongId;
    private boolean isPlaying = false;
    Button addNewSongBtn;
    Button removeAllSongsBtn;
    ListView musicList;
    ImageButton playSongBtn;
    ImageButton previousSongBtn;
    ImageButton nextSongBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fullscreen
        View view = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOption);

        // Elementy UI
        addNewSongBtn = findViewById(R.id.add_new);
        removeAllSongsBtn = findViewById(R.id.remove_all);
        musicList = findViewById(R.id.music_list);
        playSongBtn = findViewById(R.id.play_song);
        previousSongBtn = findViewById(R.id.previous);
        nextSongBtn = findViewById(R.id.next);

        // Adapter listy piosenek
        musicListAdapter = new MusicListAdapter(this, R.layout.music_list_item, musicListItems);
        musicList.setAdapter(musicListAdapter);

        // Otwieranie menu wyboru plików
        addNewSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent()
                        .setType("*/*") // "audio/*"
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
                playSongBtn.setImageResource(R.drawable.play);
            }
        });

        // Granie piosenki
        playSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    isPlaying = false;
                    playSongBtn.setImageResource(R.drawable.pause);
                } else {
                    isPlaying = true;
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_ACTIVITY_ID && resultCode == RESULT_OK) { // Dodawanie wybranych plików do aplikacji
            if (data.getData() != null) { // Pojedyńczy plik
                Uri fileUri = data.getData();
                addSongFile(new Song(fileUri));

            } else if (data.getClipData() != null) { // Wiele plików
                ClipData fileUris = data.getClipData();
                for (int i = 0; i < fileUris.getItemCount(); i++) {
                    ClipData.Item itemUri = fileUris.getItemAt(i);
                    addSongFile(new Song(itemUri.getUri()));
                }
            }
        }
    }

    private void addSongFile(Song song) {
        System.out.println(song.getTitle());
        System.out.println(song.getUri());
        musicListAdapter.add(song);
    }

    private void playSong() {

    }
}