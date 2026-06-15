<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

require_once "../config/db_connect.php";

$database = new Database();
$db = $database->getConnection();

$data = json_decode(file_get_contents("php://input"), true);
$action = $data['action'] ?? '';

if ($action === 'signup') {
    // Check if email exists
    $checkQuery = "SELECT id FROM admins WHERE email = :email";
    $checkStmt = $db->prepare($checkQuery);
    $checkStmt->bindParam(":email", $data['email']);
    $checkStmt->execute();
    
    if ($checkStmt->rowCount() > 0) {
        echo json_encode(["status" => "error", "message" => "Email already exists"]);
        exit();
    }
    
    // Insert new admin
    $query = "INSERT INTO admins (first_name, last_name, email, password) 
              VALUES (:first_name, :last_name, :email, :password)";
    $stmt = $db->prepare($query);
    
    $hashedPassword = password_hash($data['password'], PASSWORD_DEFAULT);
    
    $stmt->bindParam(":first_name", $data['firstName']);
    $stmt->bindParam(":last_name", $data['lastName']);
    $stmt->bindParam(":email", $data['email']);
    $stmt->bindParam(":password", $hashedPassword);
    
    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Account created successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Signup failed"]);
    }
    
} elseif ($action === 'login') {
    $query = "SELECT id, first_name, last_name, email, password FROM admins WHERE email = :email";
    $stmt = $db->prepare($query);
    $stmt->bindParam(":email", $data['email']);
    $stmt->execute();
    
    if ($stmt->rowCount() > 0) {
        $admin = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (password_verify($data['password'], $admin['password'])) {
            echo json_encode([
                "status" => "success", 
                "message" => "Login successful",
                "name" => $admin['first_name'] . " " . $admin['last_name'],
                "email" => $admin['email']
            ]);
        } else {
            echo json_encode(["status" => "error", "message" => "Invalid password"]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "Email not found"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid action"]);
}
?>