package com.xiaoniang.epub.gui.windowlist;

import com.xiaoniang.epub.api.EpubBook;
import com.xiaoniang.epub.gui.windowlist.listcell.ListViewCell;
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
    private ListView<EpubBook> listView;
    private ArrayList<EpubBook> links = new ArrayList<EpubBook>();
    ObservableList<EpubBook> observableList = FXCollections.observableArrayList();
    private Scene scene;

    public MainWindowController()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
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

    public void setListView() throws IOException {
    	int i = 0;
        for (String link : Links.links().keySet()) {
			links.add(new EpubBook(link));
			if (++i>3) {
			    break;
            }
		}
        observableList.setAll(links);
        listView.setItems(observableList);
        listView.setCellFactory(listView -> new ListViewCell());
    }
    
    public Scene getScene() {
    	return scene;
    }
}