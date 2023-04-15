package org.example;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class User {
    private final String username;
    private final String email;
    private final byte[] passwordHash;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.passwordHash = codePassword(password);
    }

    private byte[] codePassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(salt);
        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    public static List<User> findDuplicates(Collection<User> A, Collection<User> B) {
        HashSet<User> collA = new HashSet<>(A);
        HashSet<User> collB = new HashSet<>(B);
        List<User> duplicates = new ArrayList<>();
        for (User user : collA) {
            if (collB.contains(user)) {
                duplicates.add(user);
            }
        }

        return duplicates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username)
                && Objects.equals(email, user.email)
                && Arrays.equals(passwordHash, user.passwordHash);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(username, email);
        result = 31 * result + Arrays.hashCode(passwordHash);
        return result;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(username)
                .append("; ")
                .append(email);
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        User user1 = new User("Dima", "Dima@mail.ru", "ekaterinburg2003");
        User user2 = new User("Stephan", "Stephan@mail.ru", "Lubov2015");
        User user3 = new User("Liza", "Liza@mail.ru", "17april");
        User user4 = new User("Nastya", "Nastya@yandex.ru", "Stephan15");

        Set<User> collA = new HashSet<>();
        Set<User> collB = new HashSet<>();

        collA.add(user1);
        collA.add(user2);
        collA.add(user4);

        collB.add(user3);
        collB.add(user4);
        collB.add(user2);

        List<User> duplicates = User.findDuplicates(collA, collB);
        duplicates.forEach(System.out::println);
        System.out.println(duplicates.size());
    }
}
