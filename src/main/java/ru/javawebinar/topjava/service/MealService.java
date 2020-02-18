package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Service
public class MealService {

    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(authUserId(), meal);
    }

    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(authUserId(), id), id);
    }

    public Meal get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(authUserId(), id), id);
    }

    public List<Meal> getAll() {
        return repository.getAll(authUserId());
    }

    public List<Meal> getAll(LocalDate startDate, LocalDate endDate) {
        return repository.getAll(authUserId(), startDate, endDate);
    }

    public void update(Meal meal) throws NotFoundException {
        checkNotFoundWithId(repository.save(authUserId(), meal), meal.getId());
    }
}