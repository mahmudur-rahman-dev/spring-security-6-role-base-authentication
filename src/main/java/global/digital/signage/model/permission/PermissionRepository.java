package global.digital.signage.model.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findByIdInAndActiveAndParentNotNullOrderBySortAsc(List<Long> permissions, boolean active);

    List<Permission> findByIdInAndActiveOrderBySortAsc(List<Long> permissions, boolean active);

}
