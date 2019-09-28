package com.xiaoniang.epub.gui;

import com.xiaoniang.epub.resources.Links;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

import com.xiaoniang.epub.gui.*;

public class MainWindow extends Application {

	@Override
	public void start(Stage stage) {
		initUI(stage);
	}

	private void initUI(Stage stage) {
		
		ArrayList<String> strings = new ArrayList<String>();
		Links.fill();
		for (String link : Links.links().keySet()) {
			strings.add(link);
			/*Button button = new Button(link);
			root.getItems().add(button);
			button.setPrefSize(924, 40);*/
		}
		ListViewController controller = new ListViewController();
		controller.setListView();
		/*root.setStyle("-fx-background-color: BEIGE;");  
		Scene scene = new Scene(root, 1024, 600);
		scene.setFill(Color.CYAN);
		stage.setTitle("Main Window");
		stage.setScene(scene);*/
		stage.setScene(controller.getScene());
		stage.show();
	}

	public static void create(String[] args) {
		launch(args);
	}
}
