package ua.pp.sanderzet.staffagency.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by sander on 22.04.17.
 */
public class Job {
    private final IntegerProperty rowid;
    private final IntegerProperty idPerson;
    private final StringProperty place;
    private final StringProperty firm;
    private final StringProperty position;
//    Next two now don`t use, for future purpose
    private final ObjectProperty<LocalDate> startJob;
    private final ObjectProperty<LocalDate> endJob;

    private final ObjectProperty<LocalDate> transitionJob;

//    Default constructor

    public Job()
    {
        this(0,0, null,null,null,
                null,null, null);
    }

    public Job(Integer rowid, Integer idPerson, String place, String firm, String position,
               LocalDate startJob, LocalDate end, LocalDate transitionJob) {
        this.rowid = new SimpleIntegerProperty(rowid);
        this.idPerson = new SimpleIntegerProperty(idPerson);
        this.place = new SimpleStringProperty(place);
        this.firm = new SimpleStringProperty(firm);
        this.position = new SimpleStringProperty(position);
        this.startJob = new SimpleObjectProperty<>(startJob);
        this.endJob = new SimpleObjectProperty<>(end);
        this.transitionJob = new SimpleObjectProperty<> (transitionJob);
    }

    public void setRowid(int rowid) {
        this.rowid.set(rowid);
    }

    public int getRowid() {
        return rowid.get();
    }

    public IntegerProperty rowidProperty() {
        return rowid;
    }

    public void setIdPerson(Integer idPerson) {this.idPerson.set(idPerson);}
public int getIdPerson () { return idPerson.get();}
public IntegerProperty idPersonProperty() {return idPerson;}
    public void setPlace(String place) {
        this.place.set(place);
    }

    public String getPlace() {
        return place.get();
    }

    public StringProperty placeProperty() {
        return place;
    }

    public void setFirm(String firm) {
        this.firm.set(firm);
    }

    public String getFirm() {
        return firm.get();
    }

    public StringProperty firmProperty() {
        return firm;
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty positionProperty() {
        return position;
    }

    public void setStartJob(LocalDate startJob) {
        this.startJob.set(startJob);
    }

    public LocalDate getStartJob() {
        return startJob.get();
    }

    public ObjectProperty<LocalDate> startJobProperty() {
        return startJob;
    }

    public void setEndJob(LocalDate endJob) {
        this.endJob.set(endJob);
    }

    public LocalDate getEndJob() {
        return endJob.get();
    }

    public ObjectProperty<LocalDate> endJobProperty() {
        return endJob;
    }

    public void setTransitionJob(LocalDate transitionJob) {this.transitionJob.set(transitionJob);}

    public LocalDate getTransitionJob() {return transitionJob.get();}

    public ObjectProperty<LocalDate> transitionJobProperty() {return transitionJob; }
}
