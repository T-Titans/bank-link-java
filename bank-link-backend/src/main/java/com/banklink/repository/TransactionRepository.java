package com.banklink.repository;

import com.banklink.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Fixed: Use explicit @Query to avoid confusion with property path resolution
    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountIdOrderByTransactionDateDesc(@Param("accountId") String accountId);
    
    // Find transactions by account ID
    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") String accountId);
    
    // Find transactions by status
    @Query("SELECT t FROM Transaction t WHERE t.status = :status")
    List<Transaction> findByStatus(@Param("status") String status);
    
    // Find transactions by type
    @Query("SELECT t FROM Transaction t WHERE t.transactionType = :type")
    List<Transaction> findByTransactionType(@Param("type") String type);
    
    // Find transactions by date range
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByTransactionDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find transactions by account and date range
    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountIdAndDateRange(@Param("accountId") String accountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find recent transactions by account (last 10)
    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId ORDER BY t.transactionDate DESC LIMIT 10")
    List<Transaction> findRecentTransactionsByAccountId(@Param("accountId") String accountId);
    
    // Find transactions by reference number
    @Query("SELECT t FROM Transaction t WHERE t.referenceNumber = :referenceNumber")
    List<Transaction> findByReferenceNumber(@Param("referenceNumber") String referenceNumber);
    
    // Count transactions by account
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.accountId = :accountId")
    long countByAccountId(@Param("accountId") String accountId);
}