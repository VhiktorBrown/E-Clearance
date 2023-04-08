package com.theelitedevelopers.e_clearance.data.models;

public class ClearanceProgress {
    String id;
    String uid;
    Boolean clearedResults;
    Boolean clearedDepartment;
    Boolean clearedFaculty;
    Boolean clearedLibrary;
    Boolean clearedSecurity;
    Boolean clearedStudentAffairs;
    Boolean clearedAlumni;
    Boolean clearedAdmin;
    Boolean clearedBursary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getClearedResults() {
        return clearedResults;
    }

    public void setClearedResults(Boolean clearedResults) {
        this.clearedResults = clearedResults;
    }

    public Boolean getClearedDepartment() {
        return clearedDepartment;
    }

    public void setClearedDepartment(Boolean clearedDepartment) {
        this.clearedDepartment = clearedDepartment;
    }

    public Boolean getClearedFaculty() {
        return clearedFaculty;
    }

    public void setClearedFaculty(Boolean clearedFaculty) {
        this.clearedFaculty = clearedFaculty;
    }

    public Boolean getClearedLibrary() {
        return clearedLibrary;
    }

    public void setClearedLibrary(Boolean clearedLibrary) {
        this.clearedLibrary = clearedLibrary;
    }

    public Boolean getClearedSecurity() {
        return clearedSecurity;
    }

    public void setClearedSecurity(Boolean clearedSecurity) {
        this.clearedSecurity = clearedSecurity;
    }

    public Boolean getClearedStudentAffairs() {
        return clearedStudentAffairs;
    }

    public void setClearedStudentAffairs(Boolean clearedStudentAffairs) {
        this.clearedStudentAffairs = clearedStudentAffairs;
    }

    public Boolean getClearedAlumni() {
        return clearedAlumni;
    }

    public void setClearedAlumni(Boolean clearedAlumni) {
        this.clearedAlumni = clearedAlumni;
    }

    public Boolean getClearedAdmin() {
        return clearedAdmin;
    }

    public void setClearedAdmin(Boolean clearedAdmin) {
        this.clearedAdmin = clearedAdmin;
    }

    public Boolean getClearedBursary() {
        return clearedBursary;
    }

    public void setClearedBursary(Boolean clearedBursary) {
        this.clearedBursary = clearedBursary;
    }
}
