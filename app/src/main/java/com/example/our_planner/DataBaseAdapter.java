package com.example.our_planner;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.our_planner.model.Comment;
import com.example.our_planner.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class DataBaseAdapter {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private static GroupInterface groupInterface;

    public static FirebaseUser getUser() {
        return user;
    }

    public static void login(DBInterface i, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user = mAuth.getCurrentUser();
                i.setToast("Logged as " + user.getDisplayName());
            } else {
                i.setToast(task.getException().getMessage());
            }
        });
    }

    public static void register(DBInterface i, String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user = mAuth.getCurrentUser();
                user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username).build());
                i.setToast("Registered successfully");
            } else {
                i.setToast(task.getException().getMessage());
            }
        });
    }

    public static void subscribeGroupObserver(GroupInterface i) {
        groupInterface = i;
        loadGroups();
    }

    public static void loadGroups() {
        db.collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Group> groups = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> g = document.getData();
                    groups.add(new Group(document.getId(), (String) g.get("title"), (String) g.get("details"), Integer.parseInt((String) g.get("colour"))));
                }
                groupInterface.update(groups);
            } else {
                groupInterface.setToast(task.getException().getMessage());
            }
        });
    }

    public static void createGroup(DBInterface i, String title, String details, int colour) {
        Map<String, Object> g = new HashMap<>();
        g.put("title", title);
        g.put("details", details);
        g.put("colour", String.valueOf(colour));
        db.collection("groups").add(g).addOnSuccessListener(documentReference -> {
            i.setToast("Group created successfully");
            loadGroups();
        }).addOnFailureListener(e -> i.setToast(e.getMessage()));
    }

    public static void editGroup(DBInterface i, String id, String title, String details, int colour) {
        Map<String, Object> g = new HashMap<>();
        g.put("title", title);
        g.put("details", details);
        g.put("colour", String.valueOf(colour));
        db.collection("groups").document(id).set(g).addOnSuccessListener(documentReference -> {
            i.setToast("Group edited successfully");
            loadGroups();
        }).addOnFailureListener(e -> i.setToast(e.getMessage()));
    }

    public static void leaveGroup(Group g) {
        db.collection("groups").document(g.getId()).delete().addOnSuccessListener(documentReference -> {
            groupInterface.setToast("Group left");
            loadGroups();
        });
    }
/*
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            if (remoteMessage.getNotification() != null) {
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
        }

        private void showNotification(String title, String message) {
            // Assign channel ID
            String channel_id = "notification_channel";

            // Create a Builder object using NotificationCompat
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                    .setSmallIcon(R.drawable.ic_menu_invitations).setAutoCancel(true).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setOnlyAlertOnce(true);
            builder = builder.setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.ic_menu_invitations);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // Check if the Android Version is greater than Oreo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH));
            }

            notificationManager.notify(0, builder.build());
        }

        private boolean userExists(String email) {
            final boolean[] b = new boolean[1];
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> b[0] = !task.getResult().getSignInMethods().isEmpty());
            return b[0];
        }


        public void inviteUser(DBInterface i, String email, String title) {
            if (userExists(email)) {
                Message m = new Message.Builder
                        .setBody(getUserName() + " has invited you to the group " + title).build())
                        .setTopic(email).build();
                mes.send(m);
            } else {
                i.setToast("There is no user registered with this email!");
            }
        }*/

    public static String getUserName() {
        return user.getDisplayName();
    }

    public interface DBInterface {
        void setToast(String s);
    }

    public static boolean alreadyLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public static void logOut() {
        mAuth.signOut();
    }

    public static void postComment(String message){
        rtdb.getReference().child("comments").push().setValue(new Comment(message));
    }

    public static void loadComments(CommentInterface i){
        DatabaseReference ref = rtdb.getReference().child("comments");
        ref.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comment comment = snapshot.getValue(Comment.class);
                i.addComment(comment);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public interface GroupInterface extends DBInterface {
        void update(ArrayList<Group> groups);
    }

    public interface CommentInterface {
        void addComment(Comment comment);
    }
}
