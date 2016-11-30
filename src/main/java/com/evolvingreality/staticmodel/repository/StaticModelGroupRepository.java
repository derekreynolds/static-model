package com.evolvingreality.staticmodel.repository;

import com.evolvingreality.staticmodel.domain.StaticModelGroup;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;

/**
 * Spring Data JPA repository for the StaticModelGroup entity.
 */
public interface StaticModelGroupRepository extends JpaRepository<StaticModelGroup,Long> {
	
	Optional<StaticModelGroup> findByApplicationNameAndGroupNameAndStaticModels_Locale(String applicationName, String groupName, String language);
}
