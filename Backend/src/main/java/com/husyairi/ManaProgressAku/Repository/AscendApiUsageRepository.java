package com.husyairi.ManaProgressAku.Repository;

import com.husyairi.ManaProgressAku.Entity.Model.AscendApiUsage;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AscendApiUsageRepository extends JpaRepository<AscendApiUsage, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM AscendApiUsage u WHERE u.id=1")
    Optional<AscendApiUsage> findForUpdate();

    /*
    PESSIMISTIC WRITE: if 2 request hit concurrently, both read 41 and increment to 42,
                        so, using this, it will lock other request that wants to read the row, until the update is done

     */
}
