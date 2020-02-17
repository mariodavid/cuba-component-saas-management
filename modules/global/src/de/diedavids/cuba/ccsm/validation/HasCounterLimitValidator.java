package de.diedavids.cuba.ccsm.validation;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasCounterLimitValidator implements ConstraintValidator<HasCounterLimit, StandardEntity> {

    private static final Logger log = LoggerFactory.getLogger(HasCounterLimitValidator.class);

    private String limit;

    private Metadata metadata;


    @Override
    public void initialize(HasCounterLimit constraintAnnotation) {
        limit = constraintAnnotation.limit();
        metadata = AppBeans.get(Metadata.class);
    }

    @Override
    public boolean isValid(StandardEntity value, ConstraintValidatorContext context) {
        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        if (!entityStates.isNew(value)) {
            return true;
        }


        Integer limitValue = userSessionSource.getUserSession().getAttribute("limit." + limit);

        if (limitValue != null) {
            long count = dataManager.getCount(selectCountFromEntity(value.getClass()));

            if ((count + 1) > limitValue) {
                renderValidationMessage(value, context, limitValue);
                return false;
            }

            return true;
        } else {
            log.warn("Limit Value '{}' not defined for this Session (Session Attribute: {}). Limit Check will be skipped.", limit, "limit." + limit);
            return true;
        }
    }

    private void renderValidationMessage(StandardEntity value, ConstraintValidatorContext context, Integer limitValue) {
        Messages messages = AppBeans.get(Messages.class);

        MetaClass entityMetaClass = metadata.getClass(value.getClass());
        String formatMessage = messages.formatMessage(
                this.getClass(),
                "HasCounterLimit.message",
                messages.getTools().getEntityCaption(entityMetaClass),
                limitValue
        );
        context.buildConstraintViolationWithTemplate(formatMessage)
                .addBeanNode()
                .addConstraintViolation();

        context.disableDefaultConstraintViolation();
    }

    private LoadContext selectCountFromEntity(Class<? extends StandardEntity> entityClass) {

        MetaClass entityMetaClass = metadata.getClass(entityClass);
        return LoadContext.create(entityClass)
                .setQuery(
                        LoadContext.createQuery("select e from " + entityMetaClass.getName() + " e")
                );
    }
}