package ru.kpfu.itis.rodsher.bookman.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import ru.kpfu.itis.rodsher.bookman.models.User;

import java.util.*;

public class UsersDriverRepositoryImpl implements UsersDriverOrTemplateRepository {
    private static final String DATABASE_NAME = "bookmans";
    private static final String COLLECTION_NAME = "users";

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> collection;
    private ObjectMapper objectMapper;

    public UsersDriverRepositoryImpl(String address, int port) {
        mongoClient = MongoClients.create(
                MongoClientSettings.builder().applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(address, port))))
                        .build());
        mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);
        collection = mongoDatabase.getCollection(COLLECTION_NAME);
        objectMapper = new ObjectMapper();
    }

    public UsersDriverRepositoryImpl() {
        this("localhost", 27017);
    }

    @Override
    public Optional<User> find(String id) {
        Document document = collection.find(new BasicDBObject("_id", new ObjectId(id))).first();
        if(document == null)
            return Optional.empty();
        try {
            return Optional.of(objectMapper.readValue(document.toJson(), User.class));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse user row with id=" + id + ".", e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        FindIterable<Document> documents = collection.find();
        for(Document document : documents) {
            try {
                System.out.println("####### " + document.toJson());
                users.add(objectMapper.readValue(document.toJson(), User.class));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Failed to parse user row with id="
                        + document.getObjectId("_id").toString() + ".", e);
            }
        }
        return users;
    }

    @Override
    public void save(User user) {
        try {
            Document document = Document.parse(objectMapper.writeValueAsString(user));
            if(document.getObjectId("_id") == null) {
                document.put("_id", new ObjectId());
            }
            collection.insertOne(document);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse user.", e);
        }
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(new BasicDBObject("_id", new ObjectId(id)));
    }

    @Override
    public void update(User user) {
        try {
            Document document = Document.parse(objectMapper.writeValueAsString(user));
            if(document.getObjectId("_id") != null) {
                collection.findOneAndReplace(new BasicDBObject("_id", document.getObjectId("_id")), document);
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse user.", e);
        }
    }
}
