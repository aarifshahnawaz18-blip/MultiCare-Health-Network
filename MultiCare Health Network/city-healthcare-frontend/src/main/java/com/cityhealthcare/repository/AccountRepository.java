package com.cityhealthcare.repository;
import com.cityhealthcare.model.Account; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface AccountRepository extends JpaRepository<Account,Long>{
 Optional<Account> findByEmail(String email);
 List<Account> findByStatusOrderByCreatedAtAsc(String status);
 List<Account> findByRoleAndStatus(String role,String status);
 long countByStatus(String status);
}
