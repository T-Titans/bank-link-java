//// script.js
//// Get current user data from localStorage
//const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
//let accounts = currentUser.accounts || {
//    "ACC001": {
//        name: "Cheque Account",
//        balance: 1000.00,
//        holder: currentUser.fullName || "Bank-Link User",
//        transactions: [
//            { date: new Date().toISOString().split('T')[0], type: "Opening Balance", amount: 1000.00, balance: 1000.00 }
//        ]
//    },
//    "SAV001": {
//        name: "Savings Account",
//        balance: 1000.00,
//        holder: currentUser.fullName || "Bank-Link User",
//        transactions: [
//            { date: new Date().toISOString().split('T')[0], type: "Opening Balance", amount: 1000.00, balance: 1000.00 }
//        ]
//    }
//};
//
//// Save accounts back to user data
//function saveUserData() {
//    if (currentUser.id) {
//        currentUser.accounts = accounts;
//        localStorage.setItem('currentUser', JSON.stringify(currentUser));
//
//        // Also update in users array
//        const users = JSON.parse(localStorage.getItem('banklink_users') || '[]');
//        const userIndex = users.findIndex(u => u.id === currentUser.id);
//        if (userIndex !== -1) {
//            users[userIndex] = currentUser;
//            localStorage.setItem('banklink_users', JSON.stringify(users));
//        }
//    }
//}
//
//// DOM Content Loaded - Start the app when the page is ready
//document.addEventListener('DOMContentLoaded', function() {
//    updateDashboard();
//    setupEventListeners();
//});
//
//function updateDashboard() {
//    // Update the balance on the dashboard for each account
//    if (accounts['ACC001']) {
//        document.getElementById('balance-acc001').textContent = formatCurrency(accounts['ACC001'].balance);
//    }
//    if (accounts['SAV001']) {
//        document.getElementById('balance-sav001').textContent = formatCurrency(accounts['SAV001'].balance);
//    }
//}
//
//function formatCurrency(amount) {
//    return 'R' + amount.toFixed(2);
//}
//
//// MODAL FUNCTIONS
//function showModal(modalId) {
//    document.getElementById(modalId).style.display = 'block';
//}
//
//function closeModal(modalId) {
//    document.getElementById(modalId).style.display = 'none';
//}
//
//function showNotification(message, isSuccess = true) {
//    const toast = document.getElementById('notification');
//    toast.className = isSuccess ? 'toast success' : 'toast error';
//    toast.querySelector('#notification-message').textContent = message;
//    toast.classList.remove('hidden');
//
//    // Hide the notification after 3 seconds
//    setTimeout(() => {
//        toast.classList.add('hidden');
//    }, 3000);
//}
//
//function setupEventListeners() {
//    // Deposit Form
//    document.getElementById('deposit-form').addEventListener('submit', function(event) {
//        event.preventDefault();
//        const accountId = document.getElementById('deposit-account').value;
//        const amount = parseFloat(document.getElementById('deposit-amount').value);
//        const date = new Date().toISOString().split('T')[0]; // Get current date
//
//        if (amount <= 0) {
//            showNotification("Deposit amount must be positive.", false);
//            return;
//        }
//
//        if (!accounts[accountId]) {
//            showNotification("Selected account not found.", false);
//            return;
//        }
//
//        // Update balance
//        accounts[accountId].balance += amount;
//
//        // Add to transaction history
//        accounts[accountId].transactions.push({
//            date: date,
//            type: "Deposit",
//            amount: amount,
//            balance: accounts[accountId].balance
//        });
//
//        // Update the UI and show success message
//        updateDashboard();
//        showNotification(`Successfully deposited ${formatCurrency(amount)} to ${accounts[accountId].name}`, true);
//        closeModal('deposit-modal');
//        this.reset(); // Clear the form
//
//        // Save user data
//        saveUserData();
//    });
//
//    // Withdraw Form
//    document.getElementById('withdraw-form').addEventListener('submit', function(event) {
//        event.preventDefault();
//        const accountId = document.getElementById('withdraw-account').value;
//        const amount = parseFloat(document.getElementById('withdraw-amount').value);
//        const date = new Date().toISOString().split('T')[0];
//
//        if (amount <= 0) {
//            showNotification("Withdrawal amount must be positive.", false);
//            return;
//        }
//
//        if (!accounts[accountId]) {
//            showNotification("Selected account not found.", false);
//            return;
//        }
//
//        // Check for sufficient funds
//        if (amount > accounts[accountId].balance) {
//            showNotification(`Insufficient funds! Your balance is ${formatCurrency(accounts[accountId].balance)}.`, false);
//            return;
//        }
//
//        // Process the withdrawal
//        accounts[accountId].balance -= amount;
//
//        // Add to transaction history
//        accounts[accountId].transactions.push({
//            date: date,
//            type: "Withdrawal",
//            amount: -amount,
//            balance: accounts[accountId].balance
//        });
//
//        // Update the UI
//        updateDashboard();
//        showNotification(`Successfully withdrew ${formatCurrency(amount)} from ${accounts[accountId].name}`, true);
//        closeModal('withdraw-modal');
//        this.reset();
//
//        // Save user data
//        saveUserData();
//    });
//
//    // Transfer Form
//    document.getElementById('transfer-form').addEventListener('submit', function(event) {
//        event.preventDefault();
//        const fromAccountId = document.getElementById('from-account').value;
//        const toAccountId = document.getElementById('to-account').value;
//        const amount = parseFloat(document.getElementById('transfer-amount').value);
//        const date = new Date().toISOString().split('T')[0];
//
//        if (amount <= 0) {
//            showNotification("Transfer amount must be positive.", false);
//            return;
//        }
//
//        if (!accounts[fromAccountId]) {
//            showNotification("Source account not found.", false);
//            return;
//        }
//
//        // Check if recipient account exists
//        if (!accounts[toAccountId]) {
//            showNotification(`Recipient account ${toAccountId} not found.`, false);
//            return;
//        }
//
//        // Check for sufficient funds
//        if (amount > accounts[fromAccountId].balance) {
//            showNotification(`Insufficient funds! Your balance is ${formatCurrency(accounts[fromAccountId].balance)}.`, false);
//            return;
//        }
//
//        // Process the transfer
//        accounts[fromAccountId].balance -= amount;
//        accounts[toAccountId].balance += amount;
//
//        // Add to transaction history for both accounts
//        accounts[fromAccountId].transactions.push({
//            date: date,
//            type: "Transfer Out",
//            amount: -amount,
//            balance: accounts[fromAccountId].balance,
//            toAccount: toAccountId
//        });
//
//        accounts[toAccountId].transactions.push({
//            date: date,
//            type: "Transfer In",
//            amount: amount,
//            balance: accounts[toAccountId].balance,
//            fromAccount: fromAccountId
//        });
//
//        // Update the UI
//        updateDashboard();
//        showNotification(`Successfully transferred ${formatCurrency(amount)} to account ${toAccountId}`, true);
//        closeModal('transfer-modal');
//        this.reset();
//
//        // Save user data
//        saveUserData();
//    });
//
//    // Download Statement Button
//    document.getElementById('download-statement').addEventListener('click', function() {
//        const accountId = document.getElementById('statement-account').value;
//        downloadStatementAsPDF(accountId);
//    });
//}
//
//// NEW FUNCTION: Download Bank Statement as PDF (Fixed version)
//function downloadStatementAsPDF(accountId) {
//    const account = accounts[accountId];
//    if (!account) {
//        showNotification("Account not found.", false);
//        return;
//    }
//
//    try {
//        // Load jsPDF library dynamically if not available
//        if (typeof window.jspdf === 'undefined') {
//            showNotification("PDF library loading. Please try again in a moment.", false);
//
//            // Load the library dynamically
//            const script = document.createElement('script');
//            script.src = 'https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js';
//            script.onload = function() {
//                const script2 = document.createElement('script');
//                script2.src = 'https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.28/jspdf.plugin.autotable.min.js';
//                script2.onload = function() {
//                    downloadStatementAsPDF(accountId); // Retry after loading
//                };
//                document.head.appendChild(script2);
//            };
//            document.head.appendChild(script);
//            return;
//        }
//
//        const { jsPDF } = window.jspdf;
//        const doc = new jsPDF();
//
//        // Add Bank-Link header
//        doc.setFontSize(20);
//        doc.setTextColor(40, 40, 40);
//        doc.text("BANK-LINK STATEMENT", 105, 20, { align: 'center' });
//
//        doc.setFontSize(12);
//        doc.setTextColor(100, 100, 100);
//        doc.text(`Generated on: ${new Date().toLocaleDateString()}`, 105, 30, { align: 'center' });
//
//        // Add account information
//        doc.setFontSize(14);
//        doc.setTextColor(0, 0, 0);
//        doc.text(`Account: ${account.name} (${accountId})`, 20, 50);
//        doc.text(`Account Holder: ${currentUser.fullName || 'Bank-Link User'}`, 20, 60);
//        doc.text(`Statement Period: Up to ${new Date().toLocaleDateString()}`, 20, 70);
//
//        // Prepare transaction data for the table
//        const tableData = account.transactions.map(transaction => [
//            transaction.date,
//            transaction.type,
//            formatCurrency(transaction.amount),
//            formatCurrency(transaction.balance)
//        ]);
//
//        // Add transactions table
//        doc.autoTable({
//            startY: 80,
//            head: [['Date', 'Transaction Type', 'Amount', 'Balance']],
//            body: tableData,
//            theme: 'grid',
//            headStyles: {
//                fillColor: [255, 98, 0],
//                textColor: [255, 255, 255],
//                fontStyle: 'bold'
//            },
//            alternateRowStyles: {
//                fillColor: [245, 245, 245]
//            }
//        });
//
//        // Add summary at the end
//        const finalY = doc.lastAutoTable.finalY + 20;
//        doc.setFontSize(12);
//        doc.setTextColor(0, 0, 0);
//        doc.text(`Total Transactions: ${account.transactions.length}`, 20, finalY);
//        doc.text(`Ending Balance: ${formatCurrency(account.balance)}`, 20, finalY + 10);
//
//        // Save the PDF
//        doc.save(`Bank-Statement-${accountId}-${new Date().toISOString().split('T')[0]}.pdf`);
//
//        showNotification(`PDF statement downloaded for ${account.name}`, true);
//
//    } catch (error) {
//        console.error("PDF generation error:", error);
//        showNotification("Error generating PDF. Please try again.", false);
//
//        // Fallback to CSV if PDF fails
//        downloadStatementAsCSV(accountId);
//    }
//}
//
//// Fallback function: Download as CSV
//function downloadStatementAsCSV(accountId) {
//    const account = accounts[accountId];
//
//    // Create CSV content
//    let csvContent = "Bank-Link Statement\n";
//    csvContent += `Account: ${account.name} (${accountId})\n`;
//    csvContent += `Account Holder: ${currentUser.fullName || 'Bank-Link User'}\n`;
//    csvContent += `Statement Date: ${new Date().toLocaleDateString()}\n\n`;
//    csvContent += "Date,Transaction Type,Amount,Balance\n";
//
//    account.transactions.forEach(transaction => {
//        csvContent += `${transaction.date},${transaction.type},${formatCurrency(transaction.amount)},${formatCurrency(transaction.balance)}\n`;
//    });
//
//    csvContent += `\nEnding Balance: ${formatCurrency(account.balance)}`;
//
//    // Create download link
//    const blob = new Blob([csvContent], { type: 'text/csv' });
//    const url = window.URL.createObjectURL(blob);
//    const a = document.createElement('a');
//    a.setAttribute('hidden', '');
//    a.setAttribute('href', url);
//    a.setAttribute('download', `Bank-Statement-${accountId}-${new Date().toISOString().split('T')[0]}.csv`);
//    document.body.appendChild(a);
//    a.click();
//    document.body.removeChild(a);
//
//    showNotification(`CSV statement downloaded for ${account.name}`, true);
//}
//
//// Close modals if user clicks outside of them
//window.onclick = function(event) {
//    const modals = document.getElementsByClassName('modal');
//    for (let modal of modals) {
//        if (event.target == modal) {
//            modal.style.display = "none";
//        }
//    }
//}
//
//// Utility function to format dates
//function formatDate(dateString) {
//    const options = { year: 'numeric', month: 'short', day: 'numeric' };
//    return new Date(dateString).toLocaleDateString(undefined, options);
//}

