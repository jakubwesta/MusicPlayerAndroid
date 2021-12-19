package westa.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> musicListItems = new ArrayList<>();
    MusicListAdapter musicListAdapter;
    int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        view.setSystemUiVisibility(uiOption);

        Button btn = (Button) findViewById(R.id.btnPause);
        ListView listView = (ListView) findViewById(R.id.musicList);

        musicListAdapter = new MusicListAdapter(this, R.layout.music_list_item, musicListItems);
        listView.setAdapter(musicListAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicListAdapter.add("Test item " + counter);
                counter += 1;
            }
        });

    }
}