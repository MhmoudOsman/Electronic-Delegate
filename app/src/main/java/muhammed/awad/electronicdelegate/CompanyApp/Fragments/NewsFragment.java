package muhammed.awad.electronicdelegate.CompanyApp.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import muhammed.awad.electronicdelegate.Models.PostModel;
import muhammed.awad.electronicdelegate.R;

public class NewsFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<PostModel, PostsViewholder> firebaseRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        recyclerView = view.findViewById(R.id.recyclerview);
        floatingActionButton = view.findViewById(R.id.add_feed_fab);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMobileDialog();
                //Toast.makeText(getContext(), "fab", Toast.LENGTH_SHORT).show();
            }
        });

        getPosts();
    }

    private void getPosts() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Posts")
                .limitToLast(50);

        FirebaseRecyclerOptions<PostModel> options =
                new FirebaseRecyclerOptions.Builder<PostModel>()
                        .setQuery(query, PostModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostModel, PostsViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostsViewholder holder, int position, @NonNull final PostModel model) {

                String key = getRef(position).getKey();

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public PostsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.post_item, parent, false);
                return new PostsViewholder(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostsViewholder extends RecyclerView.ViewHolder {
        TextView post_content;

        PostsViewholder(View itemView) {
            super(itemView);

            post_content = itemView.findViewById(R.id.content_post);
        }

        void BindPlaces(final PostModel postModel) {
            post_content.setText(postModel.getContent());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    private void showMobileDialog() {
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.post_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText post_field = dialog.findViewById(R.id.post_field);

        Button post = dialog.findViewById(R.id.post_btn);
        Button cancel = dialog.findViewById(R.id.cancel_btn);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = post_field.getText().toString();

                if (TextUtils.isEmpty(post)) {
                    Toast.makeText(getContext(), "please enter text to share ..", Toast.LENGTH_SHORT).show();
                    return;
                }

                createPost(post);

                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void createPost(String post) {
        PostModel postModel = new PostModel(post);

        String key = databaseReference.child("Posts").push().getKey();
        databaseReference.child("Posts").child(key).setValue(postModel);
    }
}
