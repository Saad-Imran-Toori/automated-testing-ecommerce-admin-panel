<?php
use PHPUnit\Framework\TestCase;

class AuthAPITest extends TestCase
{
    private $baseUrl = "http://localhost/ecommerce_api/backend/api/auth.php";
    
    public function testSignup()
    {
        $uniqueEmail = "test_" . time() . "@example.com";
        
        $postData = json_encode([
            "action" => "signup",
            "firstName" => "Test",
            "lastName" => "User",
            "email" => $uniqueEmail,
            "password" => "password123"
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
        echo "✅ Signup works\n";
    }
    
    public function testLogin()
    {
        // First create a test user
        $uniqueEmail = "login_test_" . time() . "@example.com";
        
        $signupData = json_encode([
            "action" => "signup",
            "firstName" => "Login",
            "lastName" => "Tester",
            "email" => $uniqueEmail,
            "password" => "testpass123"
        ]);
        
        $options = [
            "http" => [
                "header" => "Content-Type: application/json\r\n",
                "method" => "POST",
                "content" => $signupData
            ]
        ];
        
        $context = stream_context_create($options);
        file_get_contents($this->baseUrl, false, $context);
        
        // Now test login
        $loginData = json_encode([
            "action" => "login",
            "email" => $uniqueEmail,
            "password" => "testpass123"
        ]);
        
        $loginOptions = [
            "http" => [
                "header" => "Content-Type: application/json\r\n",
                "method" => "POST",
                "content" => $loginData
            ]
        ];
        
        $loginContext = stream_context_create($loginOptions);
        $response = file_get_contents($this->baseUrl, false, $loginContext);
        $data = json_decode($response, true);
        
        $this->assertEquals("success", $data["status"]);
        $this->assertArrayHasKey("name", $data);
        echo "✅ Login works\n";
    }
}