// script.js

// IMPORTANT: Replace this with the URL of your Spring Boot backend
const BASE_URL = 'http://localhost:8080/api';

let accounts = {}; // Accounts will be loaded from the backend

// DOM Content Loaded - Start the app when the page is ready
document.addEventListener('DOMContentLoaded', function() {
    fetchAccounts();
    setupEventListeners();
});

// Fetches the current user's accounts and updates the dashboard
async function fetchAccounts() {
    // You will need to pass an identifier like a user ID or a session token
    // For now, we assume the backend knows the logged-in user.
    try {
        const response = await fetch(`${BASE_URL}/accounts`);
        if (!response.ok) {
            throw new Error('Failed to fetch accounts.');
        }
        const data = await response.json();
        accounts = data; // Update local accounts object with data from backend
        updateDashboard();
    } catch (error) {
        console.error("Error fetching accounts:", error);
        showNotification("Failed to load accounts. Please try again.", false);
    }
}

function updateDashboard() {
    if (accounts['ACC001']) {
        document.getElementById('balance-acc001').textContent = formatCurrency(accounts['ACC001'].balance);
    }
    if (accounts['SAV001']) {
        document.getElementById('balance-sav001').textContent = formatCurrency(accounts['SAV001'].balance);
    }
}

function formatCurrency(amount) {
    return 'R' + amount.toFixed(2);
}

