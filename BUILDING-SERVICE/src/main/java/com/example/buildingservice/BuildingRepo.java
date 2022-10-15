package com.example.buildingservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepo extends JpaRepository<BuildingEntity,Long> {
}
