package uz.md.shopapp.domain.template;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class AbsEntity extends AbsTimeStampEntity {
    @CreatedBy
    @Column(updatable = false)
    private UUID addedById;

    @LastModifiedBy
    private UUID updatedById;

    private boolean deleted;

    private boolean active;
}
