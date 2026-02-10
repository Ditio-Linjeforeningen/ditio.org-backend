package org.ditio.backend.Repositories;

import org.ditio.backend.Entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
}