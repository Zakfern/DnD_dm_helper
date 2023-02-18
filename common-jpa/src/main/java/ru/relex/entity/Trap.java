package ru.relex.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.relex.entity.enums.UserState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "Id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trap_data")
public class Trap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime addedDateDate;
    private Long Userid;
    private String trapName;
    private String trapDescription;
    private String trapEnvironment;
}
