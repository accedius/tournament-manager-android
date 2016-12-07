package fit.cvut.org.cz.tmlibrary.business.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kevin on 7.10.2016.
 */
public class ServerCommunicationItem {
    public Long id;
    public String uid;
    public String etag;
    public ServerToken token;

    public String type;
    public Date created;
    public Date modified;

    public String presenter;
    public HashMap<String, Object> syncData;
    public List<ServerCommunicationItem> subItems = new ArrayList<>();

    public transient boolean serializeToken;
    public String toJson() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }

    public ServerCommunicationItem() {
        id = 0L;
    }

    public ServerCommunicationItem(String uid, String etag, ServerToken token) {
        this.uid = uid;
        this.etag = etag;
        this.token = token;
    }

    public ServerCommunicationItem(String uid, String etag, ServerToken token, String type, String presenter ) {
        this.uid = uid;
        this.etag = etag;
        this.token = token;
        this.type = type;
        this.presenter = presenter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public ServerToken getToken() {
        return token;
    }

    public void setToken(ServerToken token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public HashMap<String, Object> getSyncData() {
        return syncData;
    }

    public void setSyncData(HashMap<String, Object> syncData) {
        this.syncData = syncData;
    }

    public List<ServerCommunicationItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<ServerCommunicationItem> subItems) {
        this.subItems = subItems;
    }

    public boolean isSerializeToken() {
        return serializeToken;
    }

    public void setSerializeToken(boolean serializeToken) {
        this.serializeToken = serializeToken;
    }
}
