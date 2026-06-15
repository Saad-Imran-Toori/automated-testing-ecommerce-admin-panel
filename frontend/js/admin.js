// API Base URL
const API_URL = 'http://localhost/ecommerce_api/backend/api/products.php';

// Load products on page load
document.addEventListener('DOMContentLoaded', () => {
    loadProducts();
    displayAdminName();
    
    // Add product form submit
    document.getElementById('addProductForm').addEventListener('submit', addProduct);
    
    // Edit product form submit
    document.getElementById('editProductForm').addEventListener('submit', updateProduct);
});

// Display admin name
function displayAdminName() {
    const name = localStorage.getItem('admin_name');
    if (name && document.getElementById('adminName')) {
        document.getElementById('adminName').textContent = name;
    }
}

// Logout function
function logout() {
    localStorage.removeItem('admin_logged_in');
    localStorage.removeItem('admin_email');
    localStorage.removeItem('admin_name');
    window.location.href = 'admin_login.html';
}

// Show alert message
function showAlert(message, type) {
    const alertDiv = document.getElementById('alert');
    alertDiv.textContent = message;
    alertDiv.className = `alert alert-${type}`;
    
    setTimeout(() => {
        alertDiv.className = 'alert';
    }, 3000);
}

// Load all products (GET)
async function loadProducts() {
    const tbody = document.getElementById('productsTableBody');
    tbody.innerHTML = '<tr><td colspan="7" class="loading">Loading products...</td></tr>';
    
    try {
        const response = await fetch(API_URL);
        const result = await response.json();
        
        if (result.status === 'success' && result.data.length > 0) {
            displayProducts(result.data);
        } else {
            tbody.innerHTML = '<tr class="empty-row"><td colspan="7">📭 No products found. Add your first product above!</td></tr>';
        }
    } catch (error) {
        console.error('Error loading products:', error);
        tbody.innerHTML = '<tr class="empty-row"><td colspan="7">❌ Error loading products. Make sure XAMPP is running.</td></tr>';
    }
}

// Display products in table
function displayProducts(products) {
    const tbody = document.getElementById('productsTableBody');
    tbody.innerHTML = '';
    
    products.forEach(product => {
        const row = tbody.insertRow();
        row.innerHTML = `
            <td>${product.id}</td>
            <td><strong>${escapeHtml(product.name)}</strong></td>
            <td>${escapeHtml(product.description || '-')}</td>
            <td>$${parseFloat(product.price).toFixed(2)}</td>
            <td>${product.quantity}</td>
            <td>${escapeHtml(product.category || '-')}</td>
            <td class="actions">
                <button class="btn btn-edit" onclick="openEditModal(${product.id})">✏️ Edit</button>
                <button class="btn btn-danger" onclick="deleteProduct(${product.id})">🗑️ Delete</button>
            </td>
        `;
    });
}

// Add new product (POST)
async function addProduct(event) {
    event.preventDefault();
    
    const productData = {
        name: document.getElementById('productName').value,
        description: document.getElementById('productDesc').value,
        price: parseFloat(document.getElementById('productPrice').value),
        quantity: parseInt(document.getElementById('productQty').value) || 0,
        category: document.getElementById('productCat').value
    };
    
    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(productData)
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            showAlert(`✅ Product "${productData.name}" added successfully! (ID: ${result.product_id})`, 'success');
            document.getElementById('addProductForm').reset();
            loadProducts();
        } else {
            showAlert(`❌ Failed: ${result.message}`, 'error');
        }
    } catch (error) {
        console.error('Error adding product:', error);
        showAlert('❌ Network error. Check if XAMPP is running.', 'error');
    }
}

// Open edit modal and load product data
async function openEditModal(productId) {
    try {
        const response = await fetch(`${API_URL}?id=${productId}`);
        const result = await response.json();
        
        if (result.status === 'success') {
            const product = result.data;
            document.getElementById('editProductId').value = product.id;
            document.getElementById('editName').value = product.name;
            document.getElementById('editDesc').value = product.description || '';
            document.getElementById('editPrice').value = product.price;
            document.getElementById('editQty').value = product.quantity;
            document.getElementById('editCat').value = product.category || 'Other';
            
            document.getElementById('editModal').style.display = 'flex';
        }
    } catch (error) {
        console.error('Error loading product for edit:', error);
        showAlert('❌ Failed to load product details', 'error');
    }
}

// Update product (PUT)
async function updateProduct(event) {
    event.preventDefault();
    
    const productId = document.getElementById('editProductId').value;
    
    const productData = {
        name: document.getElementById('editName').value,
        description: document.getElementById('editDesc').value,
        price: parseFloat(document.getElementById('editPrice').value),
        quantity: parseInt(document.getElementById('editQty').value) || 0,
        category: document.getElementById('editCat').value
    };
    
    try {
        const response = await fetch(`${API_URL}?id=${productId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(productData)
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            showAlert(`✅ Product updated successfully!`, 'success');
            closeEditModal();
            loadProducts();
        } else {
            showAlert(`❌ Update failed: ${result.message}`, 'error');
        }
    } catch (error) {
        console.error('Error updating product:', error);
        showAlert('❌ Network error', 'error');
    }
}

// Delete product (DELETE)
async function deleteProduct(productId) {
    if (!confirm('⚠️ Are you sure you want to delete this product? This action cannot be undone.')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}?id=${productId}`, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.status === 'success') {
            showAlert(`✅ Product deleted successfully!`, 'success');
            loadProducts();
        } else {
            showAlert(`❌ Delete failed: ${result.message}`, 'error');
        }
    } catch (error) {
        console.error('Error deleting product:', error);
        showAlert('❌ Network error', 'error');
    }
}

// Close edit modal
function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
    document.getElementById('editProductForm').reset();
}

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('editModal');
    if (event.target === modal) {
        closeEditModal();
    }
}

// Helper function to escape HTML
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}