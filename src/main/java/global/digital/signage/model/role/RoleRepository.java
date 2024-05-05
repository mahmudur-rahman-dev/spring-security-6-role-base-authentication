package global.digital.signage.model.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findFirstByName(String name);

    Page<Role> findByCompanyIdOrderByRoleIdDesc(Long id, Pageable pageable);

    Page<Role> findAllByOrderByRoleIdDesc(Pageable pageable);

    Role findByRoleIdAndCompanyId(Long roleId, Long organizationId);

    @Query(value = "select r.roleId from Role as r where r.companyId=:companyId")
    List<Long> findRoleIdsByOrganizationId(Long companyId);

    @Query(value = "select r from Role as r where r.companyId=:companyId or r.name=:defaultUserRole order by r.roleId desc")
    List<Role> findAllByCompanyId(@Param("companyId") Long companyIdFromClaims, @Param("defaultUserRole") String defaultUserRole);
}
