package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PCM;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PCM entity.
 */
public interface PCMRepository extends JpaRepository<PCM,Long> {

}
