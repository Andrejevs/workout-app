package com.eugene.wp.logic;

import com.eugene.wp.Storage;
import com.eugene.wp.data.TaskElement;
import com.eugene.wp.inteface.IAddTrainingController;
import com.eugene.wp.inteface.ITaskItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class AddTrainingLogic {
    private static final Logger log = LoggerFactory.getLogger(AddTrainingLogic.class);
    private final IAddTrainingController listener;
    private final Storage storage;
    private ObservableList<Integer> indexList = FXCollections.observableArrayList();
    private int elementId;
    private int workoutId;

    public enum WorkOutType {
        WORK, REST
    }

    public enum AddElementAction {
        ADD_NEW_ELEMENT, ADD_EXIST_ELEMENT, ADD_EXIST_WORKOUT
    }

    public AddTrainingLogic(IAddTrainingController listener, Storage storage, int workoutId, int elementId) {
        this.listener = listener;
        this.storage = storage;
        this.workoutId = workoutId;
        this.elementId = elementId;
    }

    public int getWorkoutId() {
        return ++workoutId;
    }

    public int getElementId() {
        return ++elementId;
    }

    public void exitApp() {
        log.debug("Closing add training window");

        listener.closeStage();
    }

    public void onAddBtnAction(AddElementAction selectedItem) {
        if (selectedItem != null) {
            switch (selectedItem) {
                case ADD_NEW_ELEMENT:
                    listener.addNewElement(getNewTaskItem(selectedItem));
                    listener.clearAddComboSelection();
                    break;
                case ADD_EXIST_ELEMENT:
                    break;
                case ADD_EXIST_WORKOUT:
                    break;
                default:
                    log.error("Unknown add action name: {}", selectedItem);
                    exitApp();
            }
        }
    }

    public void removeEl(ITaskItem element) {
        listener.removeElFromBox(element);

        ConcurrentHashMap<Parent, ITaskItem> elementsList = storage.getTempItemList();

        elementsList.remove(element.getRoot());

        int elIndex = element.getIndex();
        int lastAddedIndex = indexList.get(indexList.size() - 1);

        if (elIndex != lastAddedIndex) {
            if (elIndex < lastAddedIndex && elIndex >= 0) {
                indexList.remove(indexList.size() - 1);

                elementsList.forEach((root, el) -> {
                    int currentIndex = el.getIndex();

                    if (currentIndex > elIndex) {
                        el.setIndex(currentIndex - 1);
                    }
                });
            } else {
                log.error("Impossible index for element: {}.", element.getName());
                exitApp();
            }
        }

        log.debug("remove element: {}", element.getName());
    }

    public void onChangeComboIndex(Integer newIndex, ITaskItem element) {
        int currentIndex = element.getIndex();

        if (newIndex == null || newIndex == currentIndex) {
            return;
        }

        if (newIndex > currentIndex) {
            listener.removeElFromBox(element);
            listener.addElToBox(element, newIndex);

            changeIndexOfOtherElement(newIndex, currentIndex, true);

            element.setIndex(newIndex);

            return;
        }

        listener.removeElFromBox(element);
        listener.addElToBox(element, newIndex);

        changeIndexOfOtherElement(newIndex, currentIndex, false);

        element.setIndex(newIndex);
    }

    private ITaskItem getNewTaskItem(AddElementAction type) {
        try {
            int currentIndex = listener.getVboxLastIndex() - 1;

            addNewIndexIfNeed(currentIndex);

            ITaskItem item = null;

            switch (type) {
                case ADD_NEW_ELEMENT:
                    item = new TaskElement(getElementId(), this, currentIndex, indexList);
                    break;
                default:
                    log.error("Unknown item type: {}", type);
                    exitApp();
            }

            storage.addItemToTempList(item);

            return item;
        } catch (IOException e) {
            log.error("Error to create Task item. Error: {}", e.getMessage(), e);
            exitApp();
        }

        return null;
    }

    private void addNewIndexIfNeed(int currentIndex) {
        int loop = indexList.size();

        for (int i = 0; i < loop; i++) {
            if (indexList.get(i) == currentIndex) {
                return;
            }
        }

        indexList.add(currentIndex);
    }

    private void changeIndexOfOtherElement(Integer newIndex, int currentIndex, boolean isNewIndexBigger) {
        if (isNewIndexBigger) {
            for (int i = currentIndex + 1; i <= newIndex; i++) {
                int finalI = i;
                int changeIndex = finalI - 1;

                storage.getTempItemList().forEach((parent, el) -> {
                    if (el.getIndex() == finalI) {
                        el.setIndex(changeIndex);
                        el.getController().changeComboIndexValue(changeIndex);
                    }
                });
            }

            return;
        }

        for (int i = currentIndex - 1; i >= newIndex; i--) {
            int finalI = i;
            int changeIndex = finalI + 1;

            storage.getTempItemList().forEach((parent, el) -> {
                if (el.getIndex() == finalI) {
                    el.setIndex(changeIndex);
                    el.getController().changeComboIndexValue(changeIndex);
                }
            });
        }
    }
}
