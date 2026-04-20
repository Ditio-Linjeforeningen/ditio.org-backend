package org.ditio.backend.Repositories;

import org.ditio.backend.Entities.TestAtt;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface TestAttRepository extends JpaRepository<TestAtt, UUID> {
}