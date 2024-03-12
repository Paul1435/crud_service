package ru.test.task.util;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.test.task.exception.BadRequestException;
import ru.test.task.models.Interest;
import ru.test.task.services.InterestService;

import java.util.Objects;

@Component
public class InterestValidate implements Validator {
    private final InterestService interestService;

    @Autowired
    public InterestValidate(InterestService interestService) {
        this.interestService = interestService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Interest.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        Interest interest = (Interest) target;
        if (interest == null || interest.getLabel() == null || interest.getLabel().isEmpty()) {
            throw new BadRequestException("Interest can't be empty");
        }
        if (interestService.findByLabel(interest.getLabel().toLowerCase())
                .filter(InterestFromBD -> !Objects.equals(InterestFromBD.getId(), interest.getId()))
                .isPresent()) {
            errors.rejectValue("label", "", "This interest already exists");
        }
        if (errors.hasErrors()) {
            throw new BadRequestException(GenerateAnswer.generateAnswer(errors));
        }

    }
}
