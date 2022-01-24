package westa.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MusicListAdapter extends ArrayAdapter<Song> {
    private final int layout;

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
            viewHolder.settingsBtn = convertView.findViewById(R.id.music_list_item_play);

            // Opcje piosenki
            viewHolder.settingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO Opcje piosenki
                }
            });

            // Włączenie piosenki
            viewHolder.titleTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO Włączenie piosenki
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
