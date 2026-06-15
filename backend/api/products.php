<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Include database connection
require_once "../config/db_connect.php";

// Get HTTP method
$method = $_SERVER['REQUEST_METHOD'];

// Get database connection
$database = new Database();
$db = $database->getConnection();

// For PUT and DELETE, get ID from URL
$id = isset($_GET['id']) ? $_GET['id'] : null;

// Helper function to validate numeric values
function validateNumeric($value, $fieldName, &$errors) {
    if ($value !== null && $value !== '') {
        if (!is_numeric($value)) {
            $errors[] = "$fieldName must be a number";
        } else if (floatval($value) < 0) {
            $errors[] = "$fieldName cannot be negative";
        }
    }
}

// Handle different HTTP methods
switch($method) {
    case 'GET':
        if($id) {
            // GET single product
            $query = "SELECT * FROM products WHERE id = :id";
            $stmt = $db->prepare($query);
            $stmt->bindParam(":id", $id);
            $stmt->execute();
            $product = $stmt->fetch(PDO::FETCH_ASSOC);
            
            if($product) {
                echo json_encode(["status" => "success", "data" => $product]);
            } else {
                echo json_encode(["status" => "error", "message" => "Product not found", "code" => 404]);
            }
        } else {
            // GET all products
            $query = "SELECT * FROM products ORDER BY id DESC";
            $stmt = $db->prepare($query);
            $stmt->execute();
            $products = $stmt->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode(["status" => "success", "data" => $products, "count" => count($products)]);
        }
        break;
        
    case 'POST':
        // Create new product
        $data = json_decode(file_get_contents("php://input"), true);
        $errors = [];
        
        // Validate required fields
        if(!isset($data['name']) || trim($data['name']) === '') {
            $errors[] = "Product name is required";
        }
        if(!isset($data['price'])) {
            $errors[] = "Price is required";
        }
        
        // Validate price (must be positive)
        if(isset($data['price'])) {
            if(!is_numeric($data['price'])) {
                $errors[] = "Price must be a number";
            } else if(floatval($data['price']) <= 0) {
                $errors[] = "Price must be greater than 0";
            }
        }
        
        // Validate quantity (must be non-negative)
        if(isset($data['quantity'])) {
            if(!is_numeric($data['quantity'])) {
                $errors[] = "Quantity must be a number";
            } else if(intval($data['quantity']) < 0) {
                $errors[] = "Quantity cannot be negative";
            }
        }
        
        if(count($errors) > 0) {
            echo json_encode(["status" => "error", "message" => "Validation failed", "errors" => $errors]);
            break;
        }
        
        $query = "INSERT INTO products (name, description, price, quantity, category) 
                  VALUES (:name, :description, :price, :quantity, :category)";
        $stmt = $db->prepare($query);
        
        // Sanitize inputs
        $name = htmlspecialchars(trim($data['name']));
        $description = isset($data['description']) ? htmlspecialchars(trim($data['description'])) : '';
        $price = floatval($data['price']);
        $quantity = isset($data['quantity']) ? intval($data['quantity']) : 0;
        $category = isset($data['category']) ? htmlspecialchars(trim($data['category'])) : '';
        
        $stmt->bindParam(":name", $name);
        $stmt->bindParam(":description", $description);
        $stmt->bindParam(":price", $price);
        $stmt->bindParam(":quantity", $quantity);
        $stmt->bindParam(":category", $category);
        
        if($stmt->execute()) {
            $new_id = $db->lastInsertId();
            echo json_encode(["status" => "success", "message" => "Product created", "product_id" => $new_id]);
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to create product"]);
        }
        break;
        
    case 'PUT':
        // Update product
        if(!$id) {
            echo json_encode(["status" => "error", "message" => "ID is required for update"]);
            break;
        }
        
        $data = json_decode(file_get_contents("php://input"), true);
        $errors = [];
        
        // Validate price if provided
        if(isset($data['price'])) {
            if(!is_numeric($data['price'])) {
                $errors[] = "Price must be a number";
            } else if(floatval($data['price']) <= 0) {
                $errors[] = "Price must be greater than 0";
            }
        }
        
        // Validate quantity if provided
        if(isset($data['quantity'])) {
            if(!is_numeric($data['quantity'])) {
                $errors[] = "Quantity must be a number";
            } else if(intval($data['quantity']) < 0) {
                $errors[] = "Quantity cannot be negative";
            }
        }
        
        if(count($errors) > 0) {
            echo json_encode(["status" => "error", "message" => "Validation failed", "errors" => $errors]);
            break;
        }
        
        // Build update query dynamically
        $updateFields = [];
        $params = [':id' => $id];
        
        if(isset($data['name']) && trim($data['name']) !== '') {
            $updateFields[] = "name = :name";
            $params[':name'] = htmlspecialchars(trim($data['name']));
        }
        if(isset($data['description'])) {
            $updateFields[] = "description = :description";
            $params[':description'] = htmlspecialchars(trim($data['description']));
        }
        if(isset($data['price'])) {
            $updateFields[] = "price = :price";
            $params[':price'] = floatval($data['price']);
        }
        if(isset($data['quantity'])) {
            $updateFields[] = "quantity = :quantity";
            $params[':quantity'] = intval($data['quantity']);
        }
        if(isset($data['category'])) {
            $updateFields[] = "category = :category";
            $params[':category'] = htmlspecialchars(trim($data['category']));
        }
        
        if(empty($updateFields)) {
            echo json_encode(["status" => "error", "message" => "No fields to update"]);
            break;
        }
        
        $query = "UPDATE products SET " . implode(", ", $updateFields) . " WHERE id = :id";
        $stmt = $db->prepare($query);
        
        foreach($params as $key => &$val) {
            $stmt->bindParam($key, $val);
        }
        
        if($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Product updated", "product_id" => $id]);
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to update product"]);
        }
        break;
        
    case 'DELETE':
        // Delete product
        if(!$id) {
            echo json_encode(["status" => "error", "message" => "ID is required for delete"]);
            break;
        }
        
        $query = "DELETE FROM products WHERE id = :id";
        $stmt = $db->prepare($query);
        $stmt->bindParam(":id", $id);
        
        if($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Product deleted", "product_id" => $id]);
        } else {
            echo json_encode(["status" => "error", "message" => "Failed to delete product"]);
        }
        break;
        
    default:
        echo json_encode(["status" => "error", "message" => "Method not allowed"]);
        break;
}
?>
