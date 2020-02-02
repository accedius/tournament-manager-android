package fit.cvut.org.cz.bowling.business.entities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import fit.cvut.org.cz.tmlibrary.data.entities.Participant;

public class ParticipantSharedViewModel extends ViewModel {
    //When managed - out
    private MutableLiveData<List<Participant>> toAdd = new MutableLiveData<>();
    private MutableLiveData<Participant> toDelete = new MutableLiveData<>();
    private MutableLiveData<Participant> toManage = new MutableLiveData<>();

    //From change in stats - in
    private MutableLiveData<Participant> toChangeStat = new MutableLiveData<>();

    public LiveData<List<Participant>> getToAdd() {
        return toAdd;
    }

    public void setToAdd(List<Participant> managedParticipants) {
        this.toAdd.setValue(managedParticipants);
    }

    public LiveData<Participant> getToDelete() {
        return toDelete;
    }

    public void setToDelete(Participant toDelete) {
        this.toDelete.setValue(toDelete);
    }

    public LiveData<Participant> getToManage() {
        return toManage;
    }

    public void setToManage(Participant toManage) {
        this.toManage.setValue(toManage);
    }

    public LiveData<Participant> getToChangeStat() {
        return toChangeStat;
    }

    public void setToChangeStat(Participant toChangeStat) {
        this.toChangeStat.setValue(toChangeStat);
    }
}
