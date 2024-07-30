package com.example.genius;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.genius.Model.Model;
import com.example.genius.Model.User;
import com.google.android.material.color.utilities.Score;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreBoardFragment extends Fragment {
    View view;

    ScoreBoardFragmentViewModel viewModel;
    User u;
    MyAdapter adapter;
    ProgressBar ScoreBoard_progressBar;
    SwipeRefreshLayout ScoreBoard_SwipeRefresh;

    ImageButton my_group_btn;
    ImageButton all_group_btn;

    private boolean showAllUsers = false; // Flag to indicate whether to show all users or only group users

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(ScoreBoardFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_score_board, container, false);
        u = ScoreBoardFragmentArgs.fromBundle(getArguments()).getUser();
        my_group_btn = view.findViewById(R.id.mygroup_btn);
        all_group_btn = view.findViewById(R.id.allgroup_btn);
        ScoreBoard_progressBar = view.findViewById(R.id.scoreboard_progressBar);
        ScoreBoard_progressBar.setVisibility(View.VISIBLE);
        RecyclerView list = view.findViewById(R.id.scoreboardfragment_listv);
        list.setHasFixedSize(true);
        adapter = new MyAdapter();
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(LayoutManager);
        DividerItemDecoration DividerList = new DividerItemDecoration(list.getContext(), LayoutManager.getOrientation());
        DividerList.setDrawable(getResources().getDrawable(R.drawable.divider));
        list.addItemDecoration(DividerList);
        list.setAdapter(adapter);
        ScoreBoard_SwipeRefresh = view.findViewById(R.id.scoreboard_SwipeRefresh);
        ScoreBoard_SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.reloadUserList();
                ScoreBoard_SwipeRefresh.setRefreshing(false);
            }
        });

        my_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllUsers = false; // Show only group users
                my_group_btn.setSelected(true);
                all_group_btn.setSelected(false);
                updateUsers(viewModel.getData().getValue());
            }
        });

        all_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllUsers = true; // Show all users
                my_group_btn.setSelected(false);
                all_group_btn.setSelected(true);
                updateUsers(viewModel.getData().getValue()); // Update the list with the appropriate filter
            }
        });

        if (viewModel.getData().getValue() == null) {
            Model.instance.reloadUserList();
            ScoreBoard_SwipeRefresh.setRefreshing(false);
        }
        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                updateUsers(users); // Update the list with the appropriate filter
                ScoreBoard_SwipeRefresh.setRefreshing(false);
                ScoreBoard_progressBar.setVisibility(View.GONE);
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    private void updateUsers(List<User> users) {
        if (users != null) {
            List<User> filteredUsers;
            if (showAllUsers) {
                filteredUsers = users; // Show all users
            } else {
                filteredUsers = users.stream()
                        .filter(user -> user.getGroup().equals(u.getGroup()))
                        .collect(Collectors.toList()); // Show only users from the same group
            }
            adapter.setUsers(filteredUsers); // Update the adapter with filtered users
            adapter.notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phone;
        TextView score;
        ImageView avatarImg;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.userlistrow_text_v1);
            phone = itemView.findViewById(R.id.userlistrow_text_v2);
            score = itemView.findViewById(R.id.userlistrow_text_v3);
            avatarImg = itemView.findViewById(R.id.userlistrow_imagev);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(pos, v);
                    }
                }
            });
        }

        public void bind(User user) {
            name.setText(user.getName());
            phone.setText(user.getPhone());
            score.setText(user.getScore());
            avatarImg.setImageResource(R.drawable.avatar);
            if (user.getAvatarUrl() != null) {
                avatarImg.postDelayed(() -> {
                    Picasso.get()
                            .load(user.getAvatarUrl())
                            .noPlaceholder() // No placeholder
                            .error(R.drawable.avatar) // Optional: show the default avatar if the image fails to load
                            .into(avatarImg, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    avatarImg.setVisibility(View.VISIBLE); // Make ImageView visible after successful load
                                    avatarImg.setAlpha(0f);
                                    avatarImg.animate().setDuration(300).alpha(1f).start(); // Fade in effect
                                }

                                @Override
                                public void onError(Exception e) {
                                    avatarImg.setVisibility(View.VISIBLE); // Make ImageView visible even if there's an error
                                }
                            });
                }, 100); // Slight delay before starting image load
            } else {
                avatarImg.setVisibility(View.VISIBLE); // Make ImageView visible if there's no URL
                avatarImg.setImageResource(R.drawable.avatar);
            }
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        OnItemClickListener listener;
        private List<User> users = new ArrayList<>(); // List to hold filtered users

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public void setUsers(List<User> users) {
            this.users = users; // Update the user list
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.users_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(v, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            User u2 = users.get(position);
            holder.bind(u2);
        }

        @Override
        public int getItemCount() {
            return users.size(); // Return the size of the filtered user list
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.gethomepage_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.home_menu) {
            Navigation.findNavController(view).popBackStack();
        }
        return true;
    }
}
