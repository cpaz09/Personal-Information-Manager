package application.controller;

import application.entity.ContactEntity;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

public interface ContactList {
	
	 ObservableList<ContactEntity> CONTACTLIST = FXCollections.observableArrayList();

}
