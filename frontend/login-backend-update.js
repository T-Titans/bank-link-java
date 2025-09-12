// Replace your existing login form handler in index.html with this:
document.getElementById('login-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    
    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        
        const result = await response.json();
        
        if (result.success) {
            // Store login state (keeping your localStorage approach)
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('currentUser', JSON.stringify(result.user));
            
            // Redirect to dashboard (you can change this to your banking page)
            window.location.href = 'dashboard.html';
        } else {
            document.getElementById('login-error').style.display = 'block';
            document.getElementById('login-error').textContent = result.message;
        }
    } catch (error) {
        console.error('Login error:', error);
        document.getElementById('login-error').style.display = 'block';
        document.getElementById('login-error').textContent = 'Connection error. Please try again.';
    }
});

// Replace your existing register form handler with this:
document.getElementById('register-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    
    const name = document.getElementById('reg-name').value;
    const surname = document.getElementById('reg-surname').value;
    const idNumber = document.getElementById('reg-id').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    const confirmPassword = document.getElementById('reg-confirm-password').value;
    
    // Keep your existing frontend validation
    if (password !== confirmPassword) {
        document.getElementById('register-error').textContent = 'Passwords do not match!';
        document.getElementById('register-error').style.display = 'block';
        return;
    }
    
    if (idNumber.length !== 13 || !/^\d+$/.test(idNumber)) {
        document.getElementById('register-error').textContent = 'Please enter a valid 13-digit ID number';
        document.getElementById('register-error').style.display = 'block';
        return;
    }
    
    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, surname, idNumber, email, password })
        });
        
        const result = await response.json();
        
        if (result.success) {
            // Auto-login and redirect
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('currentUser', JSON.stringify(result.user));
            window.location.href = 'dashboard.html';
        } else {
            document.getElementById('register-error').textContent = result.message;
            document.getElementById('register-error').style.display = 'block';
        }
    } catch (error) {
        console.error('Registration error:', error);
        document.getElementById('register-error').textContent = 'Connection error. Please try again.';
        document.getElementById('register-error').style.display = 'block';
    }
});
