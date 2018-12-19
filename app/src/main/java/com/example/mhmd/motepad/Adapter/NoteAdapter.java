package com.example.mhmd.motepad.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mhmd.motepad.MessageEvent;
import com.example.mhmd.motepad.Model.Note;
import com.example.mhmd.motepad.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.noteViewHolder> {

    List<Note> notes;
    private Context context;
    private int lastPosition = -1;

    public NoteAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_veiw_item, null);
        return new noteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder noteViewHolder, int i) {

        final int position = i;
        Note note = notes.get(i);
        noteViewHolder.itemTitle.setText(note.getTitle());
        noteViewHolder.itemNote.setText(note.getNote());
        noteViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("openItem"
                        , position));

            }
        });

        noteViewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete " + "\"" + note.getTitle() + "\"")
                        .setNegativeButton("No", null)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post(new MessageEvent("deleteItem"
                                        , position));
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class noteViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public TextView itemNote;
        public LinearLayout linearLayout;

        public noteViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemNote = (TextView) itemView.findViewById(R.id.item_note);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.item_linear_layout);

        }


    }

}
