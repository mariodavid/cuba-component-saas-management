package de.diedavids.cuba.ccsm.web.screens.animal;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.ccsm.entity.example.Animal;

@UiController("ccsm_Animal.browse")
@UiDescriptor("animal-browse.xml")
@LookupComponent("animalsTable")
@LoadDataBeforeShow
public class AnimalBrowse extends StandardLookup<Animal> {
}