package com.eugene.wp.inteface;

import javafx.scene.control.ComboBox;

public interface ITaskController {
    ComboBox<Integer> getComboIndex();
    void initialize();
    void changeComboIndexValue(int changeIndex);
}
