package com.evolvingreality.staticmodel.repository;

import com.evolvingreality.staticmodel.domain.StaticModel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StaticModel entity.
 */
@SuppressWarnings("unused")
public interface StaticModelRepository extends JpaRepository<StaticModel,Long> {

}
