package com.example.our_planner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.our_planner.model.Comment;
import com.example.our_planner.model.Event;
import com.example.our_planner.model.Group;
import com.example.our_planner.model.Invitation;
import com.example.our_planner.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public abstract class DataBaseAdapter {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static byte[] byteArray = new byte[]{};
    private static final List<GroupInterface> groupInterfaces = new ArrayList<>();
    private static EventInterface eventInterface;
    private static InvitationInterface invitationInterface;
    private static UriInterface uriInterface;
    private static final boolean registrationCompleted = true;
    private static ArrayList<Uri> uris;

    public static ArrayList<Uri> getUris() {
        return uris;
    }

    public static void login(DBInterface i, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user = mAuth.getCurrentUser();
                i.setToast(" " + getUserName());
            } else {
                i.setToast(task.getException().getMessage());
            }
        });
    }

    public static void register(DBInterface i, String email, String password, String username, Drawable drawableDefault) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user = mAuth.getCurrentUser();
                user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username).build()).addOnCompleteListener(task12 -> {

                    Bitmap bitmap = Bitmap.createBitmap(drawableDefault.getIntrinsicWidth(), drawableDefault.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    drawableDefault.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawableDefault.draw(canvas);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Task setProfilePictureTask = setProfilePicture(stream.toByteArray());
                    setProfilePictureTask.addOnCompleteListener(task1 -> {
                        i.setToast("-");
                    });
                });
            } else {
                i.setToast(task.getException().getMessage());
            }
        });
    }

    public static void subscribeGroupObserver(GroupInterface i) {
        groupInterfaces.add(i);
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
                for (GroupInterface i : groupInterfaces) {
                    i.updateGroups(groups);
                }
            }
        });
    }

    private static Map<String, Object> mapGroupDocument(String t, String d, Map<String, Integer> c, Map<String, User> p, Map<String, Boolean> a, ArrayList<Event> e) {
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
        g.put("events", e);
        return g;
    }

    public static void checkInvitations(Context c) {
        db.collection("invitations").get().addOnSuccessListener(task -> {
            String email = getEmail();
            for (DocumentSnapshot document : task.getDocuments()) {
                Map<String, Object> g = document.getData();
                if (g.get("user").equals(email)) {
                    //Send notification if you have just been invited
                    if (!(Boolean)g.get("notified")) {
                        g.replace("notified", true);
                        Notifier.sendNotification(c, g.get("author") + " invited you to " + g.get("title") + "!");
                        document.getReference().set(g);
                    }
                }
            }
        });
    }

    public static void subscribeInvitationObserver(InvitationInterface i) {
        invitationInterface = i;
        loadInvitations();
    }

    public static void loadInvitations() {
        db.collection("invitations").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Invitation> i = new ArrayList<>();
                String email = getEmail();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> g = document.getData();
                    if (g.get("user").equals(email)) {
                        i.add(new Invitation((String) g.get("group"), (String) g.get("title"), (String) g.get("author")));
                    }
                }
                invitationInterface.update(i);
            } else {
                invitationInterface.setToast(task.getException().getMessage());
            }
        });
    }

    private static void sendInvitation(String email, String groupId, String title) {
        String id = email + "-" + groupId;
        DocumentReference doc = db.collection("invitations").document(id);
        doc.get().addOnCompleteListener(t -> {
            if (t.isSuccessful()) {
                if (!t.getResult().exists()) {
                    //Send invitation if not invited yet
                    Map<String, Object> invitation = new HashMap<>();
                    invitation.put("user", email);
                    invitation.put("group", groupId);
                    invitation.put("author", getEmail());
                    invitation.put("title", title);
                    invitation.put("notified", false);
                    doc.set(invitation);
                }
            }
        });
    }

    private static void sendInvitations(List<String> invitationEmails, String groupId, String title) {
        for (String s : invitationEmails) {
            sendInvitation(s, groupId, title);
        }
    }

    public static void createGroup(DBInterface i, String title, String details, int colour, List<String> invitationEmails) {
        //The creator of the group is an admin by default
        String email = getEmail();
        Map<String, Integer> colours = new HashMap<>();
        colours.put(email, colour);
        Map<String, User> participants = new HashMap<>();
        participants.put(email, new User(getUserName()));
        Map<String, Boolean> admins = new HashMap<>();
        admins.put(email, true);
        ArrayList<Event> events = new ArrayList<>();
        db.collection("groups").add(mapGroupDocument(title, details, colours, participants, admins, events))
                .addOnSuccessListener(documentReference -> {
                    loadGroups();
                    sendInvitations(invitationEmails, documentReference.getId(), title);
                }).addOnFailureListener(e -> i.setToast(e.getMessage()));
    }

    private static void modifyInvitations(String groupId, String title) {
        db.collection("invitations").get().addOnSuccessListener(q -> {
            for (QueryDocumentSnapshot document : q) {
                Map<String, Object> g = document.getData();
                if (g.get("group").equals(groupId)) {
                    g.replace("title", title);
                    db.collection("invitations").document(g.get("user") + "-" + groupId).set(g);
                }
            }
        });
    }

    public static void deleteInvitation(Invitation i) {
        db.collection("invitations").document(getEmail() + "-" + i.getGroupId()).delete().addOnSuccessListener(documentReference -> {
            loadInvitations();
        });
    }

    public static void acceptInvitation(Invitation i) {
        String groupId = i.getGroupId();
        db.collection("groups").document(groupId).get().addOnSuccessListener(d -> {
            Map<String, Object> g = d.getData();
            String e = getEmail();
            //Add user to the group with no admin permission and with the colour black by default
            ((Map<String, String>) g.get("participants")).put(e, getUserName());
            ((Map<String, String>) g.get("colours")).put(e, String.valueOf(Color.BLACK));
            ((Map<String, Boolean>) g.get("admins")).put(e, false);
            db.collection("groups").document(groupId).set(g);

            //Finally, delete the invitation
            deleteInvitation(i);
        });
    }

    public static void editGroup(DBInterface i, String id, String title, String details, Map<String, Integer> colours, Map<String, User> participants, Map<String, Boolean> admins, List<String> invitationEmails, ArrayList<Event> events) {
        db.collection("groups").document(id).set(mapGroupDocument(title, details, colours, participants, admins, events))
                .addOnSuccessListener(documentReference -> {
                    loadGroups();
                    sendInvitations(invitationEmails, id, title);
                    modifyInvitations(id, title);
                }).addOnFailureListener(e -> i.setToast(e.getMessage()));
    }

    private static void deleteGroupInvitations(String id) {
        db.collection("invitations").get().addOnSuccessListener(q -> {
            for (QueryDocumentSnapshot document : q) {
                Map<String, Object> g = document.getData();
                if (g.get("group").equals(id)) {
                    db.collection("invitations").document(g.get("user") + "-" + id).delete();
                }
            }
        });
    }

    public static void leaveGroup(Group g) {
        String email = getEmail();
        Map<String, User> p = g.getParticipants();
        if (p.size() == 1) {
            db.collection("groups").document(g.getId()).delete().addOnSuccessListener(documentReference -> {
                loadGroups();
                deleteGroupInvitations(g.getId());
                deleteEvents(g.getId());
            });
        } else {
            p.remove(email);
            Map<String, Integer> c = g.getColours();
            c.remove(email);
            Map<String, Boolean> a = g.getAdmins();
            a.remove(email);
            ArrayList<Event> e = g.getEvents();
            Map<String, Object> group = mapGroupDocument(g.getTitle(), g.getDetails(), c, p, a, e);
            db.collection("groups").document(g.getId()).set(group).addOnSuccessListener(documentReference -> loadGroups());
        }
    }

    public static void checkRegistered(EmailCheckerInterface i, String email) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                i.setChecked(!task.getResult().getSignInMethods().isEmpty());
            } else {
                i.setToast(task.getException().getMessage());
            }
        });
    }

    public static String getUserName() {
        return user.getDisplayName();
    }

    public static String getEmail() {
        return user.getEmail();
    }

    public static byte[] getByteArray() {
        return byteArray;
    }

    public static boolean alreadyLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public static void logOut() {
        mAuth.signOut();
    }

    public static void postComment(String idEvent, String message) {
        rtdb.getReference().child("comments").child(idEvent).push().setValue(new Comment(message));
    }

    public static void forgotPassword(DBInterface i, String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    i.setToast("-");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException)
                    i.setToast("Email address is not valid");
                if (e instanceof FirebaseAuthInvalidUserException)
                    i.setToast("No user corresponding to this email address");
                else i.setToast("Failed to send reset email!\n" + e.getClass().getSimpleName());
            }
        });
    }

    public static void loadComments(CommentInterface i, String idEvent) {
        DatabaseReference ref = rtdb.getReference().child("comments").child(idEvent);
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

    public static void loadFiles(String eventId) {
        uris = new ArrayList<>();
        StorageReference storageRef = storage.getReference().child("Files").child(eventId);
        Task task = storageRef.listAll();
        task.addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference fileRef : listResult.getItems()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uris.add(uri);
                            uriInterface.updateUris(uris);
                        }
                    });
                }
            }
        });
    }

    public static void uploadFile(Uri uri, Context context, String eventId) {
        StorageReference storageRef = storage.getReference().child("Files").child(eventId).child(getFileName(uri, context));
        storageRef.putFile(uri);
        uris.add(uri);
        uriInterface.updateUris(uris);
    }

    @SuppressLint("Range")
    public static String getFileName(Uri uri, Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static Task<byte[]> updateProfilePicture() {
        Task<byte[]> byteArrayTask = storage.getReference().child(Objects.requireNonNull(user.getEmail())).getBytes(1024 * 1024);
        byteArrayTask.addOnSuccessListener(o -> {
            byteArray = byteArrayTask.getResult();
        });
        return byteArrayTask;
    }

    public static UploadTask setProfilePicture(byte[] byteArray) {
        StorageReference storageRef = storage.getReference().child(Objects.requireNonNull(user.getEmail()));
        DataBaseAdapter.byteArray = byteArray;
        return storageRef.putBytes(byteArray);
    }

    public static boolean isRegistrationCompleted() {
        return registrationCompleted;
    }

    public static void subscribeUriObserver(UriInterface i, String eventId) {
        uriInterface = i;
        loadFiles(eventId);
    }

    public static Task<byte[]> getImage(String author) {
        return storage.getReference().child(author).getBytes(1024 * 1024);
    }

    public interface DBInterface {
        void setToast(String s);
    }

    public interface EmailCheckerInterface extends DBInterface {
        void setChecked(boolean b);
    }

    public static void loadEvents() {
        db.collection("Events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Event> events = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> g = document.getData();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    events.add(new Event(document.getId(), (String) g.get("name"), (String) g.get("location"), LocalDate.parse(((String) g.get("date")),
                            formatter), LocalTime.parse((String) g.get("start time")), LocalTime.parse((String) g.get("end time")), (String) g.get("group")));
                }

                eventInterface.updateEvents(events);

            }
        });
    }

    public interface GroupInterface {
        void updateGroups(ArrayList<Group> groups);
    }

    public interface InvitationInterface extends DBInterface {
        void update(ArrayList<Invitation> invitations);
    }

    public static Group getGroup(String groupName) {
        AtomicReference<Group> group = new AtomicReference<>(null);
        db.collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> g = document.getData();
                    Map<String, String> participants = (HashMap<String, String>) g.get("participants");
                    if (participants.containsKey(getEmail())) {
                        if (g.get("title").equals(groupName)) {
                            Map<String, String> colours = (HashMap<String, String>) g.get("colours");
                            Map<String, Integer> coloursGroup = new HashMap<>();
                            Map<String, User> participantsGroup = new HashMap<>();
                            for (String k : colours.keySet()) {
                                coloursGroup.put(k, Integer.parseInt(colours.get(k)));
                                participantsGroup.put(k, new User(participants.get(k)));

                            }
                            group.set(new Group(document.getId(), (String) g.get("title"), (String) g.get("details"), coloursGroup, participantsGroup, (Map<String, Boolean>) g.get("admins")));
                            break;

                        }
                    }


                }
            }
        });

        return group.get();
    }

    public interface CommentInterface {
        void addComment(Comment comment);
    }

    public interface UriInterface {
        void updateUris(ArrayList<Uri> uris);
    }

    private static Map<String, Object> mapEventDocument(String name, String location, String date, String startTime, String endTime, String groupId) {
        Map<String, Object> g = new HashMap<>();
        g.put("name", name);
        g.put("location", location);
        g.put("date", date);
        g.put("start time", startTime);
        g.put("end time", endTime);
        g.put("group", groupId);
        return g;
    }

    public static void createEvent(String name, String location, String date, String startTime, String endTime, String groupId) {
        db.collection("Events").add(mapEventDocument(name, location, date, startTime, endTime, groupId))
                .addOnSuccessListener(documentReference -> {
                    loadEvents();
                });

    }

    public static void subscribeEventsObserver(EventInterface i) {
        eventInterface = i;
        loadEvents();
    }

    public interface EventInterface {
        void updateEvents(ArrayList<Event> events);
    }

    public static void editEvent(String eventId, String name, String location, String date, String startTime, String endTime, String groupId) {
        deleteEvent(eventId);
        createEvent(name, location, date, startTime, endTime, groupId);
        loadEvents();
    }

    public static void deleteEvent(String eventId) {
        DocumentReference doc = db.collection("Events").document(eventId);
        doc.delete().addOnSuccessListener(documentReference -> {
            loadEvents();
        });
    }

    public static ArrayList<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        db.collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
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


            }
        });

        return groups;
    }

    public static ArrayList<String> getGroupsNames() {
        ArrayList<String> groups = new ArrayList<>();
        db.collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> g = document.getData();
                    //  if (participants.containsKey(getEmail())) {
                    groups.add((String) g.get("title"));
                    //  }
                }


            }
        });

        return groups;
    }

    public static void deleteEvents(String groupId) {
        DocumentReference doc1;
        db.collection("Events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> g = document.getData();
                    if (((String) g.get("group")).equals(groupId)) {
                        String eventId = document.getId();
                        deleteEvent(eventId);

                    }

                }


            }
        });


    }

 /*   public static Group getGroup(String groupName) {
        AtomicReference<Group> group = new AtomicReference<>(null);
        db.collection("groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> g = document.getData();
                    Map<String, String> participants = (HashMap<String, String>) g.get("participants");
                    if (participants.containsKey(getEmail())) {
                        if (((String) g.get("title")).equals(groupName)) {
                            Map<String, String> colours = (HashMap<String, String>) g.get("colours");
                            Map<String, Integer> coloursGroup = new HashMap<>();
                            Map<String, User> participantsGroup = new HashMap<>();
                            for (String k : colours.keySet()) {
                                coloursGroup.put(k, Integer.parseInt(colours.get(k)));
                                participantsGroup.put(k, new User(participants.get(k)));

                            }
                            group.set(new Group(document.getId(), (String) g.get("title"), (String) g.get("details"), coloursGroup, participantsGroup, (Map<String, Boolean>) g.get("admins")));
                            break;

                        }
                    }


                }
            }
        });

        return group.get();
    } */
}
