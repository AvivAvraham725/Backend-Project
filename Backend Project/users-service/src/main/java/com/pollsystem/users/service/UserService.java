package com.pollsystem.users.service;

import com.pollsystem.users.model.User;
import com.pollsystem.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String POLLS_SERVICE_URL = "http://localhost:8081/api/polls";

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        try {
            String url = POLLS_SERVICE_URL + "/byUser/" + id;
            restTemplate.delete(url);
            System.out.println(" Deleted user's polls from Polls Service for user ID " + id);
        } catch (Exception e) {
            System.err.println(" Failed to delete polls for user ID " + id + ": " + e.getMessage());
        }
        userRepository.deleteById(id);
    }
}


