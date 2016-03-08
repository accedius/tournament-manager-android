package fit.cvut.org.cz.tmlibrary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vaclav on 8. 3. 2016.
 */
public class ConceptEntity implements Parcelable {

    String name;
    long id;
    InnerEntity entity;

    public ConceptEntity(String name, long id, InnerEntity entity) {
        this.name = name;
        this.id = id;
        this.entity = entity;
    }

    protected ConceptEntity(Parcel in) {
        name = in.readString();
        id = in.readLong();
        entity = in.readParcelable(InnerEntity.class.getClassLoader());
    }

    public static final Creator<ConceptEntity> CREATOR = new Creator<ConceptEntity>() {
        @Override
        public ConceptEntity createFromParcel(Parcel in) {
            return new ConceptEntity(in);
        }

        @Override
        public ConceptEntity[] newArray(int size) {
            return new ConceptEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(id);
        dest.writeParcelable(entity, flags);
    }
}
