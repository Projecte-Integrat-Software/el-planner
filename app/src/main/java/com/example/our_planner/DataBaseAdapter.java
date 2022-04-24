package com.example.our_planner;

import com.example.our_planner.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DataBaseAdapter {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    public interface DBInterface {
        void setToast(String s);
    }

    public static boolean alreadyLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public static void logOut() {
        mAuth.signOut();
    }

    public interface GroupInterface extends DBInterface {
        void setGroup(Group g);
    }
}
