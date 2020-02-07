package de.diedavids.cuba.ccsm.web.screens.animal;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.example.Animal;

@UiController("ccsm_Animal.edit")
@UiDescriptor("animal-edit.xml")
@EditedEntityContainer("animalDc")
@LoadDataBeforeShow
public class AnimalEdit extends StandardEditor<Animal> {
}