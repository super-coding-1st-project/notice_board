package com.github.supercodingspring.supercodingproject1st.repository.token;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "invalidToken")
public class Token {
    @Id @GeneratedValue
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "valid")
    private Boolean valid;
}
