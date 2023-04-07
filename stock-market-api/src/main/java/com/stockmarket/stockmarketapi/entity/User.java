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
    @Column(name = "username", nullable = false)
    private String username;

    @NonNull
    @Column(name = "password", nullable = false)
    private String password;

    @NonNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active", nullable = false)
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

    // private void setUpdatedDate() {
    //     setDateFormat(updatedDate);
    // }

    private void setDateFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(formatter);
        LocalDateTime parsedDate = LocalDateTime.parse(formattedDate, formatter);
        dateTime = parsedDate;
    }

}
