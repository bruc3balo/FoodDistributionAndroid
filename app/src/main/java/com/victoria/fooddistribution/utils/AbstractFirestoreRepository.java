package com.victoria.fooddistribution.utils;

import static com.victoria.fooddistribution.globals.GlobalRepository.firebaseFirestore;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.victoria.fooddistribution.globals.GlobalRepository;


import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractFirestoreRepository<T> {
    private final CollectionReference collectionReference;
    private final String collectionName;
    private final Class<T> parameterizedType;
    private final Logger logger;


    protected AbstractFirestoreRepository(String collection) {
        this.collectionReference = GlobalRepository.firebaseFirestore.collection(collection);
        this.collectionName = collection;
        this.parameterizedType = getParameterizedType();
        this.logger = Logger.getLogger(collection);
    }

    private Class<T> getParameterizedType() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) Objects.requireNonNull(type).getActualTypeArguments()[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public T save(T model) {
        String documentId = getDocumentId(model);
        Task<Void> resultApiFuture = collectionReference.document(documentId).set(model);

        if (resultApiFuture.isSuccessful()) {
            logger.info(collectionName + "saved" + documentId);
            return model;

        } else {
            logger.info(collectionName + "failed to saved" + documentId);
            return null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean delete(T model) {
        final boolean[] deleted = {false};
        try {
            String documentId = getDocumentId(model);
            Task<Void> resultApiFuture = collectionReference.document(documentId).delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    logger.info("Success deleting " + collectionName + model);
                    deleted[0] = true;
                } else {
                    logger.info("Failed deleting " + collectionName + model);
                    deleted[0] = false;
                }
            });
        } catch (Exception e) {
            logger.info("Error deleting " + collectionName + model + e.getMessage());
        }
        return deleted[0];
    }

    public boolean remove(String id) {
        boolean deleted = false;
        try {
            Task<Void> resultApiFuture = collectionReference.document(id).delete();
            deleted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<T> retrieveAll() {

        final List<T> collection = new ArrayList<T>();

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot qs = task.getResult();
                    if (qs != null) {
                        if (!qs.isEmpty()) {
                            collection.addAll(qs.getDocuments().stream().map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(parameterizedType)).collect(Collectors.toList()));
                        } else {
                            logger.info("Empty list");
                        }
                    } else {
                        logger.info("No data");
                    }

                } else {
                    logger.info("Failed to get data "+ Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });


        return collection;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Optional<T> get(String documentId) {
        DocumentReference documentReference = collectionReference.document(documentId);
        final Optional<T>[] object = new Optional[]{Optional.empty()};

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        object[0] = Optional.ofNullable(documentSnapshot.toObject(parameterizedType));
                    } else {
                        logger.info("Error " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                } else {
                    object[0] = Optional.empty();
                    logger.info("Error " + Objects.requireNonNull(task.getException()).getMessage());
                }
            } else {
                object[0] = Optional.empty();
                logger.info("Error " + Objects.requireNonNull(task.getException()).getMessage());

            }
        });


        return object[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected String getDocumentId(T t) {
        Object key;
        Class clzz = t.getClass();
        do {
            key = getKeyFromFields(clzz, t);
            clzz = clzz.getSuperclass();
        } while (key == null && clzz != null);

        if (key == null) {
            return UUID.randomUUID().toString();
        }
        return String.valueOf(key);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Object getKeyFromFields(Class<?> clazz, Object t) {

        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DocumentId.class))
                .findFirst()
                .map(field -> getValue(t, field))
                .orElse(null);
    }

    @Nullable
    private Object getValue(Object t, java.lang.reflect.Field field) {
        field.setAccessible(true);
        try {
            return field.get(t);
        } catch (IllegalAccessException e) {
            logger.info("Error in getting documentId key" + e);
            logger.info("ERROR " + e.getMessage());

        }
        return null;
    }

    protected CollectionReference getCollectionReference() {
        return this.collectionReference;
    }

    protected Class<T> getType() {
        return this.parameterizedType;
    }
}