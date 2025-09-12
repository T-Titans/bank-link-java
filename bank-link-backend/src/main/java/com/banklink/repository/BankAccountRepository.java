package com.banklink.repository;

import com.banklink.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    
    // Fixed query to use the correct relationship path
    @Query("SELECT ba FROM BankAccount ba WHERE ba.user.id = :userId AND ba.accountStatus = 'ACTIVE'")
    List<BankAccount> findActiveAccountsByUserId(@Param("userId") Long userId);
    
    // Find all accounts by user ID
    @Query("SELECT ba FROM BankAccount ba WHERE ba.user.id = :userId")
    List<BankAccount> findByUserId(@Param("userId") Long userId);
    
    // Find account by account ID and user ID for security
    @Query("SELECT ba FROM BankAccount ba WHERE ba.accountId = :accountId AND ba.user.id = :userId")
    Optional<BankAccount> findByAccountIdAndUserId(@Param("accountId") String accountId, @Param("userId") Long userId);
    
    // Find accounts by status using string comparison (since we don't have AccountStatus enum yet)
    @Query("SELECT ba FROM BankAccount ba WHERE ba.accountStatus = :status")
    List<BankAccount> findByAccountStatus(@Param("status") String status);
    
    // Find accounts by account type
    List<BankAccount> findByAccountType(String accountType);
    
    // Check if account exists for user
    @Query("SELECT COUNT(ba) > 0 FROM BankAccount ba WHERE ba.accountId = :accountId AND ba.user.id = :userId")
    boolean existsByAccountIdAndUserId(@Param("accountId") String accountId, @Param("userId") Long userId);
    
    // Additional useful methods
    @Query("SELECT ba FROM BankAccount ba WHERE ba.user.id = :userId AND ba.accountStatus IN ('ACTIVE', 'INACTIVE')")
    List<BankAccount> findActiveAndInactiveAccountsByUserId(@Param("userId") Long userId);
}