// MODAL FUNCTIONS
function showModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

function showNotification(message, isSuccess = true) {
    const toast = document.getElementById('notification');
    toast.className = isSuccess ? 'toast success' : 'toast error';
    toast.querySelector('#notification-message').textContent = message;
    toast.classList.remove('hidden');
    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3000);
}

function setupEventListeners() {
    // Deposit Form
    document.getElementById('deposit-form').addEventListener('submit', async function(event) {
        event.preventDefault();
        const accountId = document.getElementById('deposit-account').value;
        const amount = parseFloat(document.getElementById('deposit-amount').value);

        if (amount <= 0) {
            showNotification("Deposit amount must be positive.", false);
            return;
        }

        try {
            const response = await fetch(`${BASE_URL}/accounts/deposit`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ accountId, amount })
            });

            const result = await response.json();

            if (!response.ok) {
                // The server returned an error
                throw new Error(result.message || "Failed to process deposit.");
            }

            // Success! Update the dashboard with new data from server response
            accounts[accountId].balance = result.newBalance;
            updateDashboard();
            showNotification(`Successfully deposited ${formatCurrency(amount)} to ${accounts[accountId].name}`, true);
            closeModal('deposit-modal');
            this.reset();
        } catch (error) {
            console.error("Deposit error:", error);
            showNotification(error.message, false);
        }
    });

    // Withdraw Form
    document.getElementById('withdraw-form').addEventListener('submit', async function(event) {
        event.preventDefault();
        const accountId = document.getElementById('withdraw-account').value;
        const amount = parseFloat(document.getElementById('withdraw-amount').value);

        if (amount <= 0) {
            showNotification("Withdrawal amount must be positive.", false);
            return;
        }

        try {
            const response = await fetch(`${BASE_URL}/accounts/withdraw`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ accountId, amount })
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || "Failed to process withdrawal.");
            }

            accounts[accountId].balance = result.newBalance;
            updateDashboard();
            showNotification(`Successfully withdrew ${formatCurrency(amount)} from ${accounts[accountId].name}`, true);
            closeModal('withdraw-modal');
            this.reset();
        } catch (error) {
            console.error("Withdrawal error:", error);
            showNotification(error.message, false);
        }
    });

    // Transfer Form
    document.getElementById('transfer-form').addEventListener('submit', async function(event) {
        event.preventDefault();
        const fromAccountId = document.getElementById('from-account').value;
        const toAccountId = document.getElementById('to-account').value;
        const amount = parseFloat(document.getElementById('transfer-amount').value);

        if (amount <= 0) {
            showNotification("Transfer amount must be positive.", false);
            return;
        }

        try {
            const response = await fetch(`${BASE_URL}/accounts/transfer`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ fromAccountId, toAccountId, amount })
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || "Failed to process transfer.");
            }

            // Update both accounts based on the server's response
            accounts[fromAccountId].balance = result.fromAccount.newBalance;
            accounts[toAccountId].balance = result.toAccount.newBalance;

            updateDashboard();
            showNotification(`Successfully transferred ${formatCurrency(amount)} to account ${toAccountId}`, true);
            closeModal('transfer-modal');
            this.reset();
        } catch (error) {
            console.error("Transfer error:", error);
            showNotification(error.message, false);
        }
    });

    // Download Statement Button
    document.getElementById('download-statement').addEventListener('click', function() {
        const accountId = document.getElementById('statement-account').value;
        // This function will remain unchanged as PDF generation is a client-side task
        downloadStatementAsPDF(accountId);
    });
}

