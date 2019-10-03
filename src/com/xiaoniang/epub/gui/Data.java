package com.xiaoniang.epub.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.io.IOException;

public class Data
{
    @FXML private HBox hBox;
    @FXML private Label label1;
    @FXML private Label label2;
    @FXML private Label label3;
    @FXML private Label label4;
    @FXML private Label label5;
    @FXML private Label label6;


    Data()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/listCellItem.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    void setInfo(String string)
    {
        label1.setText("1");
        label2.setText("2");
        label3.setText("3");
        label4.setText("4");
        label5.setText("5");
        label6.setText(string);
    }

    HBox getBox()
    {
        return hBox;
    }
}