package com.example.our_planner;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.our_planner.model.Comment;
import com.example.our_planner.model.Group;
import com.example.our_planner.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class DataBaseAdapter {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static byte[] byteArray = new byte[]{};
    private static GroupInterface groupInterface;

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

    public static void forgotPassword(DBInterface i, String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    i.setToast("We have sent you instructions to reset your password!");
                } else {
                    i.setToast("Failed to send reset email!");
                }
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
                    Map<String, String> participants = (HashMap<String, String>) g.get("participants");
                    if (participants.containsKey(getEmail())) {
                        Map<String, String> colours = (HashMap<String, String>) g.get("colours");
                        Map<String, Integer> coloursGroup = new HashMap<>();
                        Map<String, User> participantsGroup = new HashMap<>();
                        for (String k : colours.keySet()) {
                            coloursGroup.put(k, Integer.parseInt(colours.get(k)));
                            participantsGroup.put(k, new User(participants.get(k)));
                        }
                        groups.add(new Group(document.getId(), (String) g.get("title"), (String) g.get("details"), coloursGroup, participantsGroup, (Map<String, Boolean>) g.get("admins")));
                    }
                }
                groupInterface.update(groups);
            } else {
                groupInterface.setToast(task.getException().getMessage());
            }
        });
    }

    private static Map<String, Object> mapGroupDocument(String t, String d, Map<String, Integer> c, Map<String, User> p, Map<String, Boolean> a) {
        Map<String, Object> g = new HashMap<>();
        g.put("title", t);
        g.put("details", d);
        Map<String, String> colours = new HashMap<>();
        Map<String, String> participants = new HashMap<>();
        for (String k : c.keySet()) {
            colours.put(k, String.valueOf(c.get(k)));
            participants.put(k, p.get(k).getUsername());
        }
        g.put("colours", colours);
        g.put("participants", participants);
        g.put("admins", a);
        return g;
    }

    public static void createGroup(DBInterface i, String title, String details, int colour) {
        //The creator of the group is an admin by default
        String email = getEmail();
        Map<String, Integer> colours = new HashMap<>();
        colours.put(email, colour);
        Map<String, User> participants = new HashMap<>();
        participants.put(email, new User(getUserName()));
        Map<String, Boolean> admins = new HashMap<>();
        admins.put(email, true);
        db.collection("groups").add(mapGroupDocument(title, details, colours, participants, admins)).addOnSuccessListener(documentReference -> {
            i.setToast("Group created successfully");
            loadGroups();
        }).addOnFailureListener(e -> i.setToast(e.getMessage()));
    }

    public static void editGroup(DBInterface i, String id, String title, String details, Map<String, Integer> colours, Map<String, User> participants, Map<String, Boolean> admins) {
        db.collection("groups").document(id).set(mapGroupDocument(title, details, colours, participants, admins)).addOnSuccessListener(documentReference -> {
            i.setToast("Group edited successfully");
            loadGroups();
        }).addOnFailureListener(e -> i.setToast(e.getMessage()));
    }

    public static void leaveGroup(Group g) {
        String email = getEmail();
        Map<String, User> p = g.getParticipants();
        if (p.size() == 1) {
            db.collection("groups").document(g.getId()).delete().addOnSuccessListener(documentReference -> {
                groupInterface.setToast("Group left and deleted since there are no participants left");
                loadGroups();
            });
        } else {
            p.remove(email);
            Map<String, Integer> c = g.getColours();
            c.remove(email);
            Map<String, Boolean> a = g.getAdmins();
            a.remove(email);
            Map<String, Object> group = mapGroupDocument(g.getTitle(), g.getDetails(), c, p, a);
            db.collection("groups").document(g.getId()).set(group).addOnSuccessListener(documentReference -> {
                groupInterface.setToast("Group edited successfully");
                loadGroups();
            }).addOnFailureListener(e -> groupInterface.setToast(e.getMessage()));
        }
    }

    /*
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
        }
    */
    public static String getUserName() {
        return user.getDisplayName();
    }

    public static String getEmail() {
        return user.getEmail();
    }

    public static byte[] getByteArray() {
        return byteArray;
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

    public static Task<byte[]> updateProfilePicture(Drawable drawableDefault) {
        Task<byte[]> byteArrayTask = storage.getReference().child(user.getUid()).getBytes(1024 * 1024);
        byteArrayTask.addOnSuccessListener(o -> {
            byteArray = (byte[]) byteArrayTask.getResult();
        });
        byteArrayTask.addOnFailureListener(e -> {
            Bitmap bitmap = Bitmap.createBitmap(drawableDefault.getIntrinsicWidth(), drawableDefault.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawableDefault.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawableDefault.draw(canvas);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        });
        return byteArrayTask;
    }

    public static UploadTask setProfilePicture(byte[] byteArray) {
        StorageReference storageRef = storage.getReference().child(user.getUid());
        return storageRef.putBytes(byteArray);
    }

    public interface GroupInterface extends DBInterface {
        void update(ArrayList<Group> groups);
    }

    public interface CommentInterface {
        void addComment(Comment comment);
    }
}