// The following functions remain mostly unchanged, as they are client-side utilities.
function downloadStatementAsPDF(accountId) {
    const account = accounts[accountId];
    if (!account) {
        showNotification("Account not found.", false);
        return;
    }

    try {
        if (typeof window.jspdf === 'undefined') {
            showNotification("PDF library loading. Please try again in a moment.", false);
            const script = document.createElement('script');
            script.src = 'https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js';
            script.onload = function() {
                const script2 = document.createElement('script');
                script2.src = 'https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.28/jspdf.plugin.autotable.min.js';
                script2.onload = function() {
                    downloadStatementAsPDF(accountId);
                };
                document.head.appendChild(script2);
            };
            document.head.appendChild(script);
            return;
        }

        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();

        doc.setFontSize(20);
        doc.setTextColor(40, 40, 40);
        doc.text("BANK-LINK STATEMENT", 105, 20, { align: 'center' });

        doc.setFontSize(12);
        doc.setTextColor(100, 100, 100);
        doc.text(`Generated on: ${new Date().toLocaleDateString()}`, 105, 30, { align: 'center' });

        doc.setFontSize(14);
        doc.setTextColor(0, 0, 0);
        doc.text(`Account: ${account.name} (${accountId})`, 20, 50);
        doc.text(`Account Holder: Bank-Link User`, 20, 60);
        doc.text(`Statement Period: Up to ${new Date().toLocaleDateString()}`, 20, 70);

        const tableData = account.transactions.map(transaction => [
            transaction.date,
            transaction.type,
            formatCurrency(transaction.amount),
            formatCurrency(transaction.balance)
        ]);

        doc.autoTable({
            startY: 80,
            head: [['Date', 'Transaction Type', 'Amount', 'Balance']],
            body: tableData,
            theme: 'grid',
            headStyles: {
                fillColor: [255, 98, 0],
                textColor: [255, 255, 255],
                fontStyle: 'bold'
            },
            alternateRowStyles: {
                fillColor: [245, 245, 245]
            }
        });

        const finalY = doc.lastAutoTable.finalY + 20;
        doc.setFontSize(12);
        doc.setTextColor(0, 0, 0);
        doc.text(`Total Transactions: ${account.transactions.length}`, 20, finalY);
        doc.text(`Ending Balance: ${formatCurrency(account.balance)}`, 20, finalY + 10);

        doc.save(`Bank-Statement-${accountId}-${new Date().toISOString().split('T')[0]}.pdf`);
        showNotification(`PDF statement downloaded for ${account.name}`, true);

    } catch (error) {
        console.error("PDF generation error:", error);
        showNotification("Error generating PDF. Please try again.", false);
    }
}

// Fallback function: Download as CSV
function downloadStatementAsCSV(accountId) {
    // This function will remain unchanged
    const account = accounts[accountId];
    let csvContent = "Bank-Link Statement\n";
    csvContent += `Account: ${account.name} (${accountId})\n`;
    csvContent += `Account Holder: Bank-Link User\n`;
    csvContent += `Statement Date: ${new Date().toLocaleDateString()}\n\n`;
    csvContent += "Date,Transaction Type,Amount,Balance\n";

    account.transactions.forEach(transaction => {
        csvContent += `${transaction.date},${transaction.type},${formatCurrency(transaction.amount)},${formatCurrency(transaction.balance)}\n`;
    });

    csvContent += `\nEnding Balance: ${formatCurrency(account.balance)}`;

    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.setAttribute('hidden', '');
    a.setAttribute('href', url);
    a.setAttribute('download', `Bank-Statement-${accountId}-${new Date().toISOString().split('T')[0]}.csv`);
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);

    showNotification(`CSV statement downloaded for ${account.name}`, true);
}

// Close modals if user clicks outside of them
window.onclick = function(event) {
    const modals = document.getElementsByClassName('modal');
    for (let modal of modals) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}