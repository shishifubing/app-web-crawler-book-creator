package com.xiaoniang.epub.gui;

import com.xiaoniang.epub.resources.Links;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainWindowController
{
    @FXML
    private ListView<String> listView;
    private ArrayList<String> strings = new ArrayList<String>();
    ObservableList<String> observableList = FXCollections.observableArrayList();
    private Scene scene;

    public MainWindowController()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/MainWindow.fxml"));
        fxmlLoader.setController(this);
        try
        {
            Parent parent = (Parent)fxmlLoader.load();
            scene = new Scene(parent, 400.0 ,500.0);
        }
        catch (IOException e)
        {
        	throw new RuntimeException(e);      
        }
    }

    public void setListView()
    {
    	for (String link : Links.links().keySet()) {
			strings.add(link);
		}
        observableList.setAll(strings);
        listView.setItems(observableList);
        listView.setCellFactory(listView -> new ListViewCell());
    }
    
    public Scene getScene() {
    	return scene;
    }
}