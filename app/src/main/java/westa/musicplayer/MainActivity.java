package westa.musicplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<String> musicListItems = new ArrayList<>();
    private MusicListAdapter musicListAdapter;
    public static int nextSongId = 0;
    private HashMap<Integer, Path> musicPaths = new HashMap<>();
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
        addNewSongBtn = (Button) findViewById(R.id.add_new);
        removeAllSongsBtn = (Button) findViewById(R.id.remove_all);
        musicList = (ListView) findViewById(R.id.music_list);
        playSongBtn = (ImageButton) findViewById(R.id.play_song);
        previousSongBtn = (ImageButton) findViewById(R.id.previous);
        nextSongBtn = (ImageButton) findViewById(R.id.next);

        // Adapter listy piosenek
        musicListAdapter = new MusicListAdapter(this, R.layout.music_list_item, musicListItems);
        musicList.setAdapter(musicListAdapter);


        addNewSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // musicListAdapter.add("Test item " + nextSongId);
                // nextSongId += 1;

                Intent intent = new Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Wybierz piosenki do dodania"), 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedMusicFile = data.getData();
            System.out.println(data.getData());
        }
    }

    private void createMusicChooser() {
        Intent intent = new Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Wybierz piosenki do dodania"), 123);
    }

    private void addSong(String name, Path path) {
        musicListAdapter.add(name);
        musicPaths.put(nextSongId, path);
        nextSongId += 1;
    }
}