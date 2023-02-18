package ru.relex.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "Id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "character")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime addedDateDate;
    private Long Userid;
    private String charType;
    private String charName;
    private String charProfession;
    private String charDescription;
    private String charStats;//all stats? Json?
    private String charEnvironment;
}
