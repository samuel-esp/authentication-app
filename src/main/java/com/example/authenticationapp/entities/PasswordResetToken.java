package com.example.authenticationapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter @Setter
public class PasswordResetToken {

        @Id
        @Column(name = "id", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String token;

        private static final int EXPIRATION = 60 * 24;

        @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
        @JoinColumn(nullable = false, name = "user_id")
        private User user;

        /*private Date expiryDate;*/

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public PasswordResetToken(String token, User user){
                this.user = user;
                this.token = token;
        }

        public PasswordResetToken(String token){
                this.token = token;
        }

        public PasswordResetToken(){

        }


}
