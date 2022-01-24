package westa.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MusicListAdapter extends ArrayAdapter<Song> {
    private final int layout;
    private View activeView;

    public MusicListAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        layout = resource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mainViewHolder = null;
        Song song = getItem(position);

        if (convertView == null) {
            // Sworzenie widoku z xml
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.titleTxt = convertView.findViewById(R.id.music_list_item_title);
            viewHolder.settingsBtn = convertView.findViewById(R.id.music_list_item_details);

            // Opcje piosenki
            viewHolder.settingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(getContext(), viewHolder.settingsBtn);
                    popupMenu.getMenuInflater().inflate(R.menu.song_destails_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.delete_song:
                                    MainActivity.musicListItems.remove(position);
                                    return true;
                                case R.id.see_dong_details:
                                    // TODO
                                    return true;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });

            // Włączenie piosenki
            viewHolder.titleTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.isPlaying = true;
                    MainActivity.isPaused = false;
                    if (activeView != null) {
                        activeView.setSelected(false);
                    }
                    activeView = view;
                    view.setSelected(true);
                    MainActivity.currentSongId = position;
                    MainActivity.playsong();
                }
            });

            viewHolder.titleTxt.setText(song.getTitle());

            /* Podmiana ustawionych .onClickListenerów() do convertView żeby efekt
            kliknięcia na każdym elemencie listy był różny w zależnośći od tego na jakim się kliknęłó */
            convertView.setTag(viewHolder);
        }

        return convertView;
    }

    // Elementy xmla
    private static class ViewHolder {
        TextView titleTxt;
        ImageButton settingsBtn;
    }
}
