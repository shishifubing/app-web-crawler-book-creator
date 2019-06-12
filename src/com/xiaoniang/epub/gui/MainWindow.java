package com.xiaoniang.epub.gui;

import java.io.IOException;

import com.xiaoniang.epub.api.EpubBook;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainWindow extends Application {

	@Override
	public void start(Stage stage) {
		initUI(stage);
	}

	private void initUI(Stage stage) {
		/*
		 * EpubBook epubBook = null; try { epubBook = new EpubBook("",
		 * "https://www.wuxiaworld.com/novel/renegade-immortal"); } catch (IOException
		 * e) { System.out.println("Problems with the Internet connection"); }
		 */
		HBox root = new HBox();
		root.setPadding(new Insets(0));
		VBox menuPanel = new VBox();
		VBox contentPanel = new VBox();
		menuPanel.setMinSize(224, 600);
		menuPanel.setMaxSize(224, 600);
		menuPanel.getChildren().add(new Button("1"));
		contentPanel.getChildren().add(new Button("2"));
		root.getChildren().addAll(menuPanel, contentPanel);
		Scene scene = new Scene(root, 1024, 600);
		stage.setTitle("Main Window");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
