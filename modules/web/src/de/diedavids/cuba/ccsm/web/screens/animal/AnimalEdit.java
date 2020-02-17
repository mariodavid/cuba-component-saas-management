package de.diedavids.cuba.ccsm.web.screens.animal;

import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.example.Animal;
import de.diedavids.cuba.ccsm.limit.Limitations;

import javax.inject.Inject;

@UiController("ccsm_Animal.edit")
@UiDescriptor("animal-edit.xml")
@EditedEntityContainer("animalDc")
@LoadDataBeforeShow
public class AnimalEdit extends StandardEditor<Animal> {


    @Inject
    protected FileUploadField imageField;


    @Inject
    protected Limitations limitations;

    @Subscribe
    protected void onInit(InitEvent event) {

        if (limitations.hasLimit("IMAGE_FILE_SIZE")) {
            imageField.setFileSizeLimit(
                limitations.getLimit("IMAGE_FILE_SIZE") * 1024
            );
        }
    }
}