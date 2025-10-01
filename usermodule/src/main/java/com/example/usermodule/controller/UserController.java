//package com.example.usermodule.controller;
//
//import com.example.usermodule.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Set;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    /**
//     * Gán roles cho user
//     */
//    @PostMapping("/{userId}/roles")
//    public ResponseEntity<?> assignRoles(
//            @PathVariable Long userId,
//            @RequestBody Set<Long> roleIds
//    ) {
//        userService.assignRolesToUser(userId, roleIds);
//        return ResponseEntity.ok("Roles assigned successfully");
//    }
//}
