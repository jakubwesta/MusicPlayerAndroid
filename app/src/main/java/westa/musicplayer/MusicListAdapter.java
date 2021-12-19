package westa.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MusicListAdapter extends ArrayAdapter<String> {
    private final int layout;

    public MusicListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        layout = resource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mainViewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.music_list_item_title);
            viewHolder.playButton = (Button) convertView.findViewById(R.id.music_list_item_play);
            viewHolder.playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Test");
                }
            });
            convertView.setTag(viewHolder);

        } else {
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.title.setText(getItem(position));
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        Button playButton;
    }
}
