package com.xiaoniang.epub.gui.windowlist.listcell;

import com.xiaoniang.epub.api.EpubBook;
import javafx.scene.control.ListCell;

public class ListViewCell extends ListCell<EpubBook>
{
    @Override
    public void updateItem(EpubBook EpubBook, boolean empty)
    {
        super.updateItem(EpubBook,empty);
        if(EpubBook != null)
        {
            Data data = new Data();
            data.setInfo(EpubBook);
            setGraphic(data.getBox());
        }
    }
}

