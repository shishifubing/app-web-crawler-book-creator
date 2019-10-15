package com.xiaoniang.epub.gui.windowlist.listcell;

import com.xiaoniang.epub.api.EpubBook;
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


    Data() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("listCellItem.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void setInfo(EpubBook book)
    {
        label1.setText(book.author());
        label2.setText(book.title());
        label3.setText(""+book.tags().size());
        label4.setText("0");
        label5.setText("0");
        label6.setText("0");
    }

    HBox getBox()
    {
        return hBox;
    }
}