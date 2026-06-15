<?php
use PHPUnit\Framework\TestCase;

class DatabaseTest extends TestCase
{
    public function testDatabaseConnection()
    {
        require_once __DIR__ . '/../config/db_connect.php';
        
        $database = new Database();
        $db = $database->getConnection();
        
        $this->assertNotNull($db, "Database connection failed");
        echo "✅ Database connection successful\n";
    }
}