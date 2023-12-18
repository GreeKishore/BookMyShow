package com.bookmyshow.repositories;

import com.bookmyshow.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//1. class is changed to interface
//2. extends jpa repo and gave the parameter
public interface ShowRepository extends JpaRepository<Show,Long> {
    //in parking lot we did map<id,entity>

}
