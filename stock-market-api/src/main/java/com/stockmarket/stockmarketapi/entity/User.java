package com.stockmarket.stockmarketapi.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NonNull
    @NotNull(message = "Username cannot be null. Please try again.")
    @NotBlank(message = "Username cannot be blank. Please try again.")
    @Size(min = 8, max = 20,
            message = "Username must have 8 - 20 characters in length. Please try again.")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$",
            message = "Username cannot contain special characters other than underscore '_'.")
    @Column(name = "username", nullable = false)
    private String username;

    @NonNull
    @NotNull(message = "Password cannot be null. Please try again.")
    @NotBlank(message = "Password cannot be blank. Please try again.")
    @Size(min = 10, max = 20,
            message = "Password must have 10 - 20 characters in length. Please try again.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit and one special character.")
    @Column(name = "password", nullable = false)
    private String password;

    @NonNull
    @NotNull(message = "Email cannot be null. Please try again.")
    @NotBlank(message = "Email cannot be blank. Please try again.")
    @Size(max = 255,
            message = "Email address has a maximum length of 255 characters in length. Please try again.")
    @Email(message = "Invalid Email Address. Please try again.")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active", nullable = false)
    @Digits(integer = 1, fraction = 0, message = "isActive field must be either 0 or 1")
    private int isActive;

    @Column(name = "added_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addedDate = LocalDateTime.now();

    @Column(name = "updated_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate = LocalDateTime.now();

    // @PrePersist ensures that this method is called before the entity is inserted into database
    @PrePersist
    private void setAddedDate() {
        setDateFormat(addedDate);
    }

    private void setUpdatedDate() {
        setDateFormat(updatedDate);
    }

    private void setDateFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(formatter);
        LocalDateTime parsedDate = LocalDateTime.parse(formattedDate, formatter);
        dateTime = parsedDate;
    }

}
