<?php
use PHPUnit\Framework\TestCase;

class ProductsAPITest extends TestCase
{
    private $baseUrl = "http://localhost/ecommerce_api/backend/api/products.php";
    
    public function testGetAllProducts()
    {
        $response = file_get_contents($this->baseUrl);
        $data = json_decode($response, true);
        
        $this->assertArrayHasKey("status", $data);
        $this->assertEquals("success", $data["status"]);
        echo "✅ GET all products works\n";
    }
    
    public function testGetSingleProduct()
    {
        $response = file_get_contents($this->baseUrl . "?id=1");
        $data = json_decode($response, true);
        
        $this->assertArrayHasKey("status", $data);
        echo "✅ GET single product works\n";
    }
    
    public function testCreateProduct()
    {
        $postData = json_encode([
            "name" => "PHPUnit Test Product",
            "description" => "Created by PHPUnit test",
            "price" => 99.99,
            "quantity" => 10,
            "category" => "Testing"
        ]);
        
        $options = [
            "http" => [
                "header" => "Content-Type: application/json\r\n",
                "method" => "POST",
                "content" => $postData
            ]
        ];
        
        $context = stream_context_create($options);
        $response = file_get_contents($this->baseUrl, false, $context);
        $data = json_decode($response, true);
        
        $this->assertEquals("success", $data["status"]);
        echo "✅ POST create product works\n";
    }
    
    public function testUpdateProduct()
    {
        // First create a test product to update
        $postData = json_encode([
            "name" => "Product To Update",
            "price" => 50.00,
            "quantity" => 5,
            "category" => "Test"
        ]);
        
        $options = [
            "http" => [
                "header" => "Content-Type: application/json\r\n",
                "method" => "POST",
                "content" => $postData
            ]
        ];
        
        $context = stream_context_create($options);
        $response = file_get_contents($this->baseUrl, false, $context);
        $data = json_decode($response, true);
        
        if ($data["status"] === "success") {
            $productId = $data["product_id"];
            
            // Now update the product
            $updateData = json_encode([
                "name" => "Updated Product Name",
                "price" => 75.00
            ]);
            
            $updateOptions = [
                "http" => [
                    "header" => "Content-Type: application/json\r\n",
                    "method" => "PUT",
                    "content" => $updateData
                ]
            ];
            
            $updateContext = stream_context_create($updateOptions);
            $updateResponse = file_get_contents($this->baseUrl . "?id=" . $productId, false, $updateContext);
            $updateResult = json_decode($updateResponse, true);
            
            $this->assertEquals("success", $updateResult["status"]);
            echo "✅ PUT update product works\n";
        }
    }
    
    public function testDeleteProduct()
    {
        // First create a test product to delete
        $postData = json_encode([
            "name" => "Product To Delete",
            "price" => 25.00,
            "quantity" => 1,
            "category" => "Test"
        ]);
        
        $options = [
            "http" => [
                "header" => "Content-Type: application/json\r\n",
                "method" => "POST",
                "content" => $postData
            ]
        ];
        
        $context = stream_context_create($options);
        $response = file_get_contents($this->baseUrl, false, $context);
        $data = json_decode($response, true);
        
        if ($data["status"] === "success") {
            $productId = $data["product_id"];
            
            // Now delete the product
            $deleteOptions = [
                "http" => [
                    "method" => "DELETE"
                ]
            ];
            
            $deleteContext = stream_context_create($deleteOptions);
            $deleteResponse = file_get_contents($this->baseUrl . "?id=" . $productId, false, $deleteContext);
            $deleteResult = json_decode($deleteResponse, true);
            
            $this->assertEquals("success", $deleteResult["status"]);
            echo "✅ DELETE product works\n";
        }
    }
}