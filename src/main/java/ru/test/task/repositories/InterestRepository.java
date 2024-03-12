package ru.test.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.test.task.models.Interest;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Integer> {
    List<Interest> findInterestsByUsersId(Integer userId);

    Interest findInterestByLabel(String label);


}
