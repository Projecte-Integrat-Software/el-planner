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

import java.util.HashMap;
import java.util.Map;

public abstract class DataBaseAdapter {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseDatabase rtdb = FirebaseDatabase.getInstance();

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

    public static void createGroup(GroupInterface i, String title, String details, int colour) {
        Map<String, Object> g = new HashMap<>();
        g.put("title", title);
        g.put("details", details);
        g.put("colour", colour);
        db.collection("groups").add(g).addOnSuccessListener(documentReference -> {
            i.setToast("Group created successfully");
            i.setGroup(new Group(documentReference.getId(), title, details, colour));
        }).addOnFailureListener(e -> i.setToast(e.getMessage()));
    }

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
        void setGroup(Group g);
    }

    public interface CommentInterface {
        void addComment(Comment comment);
    }
}
