package com.eugene.wp.inteface;

import com.eugene.wp.data.TaskElement;

public interface IAddTrainingController {
    int getVboxLastIndex();

    void removeElFromBox(ITaskItem root);

    void addElToBox(ITaskItem root, int index);

    void closeStage();

    void cleanScene();

    void addNewElement(ITaskItem element);

    void clearAddComboSelection();
}
